import manager.Managers;
import manager.TaskManager;
import model.Epic;
import model.SubTask;
import model.Task;
import model.TaskStatus;

import java.io.File;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class JavaKanban {
    public static void main(String[] args) {
        File file = new File("data.csv");
        TaskManager taskManager = Managers.getFileBackedTaskManager(file);

        //Создайте две задачи, эпик с тремя подзадачами и эпик без подзадач.
        Task task1 = new Task(
                "Приготовить кофе",
                "добавить сливки",
                TaskStatus.NEW,
                Duration.ofMinutes(10),
                LocalDateTime.of(LocalDate.now(), LocalTime.now())
        );
        
        Task task2 = new Task(
                "Приготовить кофе",
                "добавить сливки",
                TaskStatus.NEW,
                Duration.ofMinutes(10),
                LocalDateTime.of(LocalDate.now(), LocalTime.now().plus(Duration.ofHours(3)))
        );

        taskManager.addTask(task1);
        taskManager.addTask(task2);

        Epic epic1 = new Epic(
                "Уборка по дому",
                "произвести уборку по всему дому"
        );
        Epic epic2 = new Epic(
                "Хомяк",
                "покормить хомяка"
        );

        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);

        SubTask subTask1 = new SubTask(
                epic1,
                "Пропылесосить комнаты",
                "тщательно",
                TaskStatus.NEW,
                Duration.ofMinutes(15),
                LocalDateTime.of(LocalDate.now(), LocalTime.now().plus(Duration.ofMinutes(10)))
        );
        SubTask subTask2 = new SubTask(
                epic1,
                "Помыть полы",
                "мыть с чистящим средством",
                TaskStatus.NEW,
                Duration.ofMinutes(15),
                LocalDateTime.of(LocalDate.now(), LocalTime.now().plus(Duration.ofMinutes(60)))
        );

        SubTask subTask3 = new SubTask(
                epic1,
                "Разобрать посудомойку",
                "протереть посуду",
                TaskStatus.NEW,
                Duration.ofMinutes(15),
                LocalDateTime.of(LocalDate.now(), LocalTime.now().plus(Duration.ofMinutes(120)))
        );

        taskManager.addSubTask(subTask1);
        taskManager.addSubTask(subTask2);
        taskManager.addSubTask(subTask3);

        System.out.println("taskManager:");
        printAllTasks(taskManager);

        //Создайте новый FileBackedTaskManager-менеджер из этого же файла.
        TaskManager taskManagerFromFile = Managers.loadFromFile(file);
        System.out.println("\ntaskManagerFromFile:");
        printAllTasks(taskManagerFromFile);

        printAllTasks(taskManager);
    }

    public static void printAllTasks(TaskManager manager) {
        System.out.println("Задачи:");
        manager.getAllTasks().forEach(System.out::println);

        System.out.println("\nЭпики:");
        manager.getAllEpics().forEach(System.out::println);

        System.out.println("\nПодзадачи:");
        manager.getAllSubTasks().forEach(System.out::println);
    }
}
