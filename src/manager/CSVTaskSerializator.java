package manager;

import task.Epic;
import task.StatusTask;
import task.Subtask;
import task.Task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CSVTaskSerializator {

    public static String filePath = "resources/tasks.csv";
    public final static int arraySize = 8;

    public static String toString(Task task) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("дата: dd MMMM yyyy время: HH:mm", Locale.US);
        String[] taskАrray = new String[arraySize];
        taskАrray[0] = String.valueOf(task.getId());
        taskАrray[1] = String.valueOf(task.getTypeTask());
        taskАrray[2] = String.valueOf(task.getNameTask());
        taskАrray[3] = String.valueOf(task.getStatus());
        taskАrray[4] = String.valueOf(task.getDescription());
        taskАrray[5] = "no epic";
        taskАrray[6] = String.valueOf(task.getDuration());
        taskАrray[7] = task.getStartTime().format(formatter);
        if (task.getClass().equals(Subtask.class)) {
            Subtask subtask = (Subtask) task;
            taskАrray[5] = String.valueOf(subtask.getEpicId());
        }
        String stringTask = String.join(",", taskАrray);
        return stringTask;
    }

    public static Task fromString(String value) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("дата: dd MMMM yyyy время: HH:mm", Locale.US);
        String[] typeTask = value.split(",");
        Task task = new Task(typeTask[2], typeTask[4], Long.parseLong(typeTask[0]),
                StatusTask.valueOf(typeTask[3]), Integer.parseInt(typeTask[6]), LocalDateTime.parse(typeTask[7],
                formatter));
        if (typeTask[1].equals(String.valueOf(TypeTask.SUBTASK))) {
            Subtask subtask = new Subtask(typeTask[2], typeTask[4], Long.parseLong(typeTask[0]),
                    StatusTask.valueOf(typeTask[3]), Integer.parseInt(typeTask[6]), LocalDateTime.parse(typeTask[7],
                    formatter), Long.parseLong(typeTask[5]));
            return subtask;
        } else if (typeTask[1].equals(String.valueOf(TypeTask.EPIC))) {
            Epic epic = new Epic(typeTask[2], typeTask[4], Long.parseLong(typeTask[0]),
                    StatusTask.valueOf(typeTask[3]), Integer.parseInt(typeTask[6]), LocalDateTime.parse(typeTask[7],
                    formatter));
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

    public static List<Long> historyFromString(String value) {
        String[] id = value.split(",");
        List<Long> history = new ArrayList<>();
        for (String i : id) {
            history.add(Long.parseLong(i));
        }
        return history;
    }
}
