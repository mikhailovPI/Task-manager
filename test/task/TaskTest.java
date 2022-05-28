package task;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    void getEndTimeTaskTest() {
        Task taskTestOne = new Task("task 1", "Описание Task 1", 0, StatusTask.NEW,
                40, LocalDateTime.of(2022, Month.MAY, 26, 11, 0));

        assertEquals(LocalDateTime.of(2022, Month.MAY, 26, 11, 0).plusMinutes(40),
                taskTestOne.getEndTime(),
                "Время окончания задачи должно совпадать. Проверь startTime и duration");
    }
}