package model;

public class SubTask extends Task {
    private int epicId;

    public SubTask(Epic epic, String taskName, String description, TaskStatus taskStatus) {
        super(taskName, description, taskStatus);
        this.epicId = epic.getId();
    }

    public SubTask(int id, int epicId, String taskName, String description, TaskStatus taskStatus) {
        super(id, taskName, description, taskStatus);
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "id=" + getId() +
                ", epicId=" + epicId +
                ", name=" + getTaskName() +
                ", status=" + getTaskStatus() +
                '}';
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }
}
