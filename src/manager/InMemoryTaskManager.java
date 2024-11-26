package manager;

import model.Epic;
import model.SubTask;
import model.Task;
import model.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager {
    private final Map<Integer, Task> taskMap = new HashMap<>();
    private final Map<Integer, Epic> epicMap = new HashMap<>();
    private final Map<Integer, SubTask> subTaskMap = new HashMap<>();
    private static int id = 1;

    public void addTask(Task task) {
        task.setId(nextId());
        taskMap.put(task.getId(), task);
    }

    public Task getTaskById(int id) {
        return taskMap.get(id);
    }

    public List<Task> getAllTasks() {
        return taskMap.values().stream().toList();
    }

    public Task updateTask(Task task) {
        if (taskMap.containsKey(task.getId())) {
            taskMap.replace(task.getId(), task);
        }
        return taskMap.get(task.getId());
    }

    public void deleteTaskById(int id) {
        taskMap.remove(id);
    }

    public void removeAllTasks() {
        taskMap.clear();
    }

    public void addEpic(Epic epic) {
        epic.setId(nextId());
        epicMap.put(epic.getId(), epic);
    }

    public Epic getEpicById(int id) {
        return epicMap.get(id);
    }

    public List<Epic> getAllEpics() {
        return epicMap.values().stream().toList();
    }

    public void updateEpic(Epic epic) {
        if (epicMap.containsKey(epic.getId())) {
            epicMap.replace(epic.getId(), epic);
        }
    }

    public void deleteEpicById(int id) {
        Epic epic = epicMap.get(id);
        removeListSubTasks(epic.getSubTasksIdList());
        epicMap.remove(id);
    }

    public void removeAllEpics() {
        subTaskMap.clear();
        epicMap.clear();
    }

    public void addSubTask(SubTask subTask) {
        subTask.setId(nextId());
        Epic epic = getEpicById(subTask.getEpicId());
        epic.addSubTaskId(subTask);

        subTaskMap.put(subTask.getId(), subTask);
        updateEpicStatus(epicMap.get(subTask.getEpicId()));
    }

    public SubTask getSubTaskById(int id) {
        return subTaskMap.get(id);
    }

    public List<SubTask> getAllSubTasks() {
        return subTaskMap.values().stream().toList();
    }

    public void updateSubTask(SubTask subTask) {
        if (subTaskMap.containsKey(subTask.getId())) {
            subTaskMap.replace(subTask.getId(), subTask);
        }
        updateEpicStatus(epicMap.get(subTask.getEpicId()));
    }

    public void deleteSubTaskById(int id) {
        Epic epic = epicMap.get(subTaskMap.get(id).getEpicId());
        epic.removeSubTaskId(id);
        subTaskMap.remove(id);
        updateEpicStatus(epic);
    }

    public void removeAllSubtasks() {
        clearEpicSubTasks();
        subTaskMap.clear();
        epicMap.values().forEach(this::updateEpicStatus);
    }

    private static int nextId() {
        return id++;
    }

    private void updateEpicStatus(Epic epic) {
        if (epic.getSubTasksIdList().isEmpty()) {
            epic.setTaskStatus(TaskStatus.NEW);
            return;
        }

        boolean allTasksIsNew = true;
        boolean allTasksIsDone = true;

        List<SubTask> epicSubTasks = getListSubTasksByEpicId(epic.getSubTasksIdList());

        for (SubTask subTask : epicSubTasks) {
            if (subTask.getTaskStatus() != TaskStatus.NEW) {
                allTasksIsNew = false;
            }

            if (subTask.getTaskStatus() != TaskStatus.DONE) {
                allTasksIsDone = false;
            }
        }

        if (allTasksIsDone) {
            epic.setTaskStatus(TaskStatus.DONE);
        } else if (allTasksIsNew) {
            epic.setTaskStatus(TaskStatus.NEW);
        } else {
            epic.setTaskStatus(TaskStatus.IN_PROGRESS);
        }
    }

    private void removeListSubTasks(List<Integer> subTaskIds) {
        for (Integer id : subTaskIds) {
            subTaskMap.remove(id);
        }
    }

    private List<SubTask> getListSubTasksByEpicId(List<Integer> subTaskIds) {
        List<SubTask> subTasks = new ArrayList<>();
        for (Integer id : subTaskIds) {
            subTasks.add(subTaskMap.get(id));
        }
        return subTasks;
    }

    private void clearEpicSubTasks() {
        epicMap.values().forEach(Epic::clearSubTasksList);
    }
}
