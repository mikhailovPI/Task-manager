import manager.*;
import task.Epic;
import task.StatusTask;
import task.Subtask;
import task.Task;

import java.time.LocalDateTime;
import java.time.Month;

import static manager.CSVTaskSerializator.filePath;

public class Main {
    public static void main(String[] args) {

        TaskManager manager = Managers.getDefault();

        Task taskOne = new Task("task 1", "Описание Task 1", 0, StatusTask.NEW, 40,
                LocalDateTime.of(2022, Month.MAY, 26, 11, 0));
        Task taskTwo = new Task("task 2", "Описание Task 2", 0, StatusTask.DONE, 30,
                LocalDateTime.of(2022, Month.MAY, 25, 10, 20));
        Task taskThree = new Task("task 3", "Описание Task 3", 0, StatusTask.DONE, 100,
                LocalDateTime.of(3000, Month.MAY, 20, 8, 30));
        Task taskFour = new Task("task 10", "Описание Task 10", 4, StatusTask.NEW, 10,
                LocalDateTime.of(2022, Month.MAY, 28, 11, 0));


        manager.addTask(taskOne);
        manager.addTask(taskTwo);
        manager.addTask(taskThree);
        manager.addTask(taskFour);

        Epic epicOne = new Epic("Epic 1", "Описание Epic 1", 0, StatusTask.DONE, 40,
                LocalDateTime.of(2022, Month.MAY, 26, 10, 00));
        Epic epicTwo = new Epic("Epic 2", "Описание Epic 2", 0, StatusTask.DONE, 40,
                LocalDateTime.of(2022, Month.MAY, 26, 10, 0));

        manager.addEpic(epicOne);
        manager.addEpic(epicTwo);

        Subtask subtaskOne = new Subtask("Subtask 1", "Описание Subtask 1", 0,
                StatusTask.DONE, 40,
                LocalDateTime.of(2022, Month.MAY, 26, 11, 0), epicOne.getId());
        Subtask subtaskTwo = new Subtask("Subtask 2", "Описание Subtask 2", 0,
                StatusTask.DONE, 40,
                LocalDateTime.of(2022, Month.MAY, 23, 11, 0),
                epicOne.getId());
        Subtask subtaskThree = new Subtask("Subtask 3", "Описание Subtask 3", 0,
                StatusTask.NEW, 40, LocalDateTime.of(2022, Month.JUNE, 1, 11, 40),
                epicTwo.getId());
        Subtask subtaskFour = new Subtask("Subtask 4", "Описание Subtask 4", 0,
                StatusTask.DONE, 40,
                LocalDateTime.of(2022, Month.MAY, 23, 11, 0), epicOne.getId());
        Subtask subtaskFive = new Subtask("Subtask 4", "Описание Subtask 4", 0,
                StatusTask.DONE, 40,
                LocalDateTime.of(2022, Month.MAY, 23, 11, 0), epicOne.getId());

        manager.addSubtask(subtaskOne);
        manager.addSubtask(subtaskTwo);
        manager.addSubtask(subtaskThree);
        manager.addSubtask(subtaskFour);
        manager.addSubtask(subtaskFive);

        manager.getTask(taskThree.getId());
        manager.getEpic(epicOne.getId());
        manager.getEpic(epicTwo.getId());
        manager.getSubtask(subtaskOne.getId());
        manager.getSubtask(subtaskTwo.getId());
        manager.getSubtask(subtaskThree.getId());

        System.out.println(manager.getEndTime(epicOne));
        System.out.println(manager.startTimeEpics(epicOne));
        System.out.println(manager.durationEpics(epicOne));
        System.out.println();

        System.out.println(manager.getPrioritizedTasks());


        FileBackedTasksManager fileBackedTasksManager = FileBackedTasksManager.loadFromFile(filePath);

       // System.out.println(fileBackedTasksManager);
    }
}