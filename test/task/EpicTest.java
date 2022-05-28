package task;

import manager.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    Epic epicTestOne;
    Subtask subtaskTestOne;
    Subtask subtaskTestTwo;

    @BeforeEach
    void creatTaskForTest () {
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        epicTestOne = new Epic("Epic 1", "Описание Epic 1", 0, StatusTask.NEW,
                40, LocalDateTime.of(2022, Month.MAY, 2, 11, 0));
        inMemoryTaskManager.addEpic(epicTestOne);

        subtaskTestOne = new Subtask("Subtask 1", "Описание Subtask 1", 0,
                StatusTask.IN_PROGRESS, 40,
                LocalDateTime.of(2022, Month.MAY, 3, 10, 0), epicTestOne.getId());
        subtaskTestTwo = new Subtask("Subtask 2", "Описание Subtask 2", 0,
                StatusTask.NEW, 40,
                LocalDateTime.of(2022, Month.MAY, 3, 11, 0), epicTestOne.getId());
        inMemoryTaskManager.addSubtask(subtaskTestOne);
        inMemoryTaskManager.addSubtask(subtaskTestTwo);

    }

    @Test
    void getEndTimeEpicTest() {
        assertEquals(LocalDateTime.of(2022, Month.MAY, 3, 10, 0).plusMinutes(100L),
                epicTestOne.getEndTime(),
                "Время окончания задачи должно совпадать. Проверь startTime и duration");
    }

    @Test
    void startTimeEpicsTest () {
        assertEquals(LocalDateTime.of(2022, Month.MAY, 3, 10, 0),
                epicTestOne.startTimeEpics(),
                "Время начала эпика должно совпадать. Проверь startTime");
    }

    @Test
    void durationEpicsTest () {
        assertEquals(100L, epicTestOne.durationEpics(),
                "Продолжительность эпика должно совпадать. Проверь duration");

    }
}

