import manager.Managers;
import manager.TaskManager;
import model.Epic;
import model.SubTask;
import model.Task;
import model.TaskStatus;

public class JavaKanban {
    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();

        //Создайте две задачи, эпик с тремя подзадачами и эпик без подзадач.
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
                epic1,
                "Дать ему что нибудь",
                "Морковь, кабачки, свекла, цветная капуста",
                TaskStatus.NEW
        );

        taskManager.addSubTask(subTask1);
        taskManager.addSubTask(subTask2);
        taskManager.addSubTask(subTask3);
        printAllTasks(taskManager);

        //Запросите созданные задачи несколько раз в разном порядке
        System.out.println("\nПосле запроса в разном порядке.");
        taskManager.getSubTaskById(7);
        taskManager.getEpicById(4);
        taskManager.getTaskById(1);
        taskManager.getEpicById(3);
        taskManager.getSubTaskById(5);
        taskManager.getTaskById(1);
        taskManager.getEpicById(3);

        printAllTasks(taskManager);

        //Удалите задачу, которая есть в истории, и проверьте, что при печати она не будет выводиться.
        System.out.println("\nПосле удаления задачи.");
        taskManager.deleteTaskById(1);
        printAllTasks(taskManager);

        //Удалите эпик с тремя подзадачами и убедитесь, что из истории удалился как сам эпик, так и все его подзадачи.
        taskManager.deleteEpicById(3);
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
        //System.out.println(manager.getHistory());
        manager.getHistory().forEach(System.out::println);
    }
}
