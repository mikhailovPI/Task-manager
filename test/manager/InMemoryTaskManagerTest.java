package manager;

import org.junit.jupiter.api.BeforeEach;

class InMemoryTaskManagerTest extends ManagerTest<InMemoryTaskManager> {


    @Override
    @BeforeEach
    void creatTaskForTest() {
        taskManager = new InMemoryTaskManager();
        super.creatTaskForTest();
    }
}