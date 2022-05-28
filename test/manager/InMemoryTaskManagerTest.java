package manager;

import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InMemoryTaskManagerTest extends ManagerTest<InMemoryTaskManager> {


    @Override
    @BeforeEach
    void creatTaskForTest() {
        taskManager = new InMemoryTaskManager();
        super.creatTaskForTest();
    }
}