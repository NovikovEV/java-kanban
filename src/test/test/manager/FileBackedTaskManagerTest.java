package manager;

import model.Epic;
import model.SubTask;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

class FileBackedTaskManagerTest {
    private Path filePath;
    private Task task1;
    private Task task2;
    private Epic epic1;
    private Epic epic2;
    private SubTask subTask1;
    private SubTask subTask2;
    private SubTask subTask3;
    private TaskManager taskManager;

    @BeforeEach
    void setUp() {
        try {
            filePath = Files.createTempFile("data-", ".csv");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        taskManager = Managers.getFileBackedTaskManager(filePath.toFile());

        task1 = new Task(
                "Приготовить кофе",
                "добавить сливки",
                TaskStatus.NEW,
                Duration.ofMinutes(15),
                LocalDateTime.of(LocalDate.now(), LocalTime.of(8,0))
        );
        task2 = new Task(
                "Купить хлеб",
                "половину буханки",
                TaskStatus.DONE,
                Duration.ofMinutes(15),
                LocalDateTime.of(LocalDate.now(), LocalTime.of(9,0))
        );

        epic1 = new Epic(
                "Уборка по дому",
                "произвести уборку по всему дому",
                Duration.ofMinutes(15),
                LocalDateTime.of(LocalDate.now(), LocalTime.of(10,0))
        );
        epic2 = new Epic(
                "Хомяк",
                "покормить хомяка",
                Duration.ofMinutes(15),
                LocalDateTime.of(LocalDate.now(), LocalTime.of(11,0))
        );

        subTask1 = new SubTask(
                5,
                3,
                "Пропылесосить комнаты",
                "тщательно",
                TaskStatus.NEW,
                Duration.ofMinutes(15),
                LocalDateTime.of(LocalDate.now(), LocalTime.of(12,0))
        );
        subTask2 = new SubTask(
                6,
                3,
                "Помыть полы",
                "мыть с чистящим средством",
                TaskStatus.NEW,
                Duration.ofMinutes(15),
                LocalDateTime.of(LocalDate.now(), LocalTime.of(13,0))
        );
        subTask3 = new SubTask(
                7,
                3,
                "Разобрать посудомойку",
                "протереть посуду",
                TaskStatus.NEW,
                Duration.ofMinutes(15),
                LocalDateTime.of(LocalDate.now(), LocalTime.of(14,0))
        );

    }

    @Test
    void testLoad() {
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);
        taskManager.addSubTask(subTask1);
        taskManager.addSubTask(subTask2);
        taskManager.addSubTask(subTask3);
        TaskManager loadedTaskManager = Managers.loadFromFile(filePath.toFile());
        String expected = taskManager.getAllTasks() + " " + taskManager.getAllEpics() + " " + taskManager.getAllSubTasks();
        String actually = loadedTaskManager.getAllTasks() + " " + loadedTaskManager.getAllEpics() + " " + taskManager.getAllSubTasks();
        Assertions.assertEquals(expected, actually);
    }

    @Test
    void testAddTask() {
        taskManager.addTask(task2);
        taskManager.addTask(task1);
        TaskManager loadedTaskManager = Managers.loadFromFile(filePath.toFile());
        String expected = taskManager.getAllTasks().toString();
        String actually = loadedTaskManager.getAllTasks().toString();
        Assertions.assertEquals(expected, actually);
    }

    @Test
    void testUpdateTask() {
        taskManager.addTask(task1);
        Task task = taskManager.getTaskById(1).orElseThrow(RuntimeException::new);
        task.setTaskName("Test task");
        taskManager.updateTask(task);
        TaskManager loadedTaskManager = Managers.loadFromFile(filePath.toFile());
        String expected = taskManager.getAllTasks().toString();
        String actually = loadedTaskManager.getAllTasks().toString();
        Assertions.assertEquals(expected, actually);
    }

    @Test
    void testDeleteTaskById() {
        taskManager.addTask(task1);
        taskManager.deleteTaskById(1);
        TaskManager loadedTaskManager = Managers.loadFromFile(filePath.toFile());
        String expected = taskManager.getAllTasks().toString();
        String actually = loadedTaskManager.getAllTasks().toString();
        Assertions.assertEquals(expected, actually);
    }

