# Трекер задач

**Данная программа может**

Программа хранит в себе задачи/подзадачи/эпики.
Каждая задача имеет:
1. Название;
2. Краткое описание;
3. Уникальный идентификационный номер;
4. Статус (New, In_progress, Done);
5. Подзадачи имеют индекс эпика, которому они принадлежат;
6. Продолжительность выполнения;
7. Ориентировочное время начала выполнения;

Программа имеет следующие функции:
1. Создать задачу/подзадачу/эпик;
2. Обновить задачу/подзадачу/эпик;
3. Получить список всех задач/подзадач/эпиков;
4. Получить задачу/подзадачу/эпик по идентификатору;
5. Удалить все задачи/подзадачи/эпики;
6. Удалить задачу/подзадачу/эпик по идентификатору;
7. Получить список всех подзадач эпика;
8. Посмотреть историю просмотра задач;
9. Запись (чтение) задач/подзадач/эпиков в (из) файл(а);
10. Сортировка списка задач по времени;

Для хранения истории просмотров задач реализованы методы, позволяющие реализовать алгоритмическую сложность О(1).
Это достигается реализацией двусвязанного списка и HashMap.

Доступ к методам осуществляется с помощью HTTP-запросов и хранит свое состояние на отдельном сервере.

Программа написана на Java. Пример кода:

```java
package manager;

import manager.history.InMemoryHistoryManager;
import manager.interfaceClass.HistoryManager;
import manager.interfaceClass.TaskManager;
import manager.taskManager.InMemoryTaskManager;

public class Managers {

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getHistoryDefault() {
        return new InMemoryHistoryManager();
    }
}
....
//Получение задачи по индексу
public Task getTask(long id) {
   Task task = userTasks.get(id);
    if (task != null) {
        inMemoryHistoryManager.add(task);
    }
    return task;
}
```
