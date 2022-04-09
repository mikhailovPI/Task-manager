import manager.InMemoryHistoryManager;
import task.StatusTask;
import task.Task;
import task.Subtask;
import task.Epic;
import manager.InMemoryTaskManager;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {

        InMemoryTaskManager manager = new InMemoryTaskManager();
        InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();

        Task taskOne = new Task("task 1", "Описание Task 1", 0, StatusTask.NEW);
        Task taskTwo = new Task("task 2", "Описание Task 2", 0, StatusTask.DONE);

        manager.creatTask(taskOne);
        manager.creatTask(taskTwo);


        Subtask subtaskOne = new Subtask("Subtask 1 ", "Описание Subtask 1", 0,
                StatusTask.IN_PROGRESS, 0);
        Subtask subtaskTwo = new Subtask("Subtask 2", "Описание Subtask 2", 0,
                StatusTask.DONE, 0);
        Subtask subtaskThree = new Subtask("Subtask 2", "Описание Subtask 2", 0,
                StatusTask.NEW, 0);


        manager.creatSubtask(subtaskOne);
        manager.creatSubtask(subtaskTwo);
        manager.creatSubtask(subtaskThree);

        ArrayList<Subtask> subtaskListOne = new ArrayList<>();
        subtaskListOne.add(subtaskOne);
        subtaskListOne.add(subtaskTwo);
        subtaskListOne.add(subtaskThree);

        ArrayList<Subtask> subtaskListTwo = new ArrayList<>();
        subtaskListTwo.add(subtaskTwo);

        Epic epicOne = new Epic("Epic 1", "Описание Epic 1", 0, StatusTask.NEW, subtaskListOne);
        Epic epicTwo = new Epic("Epic 2", "Описание Epic 2", 0, StatusTask.NEW, subtaskListTwo);


        manager.creatEpic(epicOne);
        manager.creatEpic(epicTwo);

        manager.getSubtaskByEpic(epicOne);
        manager.getSubtaskByEpic(epicTwo);


        manager.getTask(1);
        manager.getTask(2);
        manager.getSubtask(3);
        manager.getSubtask(4);
        manager.getSubtask(5);
        manager.getSubtask(3);
        manager.getTask(1);
        manager.getEpic(6);
        manager.getTask(2);
        manager.getEpic(7);
        manager.getSubtask(4);



/*        System.out.println(taskOne);
        System.out.println(taskTwo);

        System.out.println(epicOne);
        System.out.println(epicTwo);*/
    }
}
