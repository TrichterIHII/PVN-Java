package de.pvn;

import org.jetbrains.annotations.NotNull;

public class ErrorHandle {
    public enum Error {
        ALREADY_EXISTING ("A variable with this name already exists."),
        NOT_EXISTING ("Variable couldn't be overwritten, cause it'n not existing."),
        WRONG_DATATYPE ("Value don't match with datatype."),
        TOO_MANY_BYTES ("Value is too many bytes large for chosen datatype."),
        UNEXPECTED_TOKEN("Unexpected token."),
        NOT_VALID_NAME("This in not a valid name for a variable.");

        public final String message;
        Error(String error_message) {
            this.message = error_message;
        }
    }

    public static void throwError(@NotNull Error error, int line, int token, String file_path) {
        System.err.println("file " + file_path + ", line " + line + ": " + ", token " + token + error.message);
    }
}
