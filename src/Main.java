import manager.*;
import task.Epic;
import task.StatusTask;
import task.Subtask;
import task.Task;

import java.io.File;

public class Main {
    public static void main(String[] args) {

        TaskManager manager = Managers.getDefault();
        HistoryManager historyManager = Managers.getHistoryDefault();
        TaskManager manager1 = new FileBackedTasksManager(new File("resources/tasks.csv"), true);

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

        System.out.println(taskOne);
        System.out.println(taskTwo);

        System.out.println(epicOne);
        System.out.println(epicTwo + "\n");

        manager.getTask(1);
        manager.getTask(2);
        manager.getTask(3);
        manager.getEpic(4);
        manager.getEpic(5);
        manager.getSubtask(6);
        manager.getSubtask(7);
        manager.getSubtask(8);

        System.out.println(historyManager.getHistory());


        System.out.println(manager.equals(manager1));
        
    }
}
