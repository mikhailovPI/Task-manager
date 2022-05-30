
package manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.StatusTask;
import task.Subtask;
import task.Task;

import java.io.*;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends ManagerTest<FileBackedTasksManager> {

    @Override
    @BeforeEach
    void creatTaskForTest() {
        taskManager = new FileBackedTasksManager("resources/tasks_test.csv");
        super.creatTaskForTest();
    }

    @BeforeEach
    @Test
    void saveToFileEmptyListTest() {
        try {
            BufferedWriter write = new BufferedWriter(new FileWriter("resources/tasks_test.csv"));
            write.write("");
            write.close();
            BufferedReader reader = new BufferedReader(new FileReader("resources/tasks_test.csv"));
            reader.readLine();
            assertNull(reader.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void saveToFileOneTaskTest() {
        try {
            taskTestOne = new Task("task 1", "Описание Task 1", 0, StatusTask.NEW,
                    40, LocalDateTime.of(2022, Month.MAY, 27, 11, 0));
            taskManager.addTask(taskTestOne);
            BufferedReader reader = new BufferedReader(new FileReader("resources/tasks_test.csv"));
            reader.readLine();
            assertEquals("1,TASK,task 1,NEW,Описание Task 1,no epic,40,дата: 26 May 2022 время: 11:00",
                    reader.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void saveToFileOneEpicTest() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("resources/tasks_test.csv"));
            reader.readLine();
            reader.readLine();
            reader.readLine();
            assertEquals("3,EPIC,Epic 1,DONE,Описание Epic 1,no epic,100,дата: 02 May 2022 время: 11:00",
                    reader.readLine());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void saveToFileOneSubtaskTest() {
        try {
            subtaskTestOne = new Subtask("Subtask 1", "Описание Subtask 1", 0, StatusTask.IN_PROGRESS, 40,
                    LocalDateTime.of(2022, Month.MAY, 25, 11, 0), epicTestOne.getId());
            taskManager.addSubtask(subtaskTestOne);
            BufferedReader reader = new BufferedReader(new FileReader("resources/tasks_test.csv"));
            reader.readLine();
            reader.readLine();
            reader.readLine();
            reader.readLine();
            reader.readLine();
            assertEquals("5,SUBTASK,Subtask 1,DONE,Описание Subtask 1,3,40,дата: 02 May 2022 время: 11:00",
                    reader.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void loadFromFileTaskTest() {
        Map<Long, Task> userTasks = new HashMap();
        taskManager.getTask(1);
        try {
            FileBackedTasksManager.loadFromFile("resources/tasks_test.csv");
            BufferedReader bufferedReader = new BufferedReader(new FileReader("resources/tasks_test.csv"));
            bufferedReader.readLine();
            String line = bufferedReader.readLine();
            Task task = CSVTaskSerializator.fromString(line);
            userTasks.put(task.getId(), task);
            assertEquals("Task{nameTask='task 1', description='Описание Task 1', id=1, status='NEW'}",
                    userTasks.get(task.getId()).toString(), "Возвращаемая задача не совпадает");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void loadFromFileSubtaskTest() {
        Map<Long, Task> userSubtask = new HashMap();
        taskManager.getTask(1);
        try {
            FileBackedTasksManager.loadFromFile("resources/tasks_test.csv");
            BufferedReader bufferedReader = new BufferedReader(new FileReader("resources/tasks_test.csv"));
            bufferedReader.readLine();
            bufferedReader.readLine();
            bufferedReader.readLine();
            bufferedReader.readLine();
            bufferedReader.readLine();
            String line = bufferedReader.readLine();
            Task subtask = CSVTaskSerializator.fromString(line);
            userSubtask.put(subtask.getId(), subtask);
            assertEquals("Task{nameTask='Subtask 1', description='Описание Subtask 1', id=5, status='DONE'}",
                    userSubtask.get(subtask.getId()).toString(), "Возвращаемая задача не совпадает");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void loadFromFileEpicTest() {
        Map<Long, Task> userEpic = new HashMap();
        taskManager.getTask(1);
        try {
            FileBackedTasksManager.loadFromFile("resources/tasks_test.csv");
            BufferedReader bufferedReader = new BufferedReader(new FileReader("resources/tasks_test.csv"));
            bufferedReader.readLine();
            bufferedReader.readLine();
            bufferedReader.readLine();
            String line = bufferedReader.readLine();
            Task task = CSVTaskSerializator.fromString(line);
            userEpic.put(task.getId(), task);
            assertEquals("Task{nameTask='Epic 1', description='Описание Epic 1', id=3, status='DONE'}",
                    userEpic.get(task.getId()).toString(), "Возвращаемая задача не совпадает");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void loadFromFileHistoryTest() {
        taskManager.getTask(1);
        taskManager.getSubtask(8);
        taskManager.getEpic(4);
        taskManager.getSubtask(5);
        taskManager.getTask(2);
        taskManager.getSubtask(6);
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("resources/tasks_test.csv"));
            bufferedReader.readLine();
            bufferedReader.readLine();
            bufferedReader.readLine();
            bufferedReader.readLine();
            bufferedReader.readLine();
            bufferedReader.readLine();
            bufferedReader.readLine();
            bufferedReader.readLine();
            bufferedReader.readLine();
            bufferedReader.readLine();
            String line = bufferedReader.readLine();
            List<Long> taskHistory = CSVTaskSerializator.historyFromString(line);
            assertEquals(6, taskHistory.size(), "Количество задач в истории должно совпадать");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
