/*
 * Copyright (c) 2024 KessokuTeaTime
 *
 * Licensed under the GNU Lesser General Pubic License, Version 3 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.gnu.org/licenses/lgpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package band.kessoku.lib.mixin.networking.fabric;

import java.util.Queue;

import band.kessoku.lib.api.networking.ServerConfigurationNetworkHandlerExtension;
import band.kessoku.lib.impl.networking.NetworkHandlerExtension;
import band.kessoku.lib.impl.networking.server.ServerConfigurationNetworkAddon;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.network.ClientConnection;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ConnectedClientData;
import net.minecraft.server.network.ServerCommonNetworkHandler;
import net.minecraft.server.network.ServerConfigurationNetworkHandler;
import net.minecraft.server.network.ServerPlayerConfigurationTask;

// We want to apply a bit earlier than other mods which may not use us in order to prevent refCount issues
@Mixin(value = ServerConfigurationNetworkHandler.class, priority = 900)
public abstract class ServerConfigurationNetworkHandlerMixin extends ServerCommonNetworkHandler implements NetworkHandlerExtension, ServerConfigurationNetworkHandlerExtension {
    @Shadow
    @Nullable
    private ServerPlayerConfigurationTask currentTask;

    @Shadow
    protected abstract void onTaskFinished(ServerPlayerConfigurationTask.Key key);

    @Shadow
    @Final
    private Queue<ServerPlayerConfigurationTask> tasks;

    @Shadow
    public abstract boolean isConnectionOpen();

    @Shadow
    public abstract void sendConfigurations();

    @Unique
    private ServerConfigurationNetworkAddon kessokulib$addon;

    @Unique
    private boolean kessokulib$sentConfiguration;

    @Unique
    private boolean kessokulib$earlyTaskExecution;

    public ServerConfigurationNetworkHandlerMixin(MinecraftServer server, ClientConnection connection, ConnectedClientData arg) {
        super(server, connection, arg);
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void initAddon(CallbackInfo ci) {
        this.kessokulib$addon = new ServerConfigurationNetworkAddon((ServerConfigurationNetworkHandler) (Object) this, this.server);
        // A bit of a hack but it allows the field above to be set in case someone registers handlers during INIT event which refers to said field
        this.kessokulib$addon.lateInit();
    }

    @Inject(method = "sendConfigurations", at = @At("HEAD"), cancellable = true)
    private void onClientReady(CallbackInfo ci) {
        // Send the initial channel registration packet
        if (this.kessokulib$addon.startConfiguration()) {
            assert currentTask == null;
            ci.cancel();
            return;
        }

        // Ready to start sending packets
        if (!kessokulib$sentConfiguration) {
            this.kessokulib$addon.preConfig();
            kessokulib$sentConfiguration = true;
            kessokulib$earlyTaskExecution = true;
        }

        // Run the early tasks
        if (kessokulib$earlyTaskExecution) {
            if (pollEarlyTasks()) {
                ci.cancel();
                return;
            } else {
                kessokulib$earlyTaskExecution = false;
            }
        }

        // All early tasks should have been completed
        assert currentTask == null;
        assert tasks.isEmpty();

        // Run the vanilla tasks.
        this.kessokulib$addon.config();
    }

    @Unique
    private boolean pollEarlyTasks() {
        if (!kessokulib$earlyTaskExecution) {
            throw new IllegalStateException("Early task execution has finished");
        }

        if (this.currentTask != null) {
            throw new IllegalStateException("Task " + this.currentTask.getKey().id() + " has not finished yet");
        }

        if (!this.isConnectionOpen()) {
            return false;
        }

        final ServerPlayerConfigurationTask task = this.tasks.poll();

        if (task != null) {
            this.currentTask = task;
            task.sendPacket(this::sendPacket);
            return true;
        }

        return false;
    }

    @Override
    public ServerConfigurationNetworkAddon kessokulib$getNetworkAddon() {
        return kessokulib$addon;
    }

    @Override
    public void kessokulib$addTask(ServerPlayerConfigurationTask task) {
        tasks.add(task);
    }

    @Override
    public void kessokulib$completeTask(ServerPlayerConfigurationTask.Key key) {
        if (!kessokulib$earlyTaskExecution) {
            onTaskFinished(key);
            return;
        }

        final ServerPlayerConfigurationTask.Key currentKey = this.currentTask != null ? this.currentTask.getKey() : null;

        if (!key.equals(currentKey)) {
            throw new IllegalStateException("Unexpected request for task finish, current task: " + currentKey + ", requested: " + key);
        }

        this.currentTask = null;
        sendConfigurations();
    }
}
