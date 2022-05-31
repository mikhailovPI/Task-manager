package manager.saveToFile;

import manager.taskManager.InMemoryTaskManager;
import manager.exception.ManagerSaveException;
import task.Epic;
import task.Subtask;
import task.Task;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class FileBackedTasksManager extends InMemoryTaskManager {

    private File file;

    public FileBackedTasksManager(String filePath) {
        file = new File(filePath);
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
    public void deleteTasks() {
        super.deleteTasks();
        save();
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
    public void deleteSubtasks() {
        super.deleteSubtasks();
        save();
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
    public void deleteEpics() {
        super.deleteEpics();
        save();
    }

    @Override
    public void removeEpic(long id) {
        super.removeEpic(id);
        save();
    }

    // Сохранение в файл
    private void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8))) {
            String header = "id,type,name,status,description,epic,duration,startTime";
            writer.append(header);
            writer.newLine();
            for (Map.Entry<Long, Task> longTaskEntry : userTasks.entrySet()) {
                writer.append(CSVTaskSerializator.toString(longTaskEntry.getValue()));
                writer.newLine();
            }
            for (Map.Entry<Long, Epic> longEpicEntry : userEpics.entrySet()) {
                writer.append(CSVTaskSerializator.toString(longEpicEntry.getValue()));
                writer.newLine();
            }
            for (Map.Entry<Long, Subtask> longTaskEntry : userSubtasks.entrySet()) {
                writer.append(CSVTaskSerializator.toString(longTaskEntry.getValue()));
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

    private void load() {
        long idMax = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            reader.readLine();
            while (true) {
                String lineTask = reader.readLine();
                if (lineTask.isBlank()) {
                    break;
                }
                Task task = CSVTaskSerializator.fromString(lineTask);
                long id = task.getId();
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
            }

            String lineHistory = reader.readLine();
            List<Long> taskHistory = CSVTaskSerializator.historyFromString(lineHistory);
            for (Long idHistory : taskHistory) {
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
    }

    // Чтение из файла
    public static FileBackedTasksManager loadFromFile(String backedFile) {
        FileBackedTasksManager manager = new FileBackedTasksManager(backedFile);
        manager.load();
        return manager;
    }
}