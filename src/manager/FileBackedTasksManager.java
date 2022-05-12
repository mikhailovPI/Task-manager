package manager;

import task.Epic;
import task.Subtask;
import task.Task;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class FileBackedTasksManager extends InMemoryTaskManager {

    CSVTaskSerializator csv = new CSVTaskSerializator();

    private File file;

    public FileBackedTasksManager(File file) {
        this(file, false);
    }

    public FileBackedTasksManager(File file, boolean load) {
        this.file = file;
        if (load) {
            load();
        }
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public Task getTask(long id) {
        Task task = super.getTask(id);
        save();
        return task;
    }

    @Override
    public void removeTask(long id) {
        super.removeTask(id);
        save();
    }

    @Override
    public void addSubtask(Subtask subtask) {
        super.addSubtask(subtask);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public Subtask getSubtask(long id) {
        Subtask subtask = super.getSubtask(id);
        save();
        return subtask;
    }

    @Override
    public void removeSubtask(long id) {
        super.removeSubtask(id);
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public Epic getEpic(long id) {
        Epic epic = super.getEpic(id);
        save();
        return epic;
    }

    @Override
    public void removeEpic(long id) {
        super.removeEpic(id);
        save();
    }

    // Сохранение в файл
    private void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8))) {
            String header = "id,type,name,status,description,epic";
            writer.append(header);
            writer.newLine();
            for (Map.Entry<Long, Task> longTaskEntry : userTasks.entrySet()) {
                writer.append(csv.toString(longTaskEntry.getValue()));
                writer.newLine();
            }
            for (Map.Entry<Long, Epic> longEpicEntry : userEpics.entrySet()) {
                writer.append(csv.toString(longEpicEntry.getValue()));
                writer.newLine();
            }
            for (Map.Entry<Long, Subtask> longTaskEntry : userSubtasks.entrySet()) {
                writer.append(csv.toString(longTaskEntry.getValue()));
                writer.newLine();
            }
            writer.newLine();
            String historyString = CSVTaskSerializator.toString(inMemoryHistoryManager);
            writer.append(historyString);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            throw new ManagerSaveException(e.getMessage());
        }
    }

    // Восстановление из файл
    private void load() {
        long idMax = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            reader.readLine();
            while (true) {
                String lineTask = reader.readLine();
                Task task = csv.fromString(lineTask);
                long id = task.getId();
                // запись задач
                if (task.getTypeTask().equals(TypeTask.TASK)) {
                    userTasks.put(id, task);
                } else if (task.getTypeTask().equals(TypeTask.SUBTASK)) {
                    Subtask subtask = (Subtask) task;
                    userSubtasks.put(id, subtask);
                } else if (task.getTypeTask().equals(TypeTask.EPIC)) {
                    Epic epic = (Epic) task;
                    userEpics.put(id, epic);
                }
                if (idMax < id) {
                    idMax = id;
                }
                if (lineTask.isEmpty()) {
                    break;
                }
            }
            // запись истории
            reader.readLine(); // пропуск пустой строки
            String lineHistory = reader.readLine();
            List<Integer> taskHistory = csv.historyFromString(lineHistory);
            for (Integer idHistory : taskHistory) {
                if (userTasks.containsKey(idHistory)) {
                    inMemoryHistoryManager.add(userTasks.get(idHistory));
                } else if (userEpics.containsKey(idHistory)) {
                    inMemoryHistoryManager.add(userEpics.get(idHistory));
                } else if (userSubtasks.containsKey(idHistory)) {
                    inMemoryHistoryManager.add(userSubtasks.get(idHistory));
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException(e.getMessage());
        }
        index = idMax;
    }

    // Чтение из файла
    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager manager = new FileBackedTasksManager(file, true);
        return manager;
    }
}
