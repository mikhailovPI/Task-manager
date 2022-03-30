import task.Task;
import task.Subtask;
import task.Epic;
import managers.Manager;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {

        Manager manager = new Manager();

        Task taskOne = new Task("task 1", "Описание Task 1", 0, "NEW");
        Task taskTwo = new Task("task 2", "Описание Task 2", 0, "DONE");

        manager.creatTask(taskOne);

        Subtask subtaskOne = new Subtask("Subtask 1 ", "Описание Subtask 1", 0,
                "NEW", 0);
        Subtask subtaskTwo = new Subtask("Subtask 2", "Описание Subtask 2", 0,
                "DONE", 0);
        Subtask subtaskThree = new Subtask("Subtask 2", "Описание Subtask 2", 0,
                "NEW", 0);

        manager.creatSubtask(subtaskOne);
        manager.creatSubtask(subtaskTwo);
        manager.creatSubtask(subtaskThree);

        ArrayList<Integer> subtaskListOne = new ArrayList<>();
        subtaskListOne.add(subtaskOne.getId());
        subtaskListOne.add(subtaskTwo.getId());
        subtaskListOne.add(subtaskThree.getId());

        ArrayList<Integer> subtaskListTwo = new ArrayList<>();
        subtaskListTwo.add(subtaskTwo.getId());

        Epic epicOne = new Epic("Epic 1", "Описание Epic 1", 0, "", subtaskListOne);
        Epic epicTwo = new Epic("Epic 2", "Описание Epic 2", 0, "", subtaskListTwo);

        manager.creatEpic(epicOne);
        manager.creatEpic(epicTwo);

        System.out.println(epicOne);
        System.out.println(epicTwo);

        manager.deleteSubtask();
    }
}
