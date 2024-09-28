package band.kessoku.lib.api.entrypoint;

import band.kessoku.lib.impl.entrypoint.JavaLanguageAdapter;
import band.kessoku.lib.impl.entrypoint.exceptions.LanguageAdapterException;
import band.kessoku.lib.api.platform.ModData;

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
