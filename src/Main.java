import Task.Task;
import Task.Subtask;
import Task.Epic;
import TrackerTask.Manager;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {

        Manager manager = new Manager();

        Task taskOne = new Task("Задача", "Придумать описание", 0, "NEW");
        Task taskTwo = new Task("Прогулка", "Одеться по погоде и выбрать маршут прогулки", 0,
                "NEW");
        Task taskFour = new Task("Зал", "Придумать цикл тренировок", 0, "NEW");
        manager.creatTask(taskOne);
        manager.creatTask(taskTwo);


        Epic epicOne = new Epic("Эпик 1", "Описание эпика 1", 0, "", new ArrayList<>() );

        manager.creatEpic(epicOne);

        Subtask subtaskOne = new Subtask("Subtask 1 ", "Описание Subtask 1", 0,
                "IN_PROGRESS", epicOne.getId());
        Subtask subtaskTwo = new Subtask("Subtask 2", "Описание Subtask 2",0 ,
                "IN_PROGRESS", epicOne.getId());

        manager.creatSubtask(subtaskOne);
        manager.creatSubtask(subtaskTwo);

        //manager.getSubtaskByEpic (0);
    }
}
