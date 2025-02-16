package manager;

import exception.InvalidTaskTimeException;
import model.Epic;
import model.SubTask;
import model.Task;
import model.TaskStatus;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager{
    private final Map<Integer, Task> taskMap = new HashMap<>();
    private final Map<Integer, Epic> epicMap = new HashMap<>();
    private final Map<Integer, SubTask> subTaskMap = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistoryManager();
    private final Set<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));
    private int id = 1;

    @Override
    public void addTask(Task task) {
        task.setId(nextId());

        try {
            validateTask(task);
        } catch (InvalidTaskTimeException e) {
            System.err.println(e.getMessage());
        }

        taskMap.put(task.getId(), task);
        prioritizedTasks.add(task);
    }

    @Override
    public Optional<Task> getTaskById(int id) {
        Task task = taskMap.get(id);
        historyManager.add(task);
        return Optional.of(task);
    }

    @Override
    public List<Task> getAllTasks() {
        return taskMap.values().stream().toList();
    }

    @Override
    public Task updateTask(Task task) {
        try {
            validateTask(task);
        } catch (InvalidTaskTimeException e) {
            System.err.println(e.getMessage());
        }

        if (taskMap.containsKey(task.getId())) {
            taskMap.replace(task.getId(), task);
        }
        return taskMap.get(task.getId());
    }

    @Override
    public void deleteTaskById(int id) {
        prioritizedTasks.remove(taskMap.get(id));
        taskMap.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void removeAllTasks() {
        taskMap.values().forEach(prioritizedTasks::remove);
        taskMap.clear();
    }

    @Override
    public void addEpic(Epic epic) {
        epic.setId(nextId());
        epicMap.put(epic.getId(), epic);
    }

    @Override
    public Optional<Epic> getEpicById(int id) {
        Epic epic = epicMap.get(id);
        historyManager.add(epic);
        return Optional.of(epic);
    }

    @Override
    public List<Epic> getAllEpics() {
        return epicMap.values().stream().toList();
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epicMap.containsKey(epic.getId())) {
            epicMap.replace(epic.getId(), epic);
        }
    }

    @Override
    public void deleteEpicById(int id) {
        Epic epic = epicMap.get(id);
        removeListSubTasks(epic.getSubTasksIdList());
        epicMap.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void removeAllEpics() {
        epicMap.values().forEach(prioritizedTasks::remove);
        subTaskMap.clear();
        epicMap.clear();
    }

    @Override
    public void addSubTask(SubTask subTask) {
        subTask.setId(nextId());

        try {
            validateTask(subTask);
        } catch (InvalidTaskTimeException e) {
            System.err.println(e.getMessage());
        }

        Epic epic = epicMap.get(subTask.getEpicId());
        epic.addSubTaskId(subTask);

        subTaskMap.put(subTask.getId(), subTask);
        updateEpicStatus(epicMap.get(subTask.getEpicId()));
        updateEpicTime(epic);

        prioritizedTasks.add(subTask);
    }

    @Override
    public Optional<SubTask> getSubTaskById(int id) {
        SubTask subTask = subTaskMap.get(id);
        historyManager.add(subTask);
        return Optional.of(subTask);
    }

    @Override
    public List<SubTask> getAllSubTasks() {
        return subTaskMap.values().stream().toList();
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        try {
            validateTask(subTask);
        } catch (InvalidTaskTimeException e) {
            System.err.println(e.getMessage());
        }

        if (subTaskMap.containsKey(subTask.getId())) {
            subTaskMap.replace(subTask.getId(), subTask);
        }
        updateEpicStatus(epicMap.get(subTask.getEpicId()));
        updateEpicTime(epicMap.get(subTask.getEpicId()));
        prioritizedTasks.add(subTask);
    }

    @Override
    public void deleteSubTaskById(int id) {
        Epic epic = epicMap.get(subTaskMap.get(id).getEpicId());
        epic.removeSubTaskId(id);
        prioritizedTasks.remove(subTaskMap.get(id));
        subTaskMap.remove(id);
        updateEpicStatus(epic);
        updateEpicTime(epic);
        historyManager.remove(id);
    }

    @Override
    public void removeAllSubtasks() {
        clearEpicSubTasks();
        subTaskMap.clear();
        epicMap.values().forEach(epic -> {
            updateEpicStatus(epic);
            epic.setStartTime(LocalDateTime.of(LocalDate.now(), LocalTime.now()));
            epic.setEndTime(LocalDateTime.of(LocalDate.now(), LocalTime.now()));
            epic.setDuration(Duration.ofMinutes(0));
            prioritizedTasks.remove(epic);
        });
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public Set<Task> getPrioritizedTasks() {
        return new LinkedHashSet<>(prioritizedTasks);
    }

    private int nextId() {
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
        return subTaskIds.stream().map(subTaskMap::get).toList();
    }

    private void clearEpicSubTasks() {
        epicMap.values().forEach(Epic::clearSubTasksList);
    }

    private void validateTask(Task task) throws InvalidTaskTimeException {
        List<Integer> collected = prioritizedTasks.stream()
                .filter(t -> t.getId() != task.getId())
                .filter(t -> ((t.getStartTime().isBefore(task.getStartTime()) && (t.getEndTime().isAfter(task.getStartTime())))) ||
                        (t.getStartTime().isBefore(task.getEndTime()) && (t.getEndTime().isAfter(task.getEndTime()))) ||
                        (t.getStartTime().isBefore(task.getStartTime()) && (t.getEndTime().isAfter(task.getEndTime()))) ||
                        (t.getStartTime().isAfter(task.getStartTime()) && (t.getEndTime().isBefore(task.getEndTime()))) ||
                        (t.getStartTime().equals(task.getStartTime())))
                .map(Task::getId)
                .toList();

        if (!collected.isEmpty()) {
            throw new InvalidTaskTimeException("Задача с id=" + task.getId() + " пересекается с задачами id=" + collected);
        }
    }

    private LocalDateTime getMinimalDateTime(Epic epic) {
        return epic.getSubTasksIdList().stream()
                .map(subTaskMap::get)
                .map(Task::getStartTime)
                .sorted(Comparator.naturalOrder())
                .limit(1)
                .toList()
                .getFirst();
    }

    private long calculateEpicDuration(List<Integer> subTaskIds) {
        return subTaskIds.stream()
                .map(subTaskMap::get)
                .map(subTask -> subTask.getDuration().toMinutes())
                .reduce(0L, Long::sum);
    }

    private void updateEpicTime(Epic epic) {
        LocalDateTime localDateTime = getMinimalDateTime(epic);
        long duration = calculateEpicDuration(epic.getSubTasksIdList());
        epic.setStartTime(localDateTime);
        epic.setDuration(Duration.ofMinutes(duration));
        epic.setEndTime(epic.getStartTime().plus(epic.getDuration()));
    }
}
