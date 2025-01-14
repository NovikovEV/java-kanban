package manager;

import model.Epic;
import model.SubTask;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InMemoryHistoryManagerTest {
    private HistoryManager historyManager;
    private Task task1;
    private Task task2;
    private Epic epic1;
    private Epic epic2;
    private SubTask subTask1;
    private SubTask subTask2;
    private SubTask subTask3;

    @BeforeEach
    void SetUp() {
        historyManager = Managers.getDefaultHistoryManager();

        task1 = new Task(
                1,
                "Приготовить кофе",
                "добавить сливки",
                TaskStatus.NEW
        );

        task2 = new Task(
                2,
                "Купить хлеб",
                "половину буханки",
                TaskStatus.DONE
        );

        epic1 = new Epic(
                3,
                "Уборка по дому",
                "произвести уборку по всему дому",
                TaskStatus.NEW
        );
        epic2 = new Epic(
                4,
                "Хомяк",
                "покормить хомяка",
                TaskStatus.NEW
        );

        subTask1 = new SubTask(
                5,
                3,
                "Пропылесосить комнаты",
                "тщательно",
                TaskStatus.NEW
        );
        subTask2 = new SubTask(
                6,
                3,
                "Помыть полы",
                "мыть с чистящим средством",
                TaskStatus.NEW
        );

        subTask3 = new SubTask(
                7,
                4,
                "Дать ему что нибудь",
                "Морковь, кабачки, свекла, цветная капуста",
                TaskStatus.NEW
        );
    }

    @Test
    void testAddTask() {
        historyManager.add(task1);
        historyManager.add(epic1);
        historyManager.add(task2);
        historyManager.add(epic2);
        historyManager.add(subTask1);
        historyManager.add(subTask2);
        historyManager.add(subTask3);

        String expected = "[Task{id=1, taskName='Приготовить кофе', description='добавить сливки', taskStatus=NEW}, Epic{id=3, name=Уборка по дому, subTasksIdList=[], status=NEW}, Task{id=2, taskName='Купить хлеб', description='половину буханки', taskStatus=DONE}, Epic{id=4, name=Хомяк, subTasksIdList=[], status=NEW}, SubTask{id=5, epicId=3, name=Пропылесосить комнаты, status=NEW}, SubTask{id=6, epicId=3, name=Помыть полы, status=NEW}, SubTask{id=7, epicId=4, name=Дать ему что нибудь, status=NEW}]";
        String actually = historyManager.getHistory().toString();
        Assertions.assertEquals(expected, actually);
    }

    @Test
    void removeTaskById(){
        historyManager.add(task1);
        historyManager.add(epic1);
        historyManager.add(task2);
        historyManager.add(epic2);
        historyManager.add(subTask1);
        historyManager.add(subTask2);
        historyManager.add(subTask3);
        historyManager.remove(3);
        historyManager.remove(1);

        String expected = "[Task{id=2, taskName='Купить хлеб', description='половину буханки', taskStatus=DONE}, Epic{id=4, name=Хомяк, subTasksIdList=[], status=NEW}, SubTask{id=5, epicId=3, name=Пропылесосить комнаты, status=NEW}, SubTask{id=6, epicId=3, name=Помыть полы, status=NEW}, SubTask{id=7, epicId=4, name=Дать ему что нибудь, status=NEW}]";
        String actually = historyManager.getHistory().toString();
        Assertions.assertEquals(expected, actually);
    }

    @Test
    void testGetHistory() {
        historyManager.add(task1);
        historyManager.add(epic1);
        historyManager.add(task2);
        historyManager.add(epic2);
        historyManager.add(subTask2);
        historyManager.add(subTask1);
        historyManager.add(subTask3);

        String expected = "[Task{id=1, taskName='Приготовить кофе', description='добавить сливки', taskStatus=NEW}, Epic{id=3, name=Уборка по дому, subTasksIdList=[], status=NEW}, Task{id=2, taskName='Купить хлеб', description='половину буханки', taskStatus=DONE}, Epic{id=4, name=Хомяк, subTasksIdList=[], status=NEW}, SubTask{id=6, epicId=3, name=Помыть полы, status=NEW}, SubTask{id=5, epicId=3, name=Пропылесосить комнаты, status=NEW}, SubTask{id=7, epicId=4, name=Дать ему что нибудь, status=NEW}]";
        String actually = historyManager.getHistory().toString();
        Assertions.assertEquals(expected, actually);
    }

    @Test
    void thereShouldntBeNullInHistory() {
        Task task = null;
        Epic epic = null;
        SubTask subTask = null;

        historyManager.add(task);
        historyManager.add(epic);
        historyManager.add(subTask);

        Assertions.assertTrue(historyManager.getHistory().isEmpty());
    }

    @Test
    void sizeOfStoriesShouldNotExceedSevenElements() {
        final int historySize = 7;

        historyManager.add(task1);
        historyManager.add(epic1);
        historyManager.add(task2);
        historyManager.add(epic2);
        historyManager.add(subTask2);
        historyManager.add(subTask1);
        historyManager.add(subTask3);
        historyManager.add(subTask3);
        historyManager.add(subTask1);
        historyManager.add(epic2);

        Assertions.assertEquals(historySize, historyManager.getHistory().size());

        historyManager.add(task1);
        historyManager.add(epic1);
        historyManager.add(task2);
        historyManager.add(epic2);

        Assertions.assertEquals(historySize, historyManager.getHistory().size());
    }
}