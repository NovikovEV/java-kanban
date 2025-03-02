package model;

import util.DataTimeFormat;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private final List<Integer> subTasksIdList = new ArrayList<>();

    public Epic(String taskName, String description) {
        super(taskName, description, TaskStatus.NEW, Duration.ofMinutes(0), LocalDateTime.now());
    }

    public Epic(String taskName, String description, Duration taskDuration, LocalDateTime startTime) {
        super(taskName, description, TaskStatus.NEW, taskDuration, startTime);
    }

    public Epic(int id, String taskName, String description) {
        super(id, taskName, description, TaskStatus.NEW, Duration.ofMinutes(0), LocalDateTime.now());
    }

    public Epic(int id, String taskName, String description, TaskStatus taskStatus, Duration duration, LocalDateTime startTime) {
        super(id, taskName, description, TaskStatus.NEW, duration, startTime);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + getId() +
                ", name=" + getTaskName() +
                ", subTasksIdList=" + subTasksIdList +
                ", status=" + getTaskStatus() +
                ", duration=" + getDuration().toMinutes() +
                ", startTime=" + getStartTime().format(DataTimeFormat.getDTF()) +
                ", endTime=" + getEndTime().format(DataTimeFormat.getDTF()) +
                '}';
    }

    @Override
    public String serializeToCsv() {
        return String.format(
                "%s,%s,%s,%s,%s,%s,%s,%s,%s\n",
                getId(),
                TaskType.EPIC,
                getTaskName(),
                getTaskStatus(),
                getDescription(),
                getDuration().toMinutes(),
                getStartTime().format(DataTimeFormat.getDTF()),
                getEndTime().format(DataTimeFormat.getDTF()),
                subTasksIdList);
    }

    public void addSubTaskId(SubTask subTask) {
        subTasksIdList.add(subTask.getId());
    }

    public void removeSubTaskId(int id) {
        subTasksIdList.remove((Integer) id);
    }

    public List<Integer> getSubTasksIdList() {
        return List.copyOf(subTasksIdList);
    }

    public void clearSubTasksList() {
        subTasksIdList.clear();
    }
}
