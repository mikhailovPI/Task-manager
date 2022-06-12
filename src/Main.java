
import manager.*;
import manager.interfaceClass.TaskManager;
import manager.server.HttpTaskServer;
import manager.server.KVServer;
import task.Epic;
import task.StatusTask;
import task.Subtask;
import task.Task;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.Month;

public class Main {
    public static void main(String[] args) throws IOException {

        KVServer kvServer = new KVServer();
        kvServer.start();
        HttpTaskServer taskServer = new HttpTaskServer();
        taskServer.start();

        TaskManager taskManager = Managers.getDefault();
        Task taskOne = new Task("task 1", "Описание task 1", 1, StatusTask.IN_PROGRESS,
                120, LocalDateTime.of(2022, Month.MAY, 1,   0, 0));

        Task taskTwo = new Task("task 2", "Описание task 2", 2, StatusTask.IN_PROGRESS,
                120, LocalDateTime.of(2022, Month.MAY, 2,   0, 0));

        Epic epicOne  = new Epic("Epic 1", "Описание Epic 1", 3, StatusTask.NEW, 100,
                LocalDateTime.of(2022, Month.MAY, 2, 11, 0));
        Epic epicTwo  = new Epic("Epic 2", "Описание Epic 2", 4, StatusTask.NEW, 100,
                LocalDateTime.of(2022, Month.MAY, 3, 11, 0));

        Subtask subtaskOne = new Subtask("Subtask 1", "Описание Subtask 1", 5,
                StatusTask.DONE, 40, LocalDateTime.of(2022, Month.MAY, 2, 15, 0), epicOne.getId());
        Subtask subtaskTwo = new Subtask("Subtask 2", "Описание Subtask 2", 6,
                StatusTask.NEW, 40, LocalDateTime.of(2022, Month.MAY, 3, 15, 0), epicOne.getId());

        taskManager.addTask(taskOne);
        taskManager.addTask(taskTwo);
        taskManager.addEpic(epicOne);
        taskManager.addEpic(epicTwo);
        taskManager.addSubtask(subtaskOne);
        taskManager.addSubtask(subtaskTwo);
        taskManager.getTask(1);
        taskManager.getTask(2);
        taskManager.getEpic(3);
        taskManager.getEpic(4);
        taskManager.getSubtask(5);
        taskManager.getSubtask(6);
        System.out.println("История");
        System.out.println(taskManager.getHistory());
        System.out.println("\nПриоритетные задачи");
        System.out.println(taskManager.getPrioritizedTasks());
        System.out.println("\nВсе задачи");
        System.out.println(taskManager.listTask());
        System.out.println("\nВсе эпики'");
        System.out.println(taskManager.listEpic());
        System.out.println("\nВсе сабтаски");
        System.out.println(taskManager.listSubtask());
        taskServer.stop();
        kvServer.stop();
    }
}
