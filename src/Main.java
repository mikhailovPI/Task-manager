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
        Task taskTwo = new Task("task 2", "Описание Task 2", 0, StatusTask.DONE, 40,
                LocalDateTime.of(2022, Month.MAY, 26, 11, 0));
        Task taskThree = new Task("task 3", "Описание Task 3", 0, StatusTask.DONE, 40,
                LocalDateTime.of(2022, Month.MAY, 26, 11, 0));
        Task taskFour = new Task("task 10", "Описание Task 10", 4, StatusTask.NEW, 40,
                LocalDateTime.of(2022, Month.MAY, 26, 11, 0));


        manager.addTask(taskOne);
        manager.addTask(taskTwo);
        manager.addTask(taskThree);
        manager.addTask(taskFour);

        Epic epicOne = new Epic("Epic 1", "Описание Epic 1", 0, StatusTask.DONE, 40,
                LocalDateTime.of(2022, Month.MAY, 26, 10, 00));

        manager.addEpic(epicOne);

        Subtask subtaskOne = new Subtask("Subtask 1", "Описание Subtask 1", 0,
                StatusTask.DONE, 40,
                LocalDateTime.of(2022, Month.MAY, 2, 11, 0), epicOne.getId());
        Subtask subtaskTwo = new Subtask("Subtask 2", "Описание Subtask 2", 0,
                StatusTask.DONE, 40,
                LocalDateTime.of(2022, Month.MAY, 2, 12, 0),
                epicOne.getId());
        Subtask subtaskFour = new Subtask("Subtask 4", "Описание Subtask 4", 0,
                StatusTask.DONE, 40,
                LocalDateTime.of(2022, Month.MAY, 2, 13, 0), epicOne.getId());
        Subtask subtaskFive = new Subtask("Subtask 4", "Описание Subtask 4", 0,
                StatusTask.DONE, 40,
                LocalDateTime.of(2022, Month.MAY, 2, 14, 0), epicOne.getId());

        manager.addSubtask(subtaskOne);
        manager.addSubtask(subtaskTwo);
        manager.addSubtask(subtaskFour);
        manager.addSubtask(subtaskFive);

        manager.getTask(taskThree.getId());
        manager.getEpic(epicOne.getId());
        manager.getSubtask(subtaskOne.getId());
        manager.getSubtask(subtaskTwo.getId());

        System.out.println(epicOne.startTimeEpics());
        System.out.println(epicOne.durationEpics());
        System.out.println(epicOne.getEndTime());

        FileBackedTasksManager fileBackedTasksManager = FileBackedTasksManager.loadFromFile(filePath);

        System.out.println(fileBackedTasksManager);
    }
}