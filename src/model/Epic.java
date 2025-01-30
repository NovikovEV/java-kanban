package model;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private final List<Integer> subTasksIdList = new ArrayList<>();

    public Epic(String taskName, String description) {
        super(taskName, description, TaskStatus.NEW);
    }

    public Epic(int id, String taskName, String description, TaskStatus taskStatus) {
        super(id, taskName, description, taskStatus);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + getId() +
                ", name=" + getTaskName() +
                ", subTasksIdList=" + subTasksIdList +
                ", status=" + getTaskStatus() +
                '}';
    }

    @Override
    public String serializeToCsv() {
        return String.format("%s,%s,%s,%s,%s,%s\n", getId(), TaskType.EPIC, getTaskName(), getTaskStatus(), getDescription(), subTasksIdList);
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
