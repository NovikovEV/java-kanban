package model;

import util.DataTimeFormat;

import java.time.Duration;
import java.time.LocalDateTime;

public class SubTask extends Task {
    private int epicId;

    public SubTask(Epic epic, String taskName, String description, TaskStatus taskStatus, Duration duration, LocalDateTime startTime) {
        super(taskName, description, taskStatus, duration, startTime);
        this.epicId = epic.getId();
    }

    public SubTask(int id, int epicId, String taskName, String description, TaskStatus taskStatus, Duration duration, LocalDateTime startTime) {
        super(id, taskName, description, taskStatus, duration, startTime);
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "id=" + getId() +
                ", epicId=" + epicId +
                ", name=" + getTaskName() +
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
                TaskType.SUBTASK,
                getTaskName(),
                getTaskStatus(),
                getDescription(),
                getDuration().toMinutes(),
                getStartTime().format(DataTimeFormat.getDTF()),
                getEndTime().format(DataTimeFormat.getDTF()),
                getEpicId());
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }
}