    @Test
    void testRemoveAllTasks() {
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.removeAllTasks();
        TaskManager loadedTaskManager = Managers.loadFromFile(filePath.toFile());
        String expected = taskManager.getAllTasks().toString();
        String actually = loadedTaskManager.getAllTasks().toString();
        Assertions.assertEquals(expected, actually);
    }

    @Test
    void testAddEpic() {
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);
        TaskManager loadedTaskManager = Managers.loadFromFile(filePath.toFile());
        String expected = taskManager.getAllEpics().toString();
        String actually = loadedTaskManager.getAllEpics().toString();
        Assertions.assertEquals(expected, actually);
    }

    @Test
    void testUpdateEpic() {
        taskManager.addEpic(epic2);
        Epic epic = taskManager.getEpicById(1).orElseThrow(RuntimeException::new);
        epic.setTaskName("Test name");
        taskManager.updateEpic(epic);
        TaskManager loadedTaskManager = Managers.loadFromFile(filePath.toFile());
        String expected = taskManager.getAllEpics().toString();
        String actually = loadedTaskManager.getAllEpics().toString();
        Assertions.assertEquals(expected, actually);
    }

    @Test
    void testDeleteEpicById() {
        taskManager.addEpic(epic1);
        taskManager.deleteEpicById(1);
        TaskManager loadedTaskManager = Managers.loadFromFile(filePath.toFile());
        String expected = taskManager.getAllEpics().toString();
        String actually = loadedTaskManager.getAllEpics().toString();
        Assertions.assertEquals(expected, actually);
    }

    @Test
    void testRemoveAllEpics() {
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);
        taskManager.removeAllEpics();
        TaskManager loadedTaskManager = Managers.loadFromFile(filePath.toFile());
        String expected = taskManager.getAllEpics().toString();
        String actually = loadedTaskManager.getAllEpics().toString();
        Assertions.assertEquals(expected, actually);
    }

    @Test
    void testAddSubTask() {
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);
        taskManager.addSubTask(subTask1);
        taskManager.addSubTask(subTask2);
        taskManager.addSubTask(subTask3);
        TaskManager loadedTaskManager = Managers.loadFromFile(filePath.toFile());
        String expected = taskManager.getAllSubTasks().toString();
        String actually = loadedTaskManager.getAllSubTasks().toString();
        Assertions.assertEquals(expected, actually);
    }

    @Test
    void testUpdateSubTask() {
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);
        taskManager.addSubTask(subTask1);
        taskManager.addSubTask(subTask2);
        taskManager.addSubTask(subTask3);
        SubTask subTask = taskManager.getSubTaskById(6).orElseThrow(RuntimeException::new);
        subTask.setTaskName("Test subTask");
        taskManager.updateSubTask(subTask);
        TaskManager loadedTaskManager = Managers.loadFromFile(filePath.toFile());
        String expected = taskManager.getAllSubTasks().toString();
        String actually = loadedTaskManager.getAllSubTasks().toString();
        Assertions.assertEquals(expected, actually);
    }

    @Test
    void testDeleteSubTaskById() {
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);
        taskManager.addSubTask(subTask1);
        taskManager.addSubTask(subTask2);
        taskManager.addSubTask(subTask3);
        taskManager.deleteSubTaskById(7);
        TaskManager loadedTaskManager = Managers.loadFromFile(filePath.toFile());
        String expected = taskManager.getAllSubTasks().toString();
        String actually = loadedTaskManager.getAllSubTasks().toString();
        Assertions.assertEquals(expected, actually);
    }

    @Test
    void testRemoveAllSubtasks() {
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);
        taskManager.addSubTask(subTask1);
        taskManager.addSubTask(subTask2);
        taskManager.addSubTask(subTask3);
        taskManager.removeAllSubtasks();
        TaskManager loadedTaskManager = Managers.loadFromFile(filePath.toFile());
        String expected = taskManager.getAllSubTasks().toString();
        String actually = loadedTaskManager.getAllSubTasks().toString();
        Assertions.assertEquals(expected, actually);
    }
}