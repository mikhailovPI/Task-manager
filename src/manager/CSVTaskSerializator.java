package manager;

import task.Epic;
import task.StatusTask;
import task.Subtask;
import task.Task;

import java.util.ArrayList;
import java.util.List;

public class CSVTaskSerializator {

    public String toString(Task task) {
        String[] taskАrray = new String[6];
        taskАrray[0] = String.valueOf(task.getId());
        taskАrray[1] = String.valueOf(task.getTypeTask());
        taskАrray[2] = String.valueOf(task.getNameTask());
        taskАrray[3] = String.valueOf(task.getStatus());
        taskАrray[4] = String.valueOf(task.getDescription());
        taskАrray[5] = "";
        if (task.getClass().equals(Subtask.class)) {
            Subtask subtask = (Subtask) task;
            taskАrray[5] = String.valueOf(subtask.getEpicId());
        }
        String stringTask = String.join(",", taskАrray);
        return stringTask;
    }

    public Task fromString(String value) {
        String[] typeTask = value.split(",");
        Task task = new Task(typeTask[2], typeTask[4], Long.parseLong(typeTask[0]),
                StatusTask.valueOf(typeTask[3]));
        if (typeTask[1].equals(String.valueOf(TypeTask.SUBTASK))) {
            Subtask subtask = new Subtask(typeTask[2], typeTask[4], Long.parseLong(typeTask[0]),
                    StatusTask.valueOf(typeTask[3]), Long.parseLong(typeTask[5]));
            return subtask;
        } else if (typeTask[1].equals(String.valueOf(TypeTask.EPIC))) {
            Epic epic = new Epic(typeTask[2], typeTask[4], Long.parseLong(typeTask[0]),
                    StatusTask.valueOf(typeTask[3]));
            return epic;
        }
        return task;
    }

    public static String toString(HistoryManager manager) {
        StringBuilder sb = new StringBuilder();
        for (Task task : manager.getHistory()) {
            sb.append(task.getId()).append(",");
        }
        return sb.toString();
    }

    public List<Long> historyFromString(String value) {
        String[] id = value.split(",");
        List<Long> history = new ArrayList<>();
        for (String i : id) {
            history.add(Long.parseLong(i));
        }
        return history;
    }
}
