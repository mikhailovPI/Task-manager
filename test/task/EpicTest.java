package task;

import manager.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    InMemoryTaskManager taskManager;
    Epic epicTestOne;
    Subtask subtaskTestOne;
    Subtask subtaskTestTwo;
    Subtask subtaskTestThree;

    @BeforeEach
    void creatTaskForTest () {
        taskManager = new InMemoryTaskManager();
        epicTestOne = new Epic("Epic 1", "Описание Epic 1", 0, StatusTask.NEW,
                40, LocalDateTime.of(2022, Month.MAY, 3, 15, 0));
        taskManager.addEpic(epicTestOne);

        subtaskTestOne = new Subtask("Subtask 1", "Описание Subtask 1", 0,
                StatusTask.IN_PROGRESS, 40, LocalDateTime.of(2022, Month.MAY, 3, 10, 0), 1);
        subtaskTestTwo = new Subtask("Subtask 2", "Описание Subtask 2", 0,
                StatusTask.NEW, 40, LocalDateTime.of(2022, Month.MAY, 3, 11, 0), 1);
        subtaskTestThree = new Subtask("Subtask 2", "Описание Subtask 2", 0,
                StatusTask.NEW, 40, LocalDateTime.of(2022, Month.MAY, 3, 9, 0), 1);

        taskManager.addSubtask(subtaskTestOne);
        taskManager.addSubtask(subtaskTestTwo);
        taskManager.addSubtask(subtaskTestThree);

    }

    @Test
    void getEndTimeEpicTest() {
        assertEquals(LocalDateTime.of(2022, Month.MAY, 3, 9, 0).plusMinutes(160L),
                epicTestOne.getEndTime(),
                "Время окончания задачи должно совпадать. Проверь startTime и duration");
    }

    @Test
    void startTimeEpicsTest () {
        assertEquals(LocalDateTime.of(2022, Month.MAY, 3, 9, 0),
                epicTestOne.getStartTime(), "Время начала эпика должно совпадать. Проверь startTime");
    }

    @Test
    void durationEpicsTest () {
        assertEquals(160, epicTestOne.getDuration(), "Продолжительность эпика должно совпадать. Проверь duration");
    }

    @Test
    void removeSubtaskFromEpicForDetermineStartTimeTest () {
        taskManager.removeSubtask(4);

        assertEquals(LocalDateTime.of(2022, Month.MAY, 3, 10, 0),
                epicTestOne.getStartTime(), "Время начала эпика должно совпадать. Проверь startTime");
    }

    @Test
    void removeSubtaskFromEpicForDetermineDurationTest () {
        taskManager.removeSubtask(4);

        assertEquals(100, epicTestOne.getDuration(),
                "Продолжительность эпика должно совпадать. Проверь duration");
    }

    @Test
    void updateSubtaskFromEpicTest () {
        Subtask subtaskTestUpdate = new Subtask("Subtask 10 ", "Описание Subtask 10", 2,
                StatusTask.IN_PROGRESS, 40, LocalDateTime.of(2022, Month.MAY, 1, 10, 0), 1);
        taskManager.updateSubtask(subtaskTestUpdate);

        assertEquals(LocalDateTime.of(2022, Month.MAY, 1, 10, 0),
                epicTestOne.getStartTime(), "Время начала эпика должно совпадать. Проверь startTime");
    }


}

