package manager;

public class TaskTimeException extends RuntimeException {

    public TaskTimeException(String message) {
        super("Попробуйте другие временные интервалы");
    }
}
