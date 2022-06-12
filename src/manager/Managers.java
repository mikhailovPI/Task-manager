package manager;

import manager.history.InMemoryHistoryManager;
import manager.interfaceClass.HistoryManager;
import manager.interfaceClass.TaskManager;
import manager.server.HttpTaskManager;

public class Managers {

    private static TaskManager taskManager;
    private static HistoryManager historyManager;

    public static TaskManager getDefault() {
        if (taskManager == null) {
            taskManager = new HttpTaskManager();
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