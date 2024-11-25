package model;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private final List<Integer> subTasksIdList = new ArrayList<>();

    public Epic(String taskName, String description) {
        super(taskName, description, TaskStatus.NEW);
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

    public void addSubTaskId(SubTask subTask) {
        subTasksIdList.add(subTask.getId());
    }

    public void removeSubTaskId(int id) {
        subTasksIdList.remove((Integer) id);
    }

    public Integer getSubTaskId(int subTaskId) {
        return subTasksIdList.stream()
                .filter(id -> id == subTaskId)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    public List<Integer> getSubTasksIdList() {
        return List.copyOf(subTasksIdList);
    }

    public void setSubTasksIdList(List<Integer> newSubTasksIdList) {
        subTasksIdList.addAll(newSubTasksIdList);
    }

    public void clearSubTasksList() {
        subTasksIdList.clear();
    }
}
