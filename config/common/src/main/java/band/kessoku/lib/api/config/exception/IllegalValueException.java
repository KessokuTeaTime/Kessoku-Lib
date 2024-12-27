package band.kessoku.lib.api.config.exception;

import java.io.Serial;

public class IllegalValueException extends IllegalArgumentException {
    public IllegalValueException() {
    }

    public IllegalValueException(String s) {
        super(s);
    }

    public IllegalValueException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalValueException(Throwable cause) {
        super(cause);
    }

    @Serial
    private static final long serialVersionUID = 4166061622386824154L;
}
