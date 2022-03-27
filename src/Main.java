import Task.Task;
import Task.Subtask;
import Task.Epic;
import TrackerTask.Manager;

public class Main {
    public static void main(String[] args) {

        Manager manager = new Manager();

        Task taskOne = new Task("Задача", "Придумать описание", 0, "NEW");
        Task taskTwo = new Task("Прогулка", "Одеться по погоде и выбрать маршут прогулки", 0,
                "NEW");
        Task taskFour = new Task("Зал", "Придумать цикл тренировок", 0, "NEW");
        manager.creatTask(taskOne);
        manager.creatTask(taskTwo);
        manager.updateTask(taskFour);

        Subtask subtaskOne = new Subtask("Сборка коробок", "Собрать вещи в коробки", 0,
                "IN_PROGRESS", 0);

        manager.creatSubtask(subtaskOne);

        /*Epic epicOne = new Epic("Эпик 1", "Описание эпика 1", 0, "NEW", manager.listSubtask());

        manager.creatEpic(epicOne);*/

/*        manager.creatTask(taskOne, "NEW");
        manager.creatTask(taskTwo, "IN_PROGRESS");



        manager.listTask();
        manager.listSubtask();

        manager.updateTask(1, taskFour, "IN_PROGRESS");
        manager.listTask();
        manager.receiveTask(2);

        manager.removeTask(4);
        manager.listTask();
        manager.deleteTask();*/
    }
}
