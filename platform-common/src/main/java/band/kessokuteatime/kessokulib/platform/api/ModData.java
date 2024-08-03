package band.kessokuteatime.kessokulib.platform.api;

import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ModData {
    /**
     * Returns the mod's ID.
     */
    String getModId();
    /**
     * Returns the mod's version.
     */
    String getVersion();
    /**
     * Returns all of the mod's dependencies.
     */
    Collection<ModDependency> getDependencies();

    /**
     * Get the name of the mod.
     */
    String getName();

    /**
     * Get the description of the mod.
     */
    String getDescription();
    /**
     * Returns the mod's authors.
     */
    Collection<String> getAuthors();

    Optional<String> getHomepage();

    Optional<String> getSources();

    Optional<String> getIssueTracker();

    /**
     * Returns the mod's licenses.
     */
    Collection<String> getLicense();

    /**
     * Gets the path to the icon.
     *
     * @param preferredSize the preferred size (fabric only)
     * @return the logo file path relative to the file
     */
    Optional<String> getIconFile(int preferredSize);

    /**
     * Gets a list of all possible root paths for the mod.
     * This is especially relevant on Fabric, as a single mod may have multiple source sets
     * (such as client / server-specific ones), each corresponding to one root path.
     *
     * @return A list of root paths belonging to the mod
     */
    List<Path> getRootPaths();

    /**
     * Gets an NIO Path to the given resource contained within the mod file / folder.
     * The path is verified to exist, and an empty optional is returned if it doesn't.
     *
     * @param path The resource to search for
     * @return The path of the resource if it exists, or {@link Optional#empty()} if it doesn't
     */
    Optional<Path> findPath(String... path);
}
