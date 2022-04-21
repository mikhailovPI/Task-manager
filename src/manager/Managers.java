package manager;

public class Managers {

    private static TaskManager taskManager;
    private static HistoryManager historyManager;

    public static TaskManager getDefault() {
        if (taskManager == null) {
            taskManager = new InMemoryTaskManager();
        }
        return taskManager;
    }

    public static HistoryManager getHistoryDefault() {
        if (historyManager == null) {
            historyManager = new InMemoryHistoryManager();
        }
        return historyManager;
    }
}