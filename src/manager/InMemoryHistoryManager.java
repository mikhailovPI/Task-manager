package manager;

import task.Task;

import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    LinkedList<Task> history = new LinkedList<>();
    private final static int SIZE_HISTORY = 10;

    @Override
    public List<Task> getHistory() {
        return history;
    }

    @Override
    public void add(Task task) {
        history.add(task);
        if (history.size() > SIZE_HISTORY) {
            history.removeFirst();
        }
    }
}
