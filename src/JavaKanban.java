import manager.InMemoryTaskManager;
import model.Epic;
import model.SubTask;
import model.Task;
import model.TaskStatus;

public class JavaKanban {
    public static void main(String[] args) {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();

        //Создайте две задачи, а также эпик с двумя подзадачами и эпик с одной подзадачей
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

        System.out.printf("Вывод всех задач: %s%n", taskManager.getAllTasks());
        System.out.printf("Вывод задачи по id=1: %s%n", taskManager.getTaskById(1));
        System.out.printf("Вывод задачи по id=2: %s%n", taskManager.getTaskById(2));
        task2.setTaskStatus(TaskStatus.IN_PROGRESS);
        System.out.printf("Обновление задачи задачи id=2: %s%n", taskManager.updateTask(task2));
        taskManager.deleteTaskById(1);
        System.out.printf("Удаление задачи задачи с id=1: %s%n", taskManager.getAllTasks());
        taskManager.removeAllTasks();
        System.out.printf("Удаление всех задач: %s%n%n", taskManager.getAllTasks());

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

        //Распечатайте списки эпиков и подзадач.
        System.out.printf("Список эпиков: %s%n", taskManager.getAllEpics());
        System.out.printf("Список подзадач: %s%n", taskManager.getAllSubTasks());

        //Измените статусы созданных объектов, распечатайте их.
        subTask1.setTaskStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateSubTask(subTask1);
        subTask2.setTaskStatus(TaskStatus.DONE);
        subTask3.setTaskStatus(TaskStatus.DONE);
        taskManager.updateSubTask(subTask3);

        //Проверьте, что статус задачи и подзадачи сохранился, а статус эпика рассчитался по статусам подзадач
        System.out.println("\nПосле изменения подзадач");
        System.out.printf("Список эпиков: %s%n", taskManager.getAllEpics());
        System.out.printf("Список подзадач: %s%n", taskManager.getAllSubTasks());

        //попробуйте удалить одну из задач и один из эпиков
        taskManager.deleteSubTaskById(5);
        System.out.println("\nПосле удаления подзадачи c id=5");
        System.out.printf("Список эпиков: %s%n", taskManager.getAllEpics());
        System.out.printf("Список подзадач: %s%n", taskManager.getAllSubTasks());

        taskManager.deleteEpicById(4);
        System.out.println("\nПосле удаления эпика c id=4");
        System.out.printf("Список эпиков: %s%n", taskManager.getAllEpics());
        System.out.printf("Список подзадач: %s%n", taskManager.getAllSubTasks());

        taskManager.removeAllEpics();
        System.out.println("\nПосле удаления всех эпиков'");
        System.out.printf("Список эпиков: %s%n", taskManager.getAllEpics());
        System.out.printf("Список подзадач: %s%n", taskManager.getAllSubTasks());

        Epic epic3 = new Epic(
                "Уборка по дому",
                "произвести уборку по всему дому"
        );
        taskManager.addEpic(epic3);
        System.out.println("\nОбновление эпика'");
        System.out.printf("Список эпиков: %s%n", taskManager.getAllEpics());


        epic1.setTaskName("Уборка");
        taskManager.updateEpic(epic3);
        System.out.println("После обновления эпика'");
        System.out.printf("Список эпиков: %s%n", taskManager.getAllEpics());

        SubTask subTask4 = new SubTask(
                epic3,
                "Пропылесосить комнаты",
                "тщательно",
                TaskStatus.DONE
        );
        taskManager.addSubTask(subTask4);
        System.out.println("\nВывод подзадачи по id");
        System.out.printf("Список эпиков: %s%n", taskManager.getSubTaskById(9));

        System.out.println("\nУдаление всех подзадач");
        System.out.printf("Список эпиков: %s%n", taskManager.getAllEpics());
        System.out.printf("Список подзадач: %s%n", taskManager.getAllSubTasks());

        System.out.println("\nПосле удаление всех подзадач");
        taskManager.removeAllSubtasks();
        System.out.printf("Список эпиков: %s%n", taskManager.getAllEpics());
        System.out.printf("Список подзадач: %s%n", taskManager.getAllSubTasks());
    }
}
