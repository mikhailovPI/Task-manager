import manager.*;
import task.Epic;
import task.StatusTask;
import task.Subtask;
import task.Task;

import static manager.CSVTaskSerializator.filePath;

public class Main {
    public static void main(String[] args) {

        TaskManager manager = Managers.getDefault();

        Task taskOne = new Task("task 1", "Описание Task 1", 0, StatusTask.NEW);
        Task taskTwo = new Task("task 2", "Описание Task 2", 0, StatusTask.DONE);
        Task taskThree = new Task("task 3", "Описание Task 3", 0, StatusTask.DONE);

        manager.addTask(taskOne);
        manager.addTask(taskTwo);
        manager.addTask(taskThree);

        Epic epicOne = new Epic("Epic 1", "Описание Epic 1", 0, StatusTask.NEW);
        Epic epicTwo = new Epic("Epic 2", "Описание Epic 2", 0, StatusTask.NEW);

        manager.addEpic(epicOne);
        manager.addEpic(epicTwo);

        Subtask subtaskOne = new Subtask("Subtask 1 ", "Описание Subtask 1", 0,
                StatusTask.IN_PROGRESS, epicOne.getId());
        Subtask subtaskTwo = new Subtask("Subtask 2", "Описание Subtask 2", 0,
                StatusTask.DONE, epicOne.getId());
        Subtask subtaskThree = new Subtask("Subtask 3", "Описание Subtask 3", 0,
                StatusTask.NEW, epicTwo.getId());

        manager.addSubtask(subtaskOne);
        manager.addSubtask(subtaskTwo);
        manager.addSubtask(subtaskThree);

        manager.getTask(taskOne.getId());
        manager.getTask(taskTwo.getId());
        manager.getTask(taskThree.getId());
        manager.getEpic(epicOne.getId());
        manager.getEpic(epicTwo.getId());
        manager.getSubtask(subtaskOne.getId());
        manager.getSubtask(subtaskTwo.getId());
        manager.getSubtask(subtaskThree.getId());

        FileBackedTasksManager fileBackedTasksManager = FileBackedTasksManager.loadFromFile(filePath);

        System.out.println(fileBackedTasksManager);
    }
}