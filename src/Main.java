import Task.Task;
import Task.Subtask;
import Task.Epic;
import TrackerTask.Manager;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {

        Manager manager = new Manager();

        Task taskOne = new Task("Task", "Описание Task", 0, "NEW");

        manager.creatTask(taskOne);

        Subtask subtaskOne = new Subtask("Subtask 1 ", "Описание Subtask 1", 0,
                "IN_PROGRESS", 0);
        Subtask subtaskTwo = new Subtask("Subtask 2", "Описание Subtask 2",0 ,
                "DONE", 0);

        manager.creatSubtask(subtaskOne);
        manager.creatSubtask(subtaskTwo);

        manager.listSubtask();

        Epic epicOne = new Epic("Epic 1", "Описание Epic 1", 0, "", manager.listSubtask());

        manager.creatEpic(epicOne);
    }
}
