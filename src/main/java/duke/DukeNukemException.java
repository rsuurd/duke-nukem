package duke;

public class DukeNukemException extends RuntimeException {
    public DukeNukemException(String message) {
        super(message);
    }

    public DukeNukemException(String message, Throwable cause) {
        super(message, cause);
    }
}
