package band.kessoku.lib.api.util.command;

import band.kessoku.lib.api.KessokuLib;
import band.kessoku.lib.command.KessokuCommand;
import band.kessoku.lib.mixin.command.HelpCommandAccessor;
import com.google.common.collect.Iterables;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.context.ParsedCommandNode;
import com.mojang.brigadier.exceptions.BuiltInExceptionProvider;
import com.mojang.brigadier.exceptions.CommandExceptionType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.CommandNode;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.text.Texts;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ClientCommandUtils {
    private static @Nullable CommandDispatcher<ClientCommandSourceExtension> activeDispatcher;

    public static void setActiveDispatcher(@Nullable CommandDispatcher<ClientCommandSourceExtension> dispatcher) {
        ClientCommandUtils.activeDispatcher = dispatcher;
    }

    public static @Nullable CommandDispatcher<ClientCommandSourceExtension> getActiveDispatcher() {
        return activeDispatcher;
    }

    /**
     * Executes a client-sided command. Callers should ensure that this is only called
     * on slash-prefixed messages and the slash needs to be removed before calling.
     * (This is the same requirement as {@code ClientPlayerEntity#sendCommand}.)
     *
     * @param command the command with slash removed
     * @return true if the command should not be sent to the server, false otherwise
     */
    public static boolean executeCommand(String command) {
        MinecraftClient client = MinecraftClient.getInstance();

        // The interface is implemented on ClientCommandSource with a mixin.
        // noinspection ConstantConditions
        ClientCommandSourceExtension commandSource = (ClientCommandSourceExtension) client.getNetworkHandler().getCommandSource();

        client.getProfiler().push(command);

        try {
            // TODO: Check for server commands before executing.
            //   This requires parsing the command, checking if they match a server command
            //   and then executing the command with the parse results.
            activeDispatcher.execute(command, commandSource);
            return true;
        } catch (CommandSyntaxException e) {
            boolean ignored = isIgnoredException(e.getType());

            if (ignored) {
                KessokuLib.getLogger().debug("Syntax exception for client-sided command '{}'", command, e);
                return false;
            }

            KessokuLib.getLogger().warn("Syntax exception for client-sided command '{}'", command, e);
            commandSource.kessokulib$sendError(getErrorMessage(e));
            return true;
        } catch (Exception e) {
            KessokuLib.getLogger().warn("Error while executing client-sided command '{}'", command, e);
            commandSource.kessokulib$sendError(Text.of(e.getMessage()));
            return true;
        } finally {
            client.getProfiler().pop();
        }
    }

    /**
     * Tests whether a command syntax exception with the type
     * should be ignored and the command sent to the server.
     *
     * @param type the exception type
     * @return true if ignored, false otherwise
     */
    private static boolean isIgnoredException(CommandExceptionType type) {
        BuiltInExceptionProvider builtins = CommandSyntaxException.BUILT_IN_EXCEPTIONS;

        // Only ignore unknown commands and node parse exceptions.
        // The argument-related dispatcher exceptions are not ignored because
        // they will only happen if the user enters a correct command.
        return type == builtins.dispatcherUnknownCommand() || type == builtins.dispatcherParseException();
    }

    // See ChatInputSuggestor.formatException. That cannot be used directly as it returns an OrderedText instead of a Text.
    private static Text getErrorMessage(CommandSyntaxException e) {
        Text message = Texts.toText(e.getRawMessage());
        String context = e.getContext();

        return context != null ? Text.translatable("command.context.parse_error", message, e.getCursor(), context) : message;
    }

    /**
     * Runs final initialization tasks such as {@link CommandDispatcher#findAmbiguities(AmbiguityConsumer)}
     * on the command dispatcher. Also registers a {@code /fcc help} command if there are other commands present.
     */
    public static void finalizeInit() {
        if (!activeDispatcher.getRoot().getChildren().isEmpty()) {
            // Register an API command if there are other commands;
            // these helpers are not needed if there are no client commands
            LiteralArgumentBuilder<ClientCommandSourceExtension> help = ClientCommandManager.literal("help");
            help.executes(ClientCommandUtils::executeRootHelp);
            help.then(ClientCommandManager.argument("command", StringArgumentType.greedyString()).executes(ClientCommandUtils::executeArgumentHelp));

            CommandNode<ClientCommandSourceExtension> mainNode = activeDispatcher.register(ClientCommandManager.literal(KessokuCommand.MOD_ID).then(help));
            activeDispatcher.register(ClientCommandManager.literal(KessokuCommand.MOD_ID).redirect(mainNode));
        }

        // noinspection CodeBlock2Expr
        activeDispatcher.findAmbiguities((parent, child, sibling, inputs) -> {
            KessokuLib.getLogger().warn("Ambiguity between arguments {} and {} with inputs: {}", activeDispatcher.getPath(child), activeDispatcher.getPath(sibling), inputs);
        });
    }

    private static int executeRootHelp(CommandContext<ClientCommandSourceExtension> context) {
        return executeHelp(activeDispatcher.getRoot(), context);
    }

    private static int executeArgumentHelp(CommandContext<ClientCommandSourceExtension> context) throws CommandSyntaxException {
        ParseResults<ClientCommandSourceExtension> parseResults = activeDispatcher.parse(StringArgumentType.getString(context, "command"), context.getSource());
        List<ParsedCommandNode<ClientCommandSourceExtension>> nodes = parseResults.getContext().getNodes();

        if (nodes.isEmpty()) {
            throw HelpCommandAccessor.getFailedException().create();
        }

        return executeHelp(Iterables.getLast(nodes).getNode(), context);
    }

    private static int executeHelp(CommandNode<ClientCommandSourceExtension> startNode, CommandContext<ClientCommandSourceExtension> context) {
        Map<CommandNode<ClientCommandSourceExtension>, String> commands = activeDispatcher.getSmartUsage(startNode, context.getSource());

        for (String command : commands.values()) {
            context.getSource().kessokulib$sendFeedback(Text.literal("/" + command));
        }

        return commands.size();
    }

    public static void addCommands(CommandDispatcher<ClientCommandSourceExtension> target, ClientCommandSourceExtension source) {
        Map<CommandNode<ClientCommandSourceExtension>, CommandNode<ClientCommandSourceExtension>> originalToCopy = new HashMap<>();
        originalToCopy.put(activeDispatcher.getRoot(), target.getRoot());
        copyChildren(activeDispatcher.getRoot(), target.getRoot(), source, originalToCopy);
    }

    /**
     * Copies the child commands from origin to target, filtered by {@code child.canUse(source)}.
     * Mimics vanilla's CommandManager.makeTreeForSource.
     *
     * @param origin         the source command node
     * @param target         the target command node
     * @param source         the command source
     * @param originalToCopy a mutable map from original command nodes to their copies, used for redirects;
     *                       should contain a mapping from origin to target
     */
    private static void copyChildren(
            CommandNode<ClientCommandSourceExtension> origin,
            CommandNode<ClientCommandSourceExtension> target,
            ClientCommandSourceExtension source,
            Map<CommandNode<ClientCommandSourceExtension>, CommandNode<ClientCommandSourceExtension>> originalToCopy
    ) {
        for (CommandNode<ClientCommandSourceExtension> child : origin.getChildren()) {
            if (!child.canUse(source)) continue;

            ArgumentBuilder<ClientCommandSourceExtension, ?> builder = child.createBuilder();

            // Reset the unnecessary non-completion stuff from the builder
            builder.requires(s -> true); // This is checked with the if check above.

            if (builder.getCommand() != null) {
                builder.executes(context -> 0);
            }

            // Set up redirects
            if (builder.getRedirect() != null) {
                builder.redirect(originalToCopy.get(builder.getRedirect()));
            }

            CommandNode<ClientCommandSourceExtension> result = builder.build();
            originalToCopy.put(child, result);
            target.addChild(result);

            if (!child.getChildren().isEmpty()) {
                copyChildren(child, result, source, originalToCopy);
            }
        }
    }
}
