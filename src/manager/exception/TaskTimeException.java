package manager.exception;

public class TaskTimeException extends RuntimeException {

    public TaskTimeException() {
        getMessage();
    }

    @Override
    public String getMessage() {
        return "В это время выполняется другая задача.";
    }
}