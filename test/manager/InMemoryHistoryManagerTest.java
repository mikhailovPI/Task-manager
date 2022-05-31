
package manager;

import manager.history.InMemoryHistoryManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.StatusTask;
import task.Subtask;
import task.Task;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest  {

    InMemoryHistoryManager historyManager;
    Task taskTestOne;
    Task taskTestTwo;
    Epic epicTestOne;
    Epic epicTestTwo;
    Subtask subtaskTestOne;
    Subtask subtaskTestTwo;

    @BeforeEach
    void init() {
        historyManager = new InMemoryHistoryManager();
        taskTestOne = new Task("task 1", "Описание Task 1", 1, StatusTask.NEW,
                40, LocalDateTime.of(2022, Month.MAY, 26, 11, 0));
        taskTestTwo = new Task("task 2", "Описание Task 2", 2, StatusTask.NEW,
                40, LocalDateTime.of(2022, Month.MAY, 20, 11, 0));

        epicTestOne = new Epic("Epic 1", "Описание Epic 1", 3, StatusTask.NEW,
                40, LocalDateTime.of(2022, Month.MAY, 26, 11, 0));
        epicTestTwo = new Epic("Epic 2", "Описание Epic 2", 4, StatusTask.NEW,
                40, LocalDateTime.of(2022, Month.MAY, 26, 11, 0));

        subtaskTestOne = new Subtask("Subtask 1", "Описание Subtask 1", 5,
                StatusTask.IN_PROGRESS, 40,
                LocalDateTime.of(2022, Month.MAY, 25, 10, 0), epicTestOne.getId());

        subtaskTestTwo = new Subtask("Subtask 2", "Описание Subtask 2", 6,
                StatusTask.NEW, 40,
                LocalDateTime.of(2022, Month.MAY, 25, 11, 0), epicTestTwo.getId());
    }

    @Test
    void getHistoryTest() {
        historyManager.add(taskTestOne);
        historyManager.add(epicTestTwo);
        historyManager.add(subtaskTestOne);

        assertFalse(historyManager.getHistory().isEmpty(), "Список истории пустой");
        assertEquals(3, historyManager.getHistory().size(), "Ожидался другой размер списка");
        assertEquals(subtaskTestOne, historyManager.getHistory().get(2), "Под данным индексом другой объект");
    }

    @Test
    void addTest() {
        historyManager.add(taskTestTwo);
        historyManager.add(epicTestOne);
        List<Task> history = historyManager.getHistory();

        assertFalse(history.isEmpty(), "Список истории пустой");
        assertEquals(2, historyManager.getHistory().size(), "Ожидался другой размер списка");
        assertEquals(history.get(0), taskTestTwo);
    }

    @Test
    void removeTest() {
        historyManager.add(taskTestOne);
        historyManager.add(subtaskTestTwo);
        historyManager.add(taskTestTwo);
        historyManager.add(epicTestOne);
        historyManager.add(epicTestTwo);
        historyManager.add(subtaskTestOne);
        historyManager.remove(1);
        historyManager.remove(3);
        historyManager.remove(5);

        assertFalse(historyManager.getHistory().isEmpty(), "Список истории пустой");
        assertEquals(3, historyManager.getHistory().size(), "Ожидался другой размер списка");
    }

    @Test
    void isEmptyHistoryTaskTest () {
        assertTrue(historyManager.getHistory().isEmpty(), "Список историй не пустой");
    }

    @Test
    void mustBeRemoveDuplicate () {
        historyManager.add(taskTestOne);
        historyManager.add(subtaskTestTwo);
        historyManager.add(subtaskTestTwo);
        historyManager.add(subtaskTestTwo);
        historyManager.add(taskTestOne);
        historyManager.add(epicTestOne);

        assertFalse(historyManager.getHistory().isEmpty(), "Список истории пустой");
        assertEquals(3, historyManager.getHistory().size(), "Ожидался другой размер списка");
    }
}
