package manager;

import model.Epic;
import model.SubTask;
import model.Task;

import java.util.List;

public interface TaskManager {
    void addTask(Task task);

    Task getTaskById(int id);

    List<Task> getAllTasks();

    Task updateTask(Task task);

    void deleteTaskById(int id);

    void removeAllTasks();

    void addEpic(Epic epic);

    Epic getEpicById(int id);

    List<Epic> getAllEpics();

    void updateEpic(Epic epic);

    void deleteEpicById(int id);

    void removeAllEpics();

    void addSubTask(SubTask subTask);

    SubTask getSubTaskById(int id);

    List<SubTask> getAllSubTasks();

    void updateSubTask(SubTask subTask);

    void deleteSubTaskById(int id);

    void removeAllSubtasks();

    List<Task> getHistory();
}
