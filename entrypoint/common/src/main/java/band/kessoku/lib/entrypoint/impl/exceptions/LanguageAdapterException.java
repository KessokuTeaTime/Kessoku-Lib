package band.kessoku.lib.entrypoint.impl.exceptions;

public final class LanguageAdapterException extends Exception {
    /**
     * Creates a new language adapter exception.
     *
     * @param s the message
     */
    public LanguageAdapterException(String s) {
        super(s);
    }

    /**
     * Creates a new language adapter exception.
     *
     * @param t the cause
     */
    public LanguageAdapterException(Throwable t) {
        super(t);
    }

    /**
     * Creates a new language adapter exception.
     *
     * @param s the message
     * @param t the cause
     */
    public LanguageAdapterException(String s, Throwable t) {
        super(s, t);
    }
}
