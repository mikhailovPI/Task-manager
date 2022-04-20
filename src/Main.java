import manager.*;
import task.Epic;
import task.StatusTask;
import task.Subtask;
import task.Task;

public class Main {
    public static void main(String[] args) {

        TaskManager manager = Managers.getDefault();
        HistoryManager managerHistory = Managers.getHistoryDefault();

        Task taskOne = new Task("task 1", "Описание Task 1", 0, StatusTask.NEW);
        Task taskTwo = new Task("task 2", "Описание Task 2", 0, StatusTask.DONE);

        manager.creatTask(taskOne);
        manager.creatTask(taskTwo);

        Epic epicOne = new Epic("Epic 1", "Описание Epic 1", 0, StatusTask.NEW);
        Epic epicTwo = new Epic("Epic 2", "Описание Epic 2", 0, StatusTask.NEW);

        manager.creatEpic(epicOne);
        manager.creatEpic(epicTwo);

        Subtask subtaskOne = new Subtask("Subtask 1 ", "Описание Subtask 1", 0,
                StatusTask.IN_PROGRESS, epicOne.getId());
        Subtask subtaskTwo = new Subtask("Subtask 2", "Описание Subtask 2", 0,
                StatusTask.DONE, epicOne.getId());
        Subtask subtaskThree = new Subtask("Subtask 3", "Описание Subtask 3", 0,
                StatusTask.NEW, epicTwo.getId());

        manager.creatSubtask(subtaskOne);
        manager.creatSubtask(subtaskTwo);
        manager.creatSubtask(subtaskThree);

        manager.getEpic(3);
        manager.getTask(1);
        manager.getTask(2);
        manager.getTask(1);
        manager.getSubtask(5);
        manager.getEpic(4);
        manager.getSubtask(6);
        manager.getEpic(3);
        manager.getSubtask(5);
        manager.getTask(2);

        System.out.println(managerHistory.getHistory());

        System.out.println(taskOne);
        System.out.println(taskTwo);

        System.out.println(epicOne);
        System.out.println(epicTwo);



    }
}
