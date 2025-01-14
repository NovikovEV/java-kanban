package manager;

import model.Epic;
import model.SubTask;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InMemoryTaskManagerTest {
    private TaskManager taskManager;
    private Task task1;
    private Task task2;
    private Epic epic1;
    private Epic epic2;

    @BeforeEach
    void setUp() {
        task1 = new Task(
                "Приготовить кофе",
                "добавить сливки",
                TaskStatus.NEW
        );
        task2 = new Task(
                "Купить хлеб",
                "половину буханки",
                TaskStatus.DONE
        );

        epic1 = new Epic(
                "Уборка по дому",
                "произвести уборку по всему дому"
        );
        epic2 = new Epic(
                "Хомяк",
                "покормить хомяка"
        );
        taskManager = Managers.getDefault();
    }

    @Test
    void testAddTask() {
        taskManager.addTask(task1);
        String expected = "[Task{id=1, taskName='Приготовить кофе', description='добавить сливки', taskStatus=NEW}]";
        String actually = taskManager.getAllTasks().toString();
        Assertions.assertEquals(expected, actually);
    }

    @Test
    void testGetTaskById() {
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        String expected = "Task{id=1, taskName='Приготовить кофе', description='добавить сливки', taskStatus=NEW}";
        String actually = taskManager.getTaskById(1).toString();
        Assertions.assertEquals(expected, actually);
    }

    @Test
    void testGetAllTasks() {
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        String expected = "[Task{id=1, taskName='Приготовить кофе', description='добавить сливки', taskStatus=NEW}, Task{id=2, taskName='Купить хлеб', description='половину буханки', taskStatus=DONE}]";
        String actually = taskManager.getAllTasks().toString();
        Assertions.assertEquals(expected, actually);
    }

    @Test
    void testUpdateTask() {
        taskManager.addTask(task1);
        String expected = "Task{id=1, taskName='Купить хлеб', description='половину буханки', taskStatus=DONE}";
        Task task = new Task(1, "Купить хлеб", "половину буханки", TaskStatus.DONE);
        taskManager.updateTask(task);
        String actually = taskManager.getTaskById(1).toString();
        Assertions.assertEquals(expected, actually);
    }

    @Test
    void testDeleteTaskById() {
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.deleteTaskById(2);
        String expected = "[Task{id=1, taskName='Приготовить кофе', description='добавить сливки', taskStatus=NEW}]";
        String actually = taskManager.getAllTasks().toString();
        Assertions.assertEquals(expected, actually);
    }

    @Test
    void testRemoveAllTasks() {
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.removeAllTasks();

        Assertions.assertTrue(taskManager.getAllTasks().isEmpty());
    }

    @Test
    void testAddEpic() {
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);
        String expected = "[Epic{id=1, name=Уборка по дому, subTasksIdList=[], status=NEW}, Epic{id=2, name=Хомяк, subTasksIdList=[], status=NEW}]";
        String actually = taskManager.getAllEpics().toString();
        Assertions.assertEquals(expected, actually);
    }

    @Test
    void testGetEpicById() {
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);
        String expected = "Epic{id=2, name=Хомяк, subTasksIdList=[], status=NEW}";
        String actually = taskManager.getEpicById(2).toString();
        Assertions.assertEquals(expected, actually);
    }

    @Test
    void testGetAllEpics() {
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);
        String expected = "[Epic{id=1, name=Уборка по дому, subTasksIdList=[], status=NEW}, Epic{id=2, name=Хомяк, subTasksIdList=[], status=NEW}]";
        String actually = taskManager.getAllEpics().toString();
        Assertions.assertEquals(expected, actually);
    }

    @Test
    void testUpdateEpic() {
        taskManager.addEpic(epic1);
        Epic epic = taskManager.getEpicById(1);
        epic.setTaskName("Новое имя");
        taskManager.updateEpic(epic);
        String expected = "[Epic{id=1, name=Новое имя, subTasksIdList=[], status=NEW}]";
        String actually = taskManager.getAllEpics().toString();
        Assertions.assertEquals(expected, actually);
    }

    @Test
    void testDeleteEpicById() {
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);
        taskManager.deleteEpicById(2);
        String expected = "[Epic{id=1, name=Уборка по дому, subTasksIdList=[], status=NEW}]";
        String actually = taskManager.getAllEpics().toString();
        Assertions.assertEquals(expected, actually);
    }

    @Test
    void testRemoveAllEpics() {
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);
        taskManager.removeAllEpics();

        Assertions.assertTrue(taskManager.getAllEpics().isEmpty());
    }

    @Test
    void testAddSubTask() {
        taskManager.addEpic(epic1);
        SubTask subTask1 = new SubTask(
                epic1,
                "Пропылесосить комнаты",
                "тщательно",
                TaskStatus.NEW
        );
        taskManager.addSubTask(subTask1);
        String expected = "[SubTask{id=2, epicId=1, name=Пропылесосить комнаты, status=NEW}]";
        String actually = taskManager.getAllSubTasks().toString();
        Assertions.assertEquals(expected, actually);
    }

    @Test
    void testGetSubTaskById() {
        taskManager.addEpic(epic1);
        SubTask subTask1 = new SubTask(
                epic1,
                "Пропылесосить комнаты",
                "тщательно",
                TaskStatus.NEW
        );
        taskManager.addSubTask(subTask1);
        String expected = "SubTask{id=2, epicId=1, name=Пропылесосить комнаты, status=NEW}";
        String actually = taskManager.getSubTaskById(2).toString();
        Assertions.assertEquals(expected, actually);
    }

    @Test
    void testGetAllSubTasks() {
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);

        SubTask subTask1 = new SubTask(
                epic1,
                "Пропылесосить комнаты",
                "тщательно",
                TaskStatus.NEW
        );
        SubTask subTask2 = new SubTask(
                epic1,
                "Помыть полы",
                "мыть с чистящим средством",
                TaskStatus.NEW
        );

        SubTask subTask3 = new SubTask(
                epic2,
                "Дать ему что нибудь",
                "Морковь, кабачки, свекла, цветная капуста",
                TaskStatus.NEW
        );
        taskManager.addSubTask(subTask1);
        taskManager.addSubTask(subTask2);
        taskManager.addSubTask(subTask3);

        String expected = "[SubTask{id=3, epicId=1, name=Пропылесосить комнаты, status=NEW}, SubTask{id=4, epicId=1, name=Помыть полы, status=NEW}, SubTask{id=5, epicId=2, name=Дать ему что нибудь, status=NEW}]";
        String actually = taskManager.getAllSubTasks().toString();
        Assertions.assertEquals(expected, actually);
    }

    @Test
    void testUpdateSubTask() {
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);

        SubTask subTask1 = new SubTask(
                epic1,
                "Пропылесосить комнаты",
                "тщательно",
                TaskStatus.NEW
        );
        SubTask subTask2 = new SubTask(
                epic1,
                "Помыть полы",
                "мыть с чистящим средством",
                TaskStatus.NEW
        );

        SubTask subTask3 = new SubTask(
                epic2,
                "Дать ему что нибудь",
                "Морковь, кабачки, свекла, цветная капуста",
                TaskStatus.NEW
        );

        taskManager.addSubTask(subTask1);
        taskManager.addSubTask(subTask2);
        taskManager.addSubTask(subTask3);

        subTask1 = taskManager.getSubTaskById(3);
        subTask1.setTaskName("Обновленная задача");
        taskManager.updateSubTask(subTask1);

        String expected = "[SubTask{id=3, epicId=1, name=Обновленная задача, status=NEW}, SubTask{id=4, epicId=1, name=Помыть полы, status=NEW}, SubTask{id=5, epicId=2, name=Дать ему что нибудь, status=NEW}]";
        String actually = taskManager.getAllSubTasks().toString();
        Assertions.assertEquals(expected, actually);
    }

    @Test
    void testDeleteSubTaskById() {
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);

        SubTask subTask1 = new SubTask(
                epic1,
                "Пропылесосить комнаты",
                "тщательно",
                TaskStatus.NEW
        );
        SubTask subTask2 = new SubTask(
                epic1,
                "Помыть полы",
                "мыть с чистящим средством",
                TaskStatus.NEW
        );

        SubTask subTask3 = new SubTask(
                epic2,
                "Дать ему что нибудь",
                "Морковь, кабачки, свекла, цветная капуста",
                TaskStatus.NEW
        );

        taskManager.addSubTask(subTask1);
        taskManager.addSubTask(subTask2);
        taskManager.addSubTask(subTask3);

        taskManager.deleteSubTaskById(3);

        String expected = "[SubTask{id=4, epicId=1, name=Помыть полы, status=NEW}, SubTask{id=5, epicId=2, name=Дать ему что нибудь, status=NEW}]";
        String actually = taskManager.getAllSubTasks().toString();
        Assertions.assertEquals(expected, actually);
    }

    @Test
    void testRemoveAllSubtasks() {
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);

        SubTask subTask1 = new SubTask(
                epic1,
                "Пропылесосить комнаты",
                "тщательно",
                TaskStatus.NEW
        );
        SubTask subTask2 = new SubTask(
                epic1,
                "Помыть полы",
                "мыть с чистящим средством",
                TaskStatus.NEW
        );

        SubTask subTask3 = new SubTask(
                epic2,
                "Дать ему что нибудь",
                "Морковь, кабачки, свекла, цветная капуста",
                TaskStatus.NEW
        );

        taskManager.addSubTask(subTask1);
        taskManager.addSubTask(subTask2);
        taskManager.addSubTask(subTask3);

        taskManager.removeAllSubtasks();

        Assertions.assertTrue(taskManager.getAllSubTasks().isEmpty());
    }

    @Test
    void testGetHistory() {
        final int historySize = 6;
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);

        SubTask subTask1 = new SubTask(
                epic1,
                "Пропылесосить комнаты",
                "тщательно",
                TaskStatus.NEW
        );
        SubTask subTask2 = new SubTask(
                epic1,
                "Помыть полы",
                "мыть с чистящим средством",
                TaskStatus.NEW
        );

        SubTask subTask3 = new SubTask(
                epic2,
                "Дать ему что нибудь",
                "Морковь, кабачки, свекла, цветная капуста",
                TaskStatus.NEW
        );

        taskManager.addSubTask(subTask1);
        taskManager.addSubTask(subTask2);
        taskManager.addSubTask(subTask3);

        taskManager.getTaskById(1);
        taskManager.getTaskById(2);
        taskManager.getEpicById(3);
        taskManager.getEpicById(4);
        taskManager.getSubTaskById(5);
        taskManager.getSubTaskById(7);

        Assertions.assertEquals(historySize, taskManager.getHistory().size());
        String expected = "[Task{id=1, taskName='Приготовить кофе', description='добавить сливки', taskStatus=NEW}, Task{id=2, taskName='Купить хлеб', description='половину буханки', taskStatus=DONE}, Epic{id=3, name=Уборка по дому, subTasksIdList=[5, 6], status=NEW}, Epic{id=4, name=Хомяк, subTasksIdList=[7], status=NEW}, SubTask{id=5, epicId=3, name=Пропылесосить комнаты, status=NEW}, SubTask{id=7, epicId=4, name=Дать ему что нибудь, status=NEW}]";
        String actually = taskManager.getHistory().toString();
        Assertions.assertEquals(expected, actually);
    }

    @Test
    void testHistoryShouldNotExceedTenEentries() {
        final int historySize = 7;
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);

        SubTask subTask1 = new SubTask(
                epic1,
                "Пропылесосить комнаты",
                "тщательно",
                TaskStatus.NEW
        );
        SubTask subTask2 = new SubTask(
                epic1,
                "Помыть полы",
                "мыть с чистящим средством",
                TaskStatus.NEW
        );

        SubTask subTask3 = new SubTask(
                epic2,
                "Дать ему что нибудь",
                "Морковь, кабачки, свекла, цветная капуста",
                TaskStatus.NEW
        );

        taskManager.addSubTask(subTask1);
        taskManager.addSubTask(subTask2);
        taskManager.addSubTask(subTask3);

        taskManager.getTaskById(1);
        taskManager.getTaskById(2);
        taskManager.getEpicById(3);
        taskManager.getEpicById(4);
        taskManager.getSubTaskById(5);
        taskManager.getSubTaskById(7);
        taskManager.getEpicById(4);
        taskManager.getSubTaskById(5);
        taskManager.getSubTaskById(6);
        taskManager.getTaskById(1);
        taskManager.getSubTaskById(5);
        taskManager.getEpicById(4);

        Assertions.assertEquals(historySize, taskManager.getHistory().size());
        String expected = "[Task{id=2, taskName='Купить хлеб', description='половину буханки', taskStatus=DONE}, Epic{id=3, name=Уборка по дому, subTasksIdList=[5, 6], status=NEW}, SubTask{id=7, epicId=4, name=Дать ему что нибудь, status=NEW}, SubTask{id=6, epicId=3, name=Помыть полы, status=NEW}, Task{id=1, taskName='Приготовить кофе', description='добавить сливки', taskStatus=NEW}, SubTask{id=5, epicId=3, name=Пропылесосить комнаты, status=NEW}, Epic{id=4, name=Хомяк, subTasksIdList=[7], status=NEW}]";
        String actually = taskManager.getHistory().toString();
        Assertions.assertEquals(expected, actually);

    }
}