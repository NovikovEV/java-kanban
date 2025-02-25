package model;

import util.DataTimeFormat;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task {
    private int id;
    private String taskName;
    private String description;
    private TaskStatus taskStatus;
    private Duration duration;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public Task(
            String taskName,
            String description,
            TaskStatus taskStatus,
            Duration duration,
            LocalDateTime startTime
    ) {
        this.taskName = taskName;
        this.description = description;
        this.taskStatus = taskStatus;
        this.duration = duration;
        this.startTime = startTime;
        this.endTime = startTime.plus(duration);
    }

    public Task(int id, String taskName, String description, TaskStatus taskStatus, Duration duration, LocalDateTime startTime) {
        this.id = id;
        this.taskName = taskName;
        this.description = description;
        this.taskStatus = taskStatus;
        this.duration = duration;
        this.startTime = startTime;
        this.endTime = startTime.plus(duration);
    }

    public String serializeToCsv() {
        return String.format(
                "%s,%s,%s,%s,%s,%s,%s,%s\n",
                id,
                TaskType.TASK,
                taskName, taskStatus,
                description,
                duration.toMinutes(),
                startTime.format(DataTimeFormat.getDTF()),
                endTime.format(DataTimeFormat.getDTF()));
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", taskName='" + taskName + '\'' +
                ", description='" + description + '\'' +
                ", taskStatus=" + taskStatus +
                ", duration=" + duration.toMinutes() +
                ", startTime=" + startTime.format(DataTimeFormat.getDTF()) +
                ", endTime=" + endTime.format(DataTimeFormat.getDTF()) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Task task)) {
            return false;
        }
        return id == task.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, taskName);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}
