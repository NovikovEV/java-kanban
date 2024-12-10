import manager.Managers;
import manager.TaskManager;
import model.Epic;
import model.SubTask;
import model.Task;
import model.TaskStatus;

public class JavaKanban {
    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();

        //Создать несколько задач разного типа.
        Task task1 = new Task(
                "Приготовить кофе",
                "добавить сливки",
                TaskStatus.NEW
        );
        Task task2 = new Task(
                "Купить хлеб",
                "половину буханки",
                TaskStatus.DONE
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

        //Вызвать разные методы интерфейса TaskManager и напечатать историю просмотров после каждого вызова.
        System.out.println("До вызова методов getById");
        printAllTasks(taskManager);
        System.out.println("\nПосле вызова методов getById");
        taskManager.getTaskById(1);
        taskManager.getTaskById(2);
        taskManager.getEpicById(3);
        taskManager.getEpicById(4);
        taskManager.getSubTaskById(5);
        taskManager.getSubTaskById(7);
        printAllTasks(taskManager);
        System.out.println("\nПосле вызова 10 методов getById");
        taskManager.getEpicById(4);
        taskManager.getSubTaskById(5);
        taskManager.getSubTaskById(6);
        taskManager.getTaskById(1);
        printAllTasks(taskManager);
        System.out.println("\nПосле вызова 11 методов getById");
        taskManager.getTaskById(1);
        printAllTasks(taskManager);
    }

    public static void printAllTasks(TaskManager manager) {
        System.out.println("Задачи:");
        manager.getAllTasks().forEach(System.out::println);

        System.out.println("\nЭпики:");
        manager.getAllEpics().forEach(System.out::println);

        System.out.println("\nПодзадачи:");
        manager.getAllSubTasks().forEach(System.out::println);

        System.out.println("\nИстория:");
        manager.getHistory().forEach(System.out::println);
    }
}
