package manager;

import exception.ManagerSaveException;
import model.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    public void save() throws ManagerSaveException {
        final String title = "id,type,name,status,description,id_links\n";
        List<String> lines = new ArrayList<>();
        lines.add(title);

        getAllTasks().forEach(task -> lines.add(task.serializeToCsv()));
        getAllEpics().forEach(epic -> lines.add(epic.serializeToCsv()));
        getAllSubTasks().forEach(subTask -> lines.add(subTask.serializeToCsv()));

        saveToCsv(lines);
    }

    public void load() {
        List<String> lines;
        try {
            lines = loadFromCsv();
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }

        lines.removeFirst();

        for (String line : lines) {
            deSerialize(line);
        }
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);

        try {
            save();
        } catch (ManagerSaveException e) {
            throw new RuntimeException("Произошла ошибка сохранения задачи: " + task.getTaskName());
        }
    }

    @Override
    public Task updateTask(Task task) {
        Task updatedTask = super.updateTask(task);

        try {
            save();
        } catch (ManagerSaveException e) {
            throw new RuntimeException("Произошла ошибка сохранения задачи: " + task.getTaskName());
        }

        return updatedTask;
    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);

        try {
            save();
        } catch (ManagerSaveException e) {
            throw new RuntimeException("Произошла ошибка сохранения задачи c id=" + id);
        }
    }

    @Override
    public void removeAllTasks() {
        super.removeAllTasks();

        try {
            save();
        } catch (ManagerSaveException e) {
            throw new RuntimeException("Произошла ошибка сохранения задач в файл при удалении.");
        }
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);

        try {
            save();
        } catch (ManagerSaveException e) {
            throw new RuntimeException("Произошла ошибка сохранения эпика: " + epic.getTaskName());
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);

        try {
            save();
        } catch (ManagerSaveException e) {
            throw new RuntimeException("Произошла ошибка сохранения эпика: " + epic.getTaskName());
        }
    }

    @Override
    public void deleteEpicById(int id) {
        super.deleteEpicById(id);

        try {
            save();
        } catch (ManagerSaveException e) {
            throw new RuntimeException("Произошла ошибка сохранения эпика с id=" + id);
        }
    }

    @Override
    public void removeAllEpics() {
        super.removeAllEpics();

        try {
            save();
        } catch (ManagerSaveException e) {
            throw new RuntimeException("Произошла ошибка сохранения эпиков в файл при удалении.");
        }
    }

    @Override
    public void addSubTask(SubTask subTask) {
        super.addSubTask(subTask);

        try {
            save();
        } catch (ManagerSaveException e) {
            throw new RuntimeException("Произошла ошибка сохранения подзадачи: " + subTask.getTaskName());
        }
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        super.updateSubTask(subTask);

        try {
            save();
        } catch (ManagerSaveException e) {
            throw new RuntimeException("Произошла ошибка сохранения подзадачи: " + subTask.getTaskName());
        }
    }

    @Override
    public void deleteSubTaskById(int id) {
        super.deleteSubTaskById(id);

        try {
            save();
        } catch (ManagerSaveException e) {
            throw new RuntimeException("Произошла ошибка сохранения эпика с id=" + id);
        }
    }

    @Override
    public void removeAllSubtasks() {
        super.removeAllSubtasks();

        try {
            save();
        } catch (ManagerSaveException e) {
            throw new RuntimeException("Произошла ошибка сохранения подзадач в файл при удалении.");
        }
    }

    private void saveToCsv(List<String> lines) throws ManagerSaveException {
        if (file == null) {
            throw new ManagerSaveException("Невозможно сохранить данные в файл.");
        }

        try {
            FileWriter fileWriter = new FileWriter(file, StandardCharsets.UTF_8, false);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            for (String line : lines) {
                bufferedWriter.write(line);
            }

            bufferedWriter.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<String> loadFromCsv() throws ManagerSaveException {
        if (file == null) {
            throw new ManagerSaveException("Невозможно загрузить данные из файла.");
        }

        List<String> lines = new ArrayList<>();

        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while (bufferedReader.ready()) {
                lines.add(bufferedReader.readLine());
            }

            bufferedReader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return lines;
    }

    private void deSerialize(String line) {
        String[] lines = line.trim().split(",");
        TaskType taskType = TaskType.valueOf(lines[1]);
        switch (taskType) {
            case TASK -> super.addTask(
                    new Task(
                            Integer.parseInt(lines[0]),
                            lines[2],
                            lines[4],
                            getTaskStatusFromString(lines[3])
                    ));
            case EPIC -> super.addEpic(
                    new Epic(
                            Integer.parseInt(lines[0]),
                            lines[2],
                            lines[4],
                            getTaskStatusFromString(lines[3])
                    ));

            case SUBTASK -> {
                int subTaskId = Integer.parseInt(lines[0]);
                int epicId = Integer.parseInt(lines[lines.length - 1]);
                super.addSubTask(
                        new SubTask(
                                subTaskId,
                                epicId,
                                lines[2],
                                lines[4],
                                getTaskStatusFromString(lines[3])
                        ));
            }
        }
    }

    private TaskStatus getTaskStatusFromString(String line) {
        return switch (line) {
            case "NEW" -> TaskStatus.NEW;
            case "IN_PROGRESS" -> TaskStatus.IN_PROGRESS;
            case "DONE" -> TaskStatus.DONE;
            default -> throw new IllegalStateException("Неизвестное значение: " + line);
        };
    }
}
