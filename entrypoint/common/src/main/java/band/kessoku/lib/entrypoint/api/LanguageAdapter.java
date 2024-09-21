package band.kessoku.lib.entrypoint.api;

import band.kessoku.lib.entrypoint.impl.JavaLanguageAdapter;
import band.kessoku.lib.entrypoint.impl.exceptions.LanguageAdapterException;
import band.kessoku.lib.platform.api.ModData;

public interface LanguageAdapter {
    /**
     * Get an instance of the default language adapter.
     */
    static LanguageAdapter getDefault() {
        return JavaLanguageAdapter.INSTANCE;
    }

    /**
     * Creates an object of {@code type} from an arbitrary string declaration.
     *
     * @param mod   the mod which the object is from
     * @param value the string declaration of the object
     * @return the created object
     * @throws LanguageAdapterException if a problem arises during creation, such as an invalid declaration
     */
    Object parse(ModData mod, String value) throws LanguageAdapterException;
}
