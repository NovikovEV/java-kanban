package manager;

import model.Epic;
import model.SubTask;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public abstract class TaskManagerTest<T extends TaskManager> {
    protected abstract T createManager();

    protected TaskManager taskManager;
    protected Task task1;
    protected Task task2;
    protected Epic epic1;
    protected Epic epic2;

    @BeforeEach
    void setUp() {
        task1 = new Task(
                "Приготовить кофе",
                "добавить сливки",
                TaskStatus.NEW,
                Duration.ofMinutes(15),
                LocalDateTime.of(LocalDate.of(2025, 2, 15), LocalTime.of(8, 0))
        );
        task2 = new Task(
                "Купить хлеб",
                "половину буханки",
                TaskStatus.DONE,
                Duration.ofMinutes(15),
                LocalDateTime.of(LocalDate.of(2025, 2, 15), LocalTime.of(9, 0))
        );

        epic1 = new Epic(
                "Уборка по дому",
                "произвести уборку по всему дому",
                Duration.ofMinutes(15),
                LocalDateTime.of(LocalDate.of(2025, 2, 15), LocalTime.of(10, 0))
        );
        epic2 = new Epic(
                "Хомяк",
                "покормить хомяка",
                Duration.ofMinutes(15),
                LocalDateTime.of(LocalDate.of(2025, 2, 15), LocalTime.of(11, 0))
        );
        taskManager = Managers.getDefault();
    }

    @Test
    void testAddTask() {
        taskManager.addTask(task1);
        String expected = "[Task{id=1, taskName='Приготовить кофе', description='добавить сливки', taskStatus=NEW, duration=15, startTime=08:00:00/15.02.2025, endTime=08:15:00/15.02.2025}]";
        String actually = taskManager.getAllTasks().toString();
        Assertions.assertEquals(expected, actually);
    }

    @Test
    void testGetTaskById() {
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        String expected = "Optional[Task{id=1, taskName='Приготовить кофе', description='добавить сливки', taskStatus=NEW, duration=15, startTime=08:00:00/15.02.2025, endTime=08:15:00/15.02.2025}]";
        String actually = taskManager.getTaskById(1).toString();
        Assertions.assertEquals(expected, actually);
    }

    @Test
    void testGetAllTasks() {
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        String expected = "[Task{id=1, taskName='Приготовить кофе', description='добавить сливки', taskStatus=NEW, duration=15, startTime=08:00:00/15.02.2025, endTime=08:15:00/15.02.2025}, Task{id=2, taskName='Купить хлеб', description='половину буханки', taskStatus=DONE, duration=15, startTime=09:00:00/15.02.2025, endTime=09:15:00/15.02.2025}]";
        String actually = taskManager.getAllTasks().toString();
        Assertions.assertEquals(expected, actually);
    }

    @Test
    void testUpdateTask() {
        taskManager.addTask(task1);
        String expected = "Optional[Task{id=1, taskName='Купить хлеб', description='половину буханки', taskStatus=DONE, duration=15, startTime=09:00:00/15.02.2025, endTime=09:15:00/15.02.2025}]";
        Task task = new Task(
                1,
                "Купить хлеб",
                "половину буханки",
                TaskStatus.DONE,
                Duration.ofMinutes(15),
                LocalDateTime.of(LocalDate.of(2025, 2, 15), LocalTime.of(9, 0)));
        taskManager.updateTask(task);
        String actually = taskManager.getTaskById(1).toString();
        Assertions.assertEquals(expected, actually);
    }

    @Test
    void testDeleteTaskById() {
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.deleteTaskById(2);
        String expected = "[Task{id=1, taskName='Приготовить кофе', description='добавить сливки', taskStatus=NEW, duration=15, startTime=08:00:00/15.02.2025, endTime=08:15:00/15.02.2025}]";
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
        String expected = "[Epic{id=1, name=Уборка по дому, subTasksIdList=[], status=NEW, duration=15, startTime=10:00:00/15.02.2025, endTime=10:15:00/15.02.2025}, Epic{id=2, name=Хомяк, subTasksIdList=[], status=NEW, duration=15, startTime=11:00:00/15.02.2025, endTime=11:15:00/15.02.2025}]";
        String actually = taskManager.getAllEpics().toString();
        Assertions.assertEquals(expected, actually);
    }

    @Test
    void testGetEpicById() {
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);
        String expected = "Optional[Epic{id=2, name=Хомяк, subTasksIdList=[], status=NEW, duration=15, startTime=11:00:00/15.02.2025, endTime=11:15:00/15.02.2025}]";
        String actually = taskManager.getEpicById(2).toString();
        Assertions.assertEquals(expected, actually);
    }

    @Test
    void testGetAllEpics() {
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);
        String expected = "[Epic{id=1, name=Уборка по дому, subTasksIdList=[], status=NEW, duration=15, startTime=10:00:00/15.02.2025, endTime=10:15:00/15.02.2025}, Epic{id=2, name=Хомяк, subTasksIdList=[], status=NEW, duration=15, startTime=11:00:00/15.02.2025, endTime=11:15:00/15.02.2025}]";
        String actually = taskManager.getAllEpics().toString();
        Assertions.assertEquals(expected, actually);
    }

    @Test
    void testUpdateEpic() {
        taskManager.addEpic(epic1);
        Epic epic = taskManager.getEpicById(1).orElseThrow(RuntimeException::new);
        epic.setTaskName("Новое имя");
        taskManager.updateEpic(epic);
        String expected = "[Epic{id=1, name=Новое имя, subTasksIdList=[], status=NEW, duration=15, startTime=10:00:00/15.02.2025, endTime=10:15:00/15.02.2025}]";
        String actually = taskManager.getAllEpics().toString();
        Assertions.assertEquals(expected, actually);
    }

    @Test
    void testDeleteEpicById() {
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);
        taskManager.deleteEpicById(2);
        String expected = "[Epic{id=1, name=Уборка по дому, subTasksIdList=[], status=NEW, duration=15, startTime=10:00:00/15.02.2025, endTime=10:15:00/15.02.2025}]";
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
                TaskStatus.NEW,
                Duration.ofMinutes(15),
                LocalDateTime.of(LocalDate.of(2025, 2, 15), LocalTime.of(16,0))
        );
        taskManager.addSubTask(subTask1);
        String expected = "[SubTask{id=2, epicId=1, name=Пропылесосить комнаты, status=NEW, duration=15, startTime=16:00:00/15.02.2025, endTime=16:15:00/15.02.2025}]";
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
                TaskStatus.NEW,
                Duration.ofMinutes(15),
                LocalDateTime.of(LocalDate.of(2025, 2, 15), LocalTime.of(14,0))
        );
        taskManager.addSubTask(subTask1);
        String expected = "Optional[SubTask{id=2, epicId=1, name=Пропылесосить комнаты, status=NEW, duration=15, startTime=14:00:00/15.02.2025, endTime=14:15:00/15.02.2025}]";
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
                TaskStatus.NEW,
                Duration.ofMinutes(15),
                LocalDateTime.of(LocalDate.of(2025, 2, 15), LocalTime.of(12,0))
        );
        SubTask subTask2 = new SubTask(
                epic1,
                "Помыть полы",
                "мыть с чистящим средством",
                TaskStatus.NEW,
                Duration.ofMinutes(15),
                LocalDateTime.of(LocalDate.of(2025, 2, 15), LocalTime.of(13,0))
        );

        SubTask subTask3 = new SubTask(
                epic2,
                "Дать ему что нибудь",
                "Морковь, кабачки, свекла, цветная капуста",
                TaskStatus.NEW,
                Duration.ofMinutes(15),
                LocalDateTime.of(LocalDate.of(2025, 2, 15), LocalTime.of(14,0))
        );
        taskManager.addSubTask(subTask1);
        taskManager.addSubTask(subTask2);
        taskManager.addSubTask(subTask3);

        String expected = "[SubTask{id=3, epicId=1, name=Пропылесосить комнаты, status=NEW, duration=15, startTime=12:00:00/15.02.2025, endTime=12:15:00/15.02.2025}, SubTask{id=4, epicId=1, name=Помыть полы, status=NEW, duration=15, startTime=13:00:00/15.02.2025, endTime=13:15:00/15.02.2025}, SubTask{id=5, epicId=2, name=Дать ему что нибудь, status=NEW, duration=15, startTime=14:00:00/15.02.2025, endTime=14:15:00/15.02.2025}]";
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
                TaskStatus.NEW,
                Duration.ofMinutes(15),
                LocalDateTime.of(LocalDate.of(2025, 2, 15), LocalTime.of(16, 0))
        );
        SubTask subTask2 = new SubTask(
                epic1,
                "Помыть полы",
                "мыть с чистящим средством",
                TaskStatus.NEW,
                Duration.ofMinutes(15),
                LocalDateTime.of(LocalDate.of(2025, 2, 15), LocalTime.of(17, 0))
        );

        SubTask subTask3 = new SubTask(
                epic2,
                "Дать ему что нибудь",
                "Морковь, кабачки, свекла, цветная капуста",
                TaskStatus.NEW,
                Duration.ofMinutes(15),
                LocalDateTime.of(LocalDate.of(2025, 2, 15), LocalTime.of(18, 0))
        );

        taskManager.addSubTask(subTask1);
        taskManager.addSubTask(subTask2);
        taskManager.addSubTask(subTask3);

        subTask1 = taskManager.getSubTaskById(3).orElseThrow(RuntimeException::new);
        subTask1.setTaskName("Обновленная задача");
        taskManager.updateSubTask(subTask1);

        String expected = "[SubTask{id=3, epicId=1, name=Обновленная задача, status=NEW, duration=15, startTime=16:00:00/15.02.2025, endTime=16:15:00/15.02.2025}, SubTask{id=4, epicId=1, name=Помыть полы, status=NEW, duration=15, startTime=17:00:00/15.02.2025, endTime=17:15:00/15.02.2025}, SubTask{id=5, epicId=2, name=Дать ему что нибудь, status=NEW, duration=15, startTime=18:00:00/15.02.2025, endTime=18:15:00/15.02.2025}]";
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
                TaskStatus.NEW,
                Duration.ofMinutes(15),
                LocalDateTime.of(LocalDate.of(2025, 2, 15), LocalTime.of(18,30))
        );
        SubTask subTask2 = new SubTask(
                epic1,
                "Помыть полы",
                "мыть с чистящим средством",
                TaskStatus.NEW,
                Duration.ofMinutes(15),
                LocalDateTime.of(LocalDate.of(2025, 2, 15), LocalTime.of(19,0))
        );

        SubTask subTask3 = new SubTask(
                epic2,
                "Дать ему что нибудь",
                "Морковь, кабачки, свекла, цветная капуста",
                TaskStatus.NEW,
                Duration.ofMinutes(15),
                LocalDateTime.of(LocalDate.of(2025, 2, 15), LocalTime.of(20,0))
        );

        taskManager.addSubTask(subTask1);
        taskManager.addSubTask(subTask2);
        taskManager.addSubTask(subTask3);

        taskManager.deleteSubTaskById(3);

        String expected = "[SubTask{id=4, epicId=1, name=Помыть полы, status=NEW, duration=15, startTime=19:00:00/15.02.2025, endTime=19:15:00/15.02.2025}, SubTask{id=5, epicId=2, name=Дать ему что нибудь, status=NEW, duration=15, startTime=20:00:00/15.02.2025, endTime=20:15:00/15.02.2025}]";
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
                TaskStatus.NEW,
                Duration.ofMinutes(15),
                LocalDateTime.of(LocalDate.now(), LocalTime.of(8, 0))
        );
        SubTask subTask2 = new SubTask(
                epic1,
                "Помыть полы",
                "мыть с чистящим средством",
                TaskStatus.NEW,
                Duration.ofMinutes(15),
                LocalDateTime.of(LocalDate.now(), LocalTime.of(10, 0))
        );

        SubTask subTask3 = new SubTask(
                epic2,
                "Дать ему что нибудь",
                "Морковь, кабачки, свекла, цветная капуста",
                TaskStatus.NEW,
                Duration.ofMinutes(15),
                LocalDateTime.of(LocalDate.now(), LocalTime.of(15, 0))
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
                TaskStatus.NEW,
                Duration.ofMinutes(15),
                LocalDateTime.of(LocalDate.of(2025, 2, 15), LocalTime.of(21,0))
        );
        SubTask subTask2 = new SubTask(
                epic1,
                "Помыть полы",
                "мыть с чистящим средством",
                TaskStatus.NEW,
                Duration.ofMinutes(15),
                LocalDateTime.of(LocalDate.of(2025, 2, 15), LocalTime.of(22,0))
        );

        SubTask subTask3 = new SubTask(
                epic2,
                "Дать ему что нибудь",
                "Морковь, кабачки, свекла, цветная капуста",
                TaskStatus.NEW,
                Duration.ofMinutes(15),
                LocalDateTime.of(LocalDate.of(2025, 2, 15), LocalTime.of(10,0))
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
        String expected = "[Task{id=1, taskName='Приготовить кофе', description='добавить сливки', taskStatus=NEW, duration=15, startTime=08:00:00/15.02.2025, endTime=08:15:00/15.02.2025}, Task{id=2, taskName='Купить хлеб', description='половину буханки', taskStatus=DONE, duration=15, startTime=09:00:00/15.02.2025, endTime=09:15:00/15.02.2025}, Epic{id=3, name=Уборка по дому, subTasksIdList=[5, 6], status=NEW, duration=30, startTime=21:00:00/15.02.2025, endTime=21:30:00/15.02.2025}, Epic{id=4, name=Хомяк, subTasksIdList=[7], status=NEW, duration=15, startTime=10:00:00/15.02.2025, endTime=10:15:00/15.02.2025}, SubTask{id=5, epicId=3, name=Пропылесосить комнаты, status=NEW, duration=15, startTime=21:00:00/15.02.2025, endTime=21:15:00/15.02.2025}, SubTask{id=7, epicId=4, name=Дать ему что нибудь, status=NEW, duration=15, startTime=10:00:00/15.02.2025, endTime=10:15:00/15.02.2025}]";
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
                TaskStatus.NEW,
                Duration.ofMinutes(15),
                LocalDateTime.of(LocalDate.of(2025, 2, 15), LocalTime.of(13,0))
        );
        SubTask subTask2 = new SubTask(
                epic1,
                "Помыть полы",
                "мыть с чистящим средством",
                TaskStatus.NEW,
                Duration.ofMinutes(15),
                LocalDateTime.of(LocalDate.of(2025, 2, 15), LocalTime.of(14,0))
        );

        SubTask subTask3 = new SubTask(
                epic2,
                "Дать ему что нибудь",
                "Морковь, кабачки, свекла, цветная капуста",
                TaskStatus.NEW,
                Duration.ofMinutes(15),
                LocalDateTime.of(LocalDate.of(2025, 2, 15), LocalTime.of(15,0))
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
        String expected = "[Task{id=2, taskName='Купить хлеб', description='половину буханки', taskStatus=DONE, duration=15, startTime=09:00:00/15.02.2025, endTime=09:15:00/15.02.2025}, Epic{id=3, name=Уборка по дому, subTasksIdList=[5, 6], status=NEW, duration=30, startTime=13:00:00/15.02.2025, endTime=13:30:00/15.02.2025}, SubTask{id=7, epicId=4, name=Дать ему что нибудь, status=NEW, duration=15, startTime=15:00:00/15.02.2025, endTime=15:15:00/15.02.2025}, SubTask{id=6, epicId=3, name=Помыть полы, status=NEW, duration=15, startTime=14:00:00/15.02.2025, endTime=14:15:00/15.02.2025}, Task{id=1, taskName='Приготовить кофе', description='добавить сливки', taskStatus=NEW, duration=15, startTime=08:00:00/15.02.2025, endTime=08:15:00/15.02.2025}, SubTask{id=5, epicId=3, name=Пропылесосить комнаты, status=NEW, duration=15, startTime=13:00:00/15.02.2025, endTime=13:15:00/15.02.2025}, Epic{id=4, name=Хомяк, subTasksIdList=[7], status=NEW, duration=15, startTime=15:00:00/15.02.2025, endTime=15:15:00/15.02.2025}]";
        String actually = taskManager.getHistory().toString();
        Assertions.assertEquals(expected, actually);
    }

    @Test
    void TestPrioritizedTasks() {
        taskManager.addTask(task2);
        taskManager.addTask(task1);
        String expected = "[Task{id=2, taskName='Приготовить кофе', description='добавить сливки', taskStatus=NEW, duration=15, startTime=08:00:00/15.02.2025, endTime=08:15:00/15.02.2025}, Task{id=1, taskName='Купить хлеб', description='половину буханки', taskStatus=DONE, duration=15, startTime=09:00:00/15.02.2025, endTime=09:15:00/15.02.2025}]";
        String actually = taskManager.getPrioritizedTasks().toString();
        Assertions.assertEquals(expected, actually);
    }
}
