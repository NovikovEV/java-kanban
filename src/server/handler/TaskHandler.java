package server.handler;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import dto.TaskDto;
import exception.ManagerException;
import manager.TaskManager;
import model.Task;
import server.HttpMethod;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

public class TaskHandler extends BaseHttpHandler {
    public TaskHandler(TaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        HttpMethod httpMethod = HttpMethod.valueOf(exchange.getRequestMethod());

        String[] path = exchange.getRequestURI().getPath().split("/");

        switch (httpMethod) {
            case GET -> {
                if (path.length == 3) {
                    getTaskById(exchange);
                } else {
                    getAllTasks(exchange);
                }
            }
            case POST -> {
                if (path.length == 3) {
                    updateTask(exchange);
                } else {
                    addTask(exchange);
                }
            }
            case DELETE -> deleteTaskById(exchange);
        }
    }

    private void getAllTasks(HttpExchange exchange) throws IOException {
        List<Task> tasks = taskManager.getAllTasks();
        writeResponse(exchange, gson.toJson(tasks), HttpURLConnection.HTTP_OK);
    }

    private void getTaskById(HttpExchange exchange) throws IOException {
        Optional<Integer> id = getId(exchange);

        if (id.isPresent()) {
            Optional<Task> task = taskManager.getTaskById(id.get());

            if (task.isPresent()) {
                writeResponse(exchange, gson.toJson(task.get()), HttpURLConnection.HTTP_OK);
            } else {
                writeResponse(exchange, convertToMessage("Задача с id=" + id.get() + " не найдена"), HttpURLConnection.HTTP_NOT_FOUND);
            }

        } else {
            writeResponse(exchange, convertToMessage("Не верно указан id"), HttpURLConnection.HTTP_BAD_REQUEST);
        }
    }

    private void addTask(HttpExchange exchange) throws IOException {
        if (checkHeader(exchange)) {
            InputStream inputStream = exchange.getRequestBody();
            Optional<TaskDto> taskDto = parseTask(inputStream);

            Optional<Integer> id = getId(exchange);

            if (id.isEmpty()) {
                if (taskDto.isPresent()) {
                    Task task = convertToTask(taskDto.get());

                    try {
                        taskManager.addTask(task);
                        writeResponse(exchange, convertToMessage("Задача добавлена"), HttpURLConnection.HTTP_CREATED);
                    } catch (ManagerException e) {
                        writeResponse(exchange, convertToMessage(e.getMessage()), HttpURLConnection.HTTP_NOT_ACCEPTABLE);
                    }
                }
            }
        } else {
            writeResponse(exchange, convertToMessage("Неправильный формат запроса"), HttpURLConnection.HTTP_BAD_REQUEST);
        }
    }

    private void updateTask(HttpExchange exchange) throws IOException {
        if (checkHeader(exchange)) {
            InputStream inputStream = exchange.getRequestBody();
            Optional<TaskDto> taskDto = parseTask(inputStream);

            Optional<Integer> id = getId(exchange);

            if (id.isPresent()) {
                Optional<Task> task = taskManager.getTaskById(id.get());

                if (task.isPresent() && taskDto.isPresent()) {
                    taskManager.updateTask(convertToTask(taskDto.get(), id.get()));
                    writeResponse(exchange, convertToMessage("Задача обновлена"), HttpURLConnection.HTTP_CREATED);
                } else {
                    writeResponse(exchange, convertToMessage("Задача с id=" + id.get() + " не найдена"), HttpURLConnection.HTTP_NOT_FOUND);
                }
            } else {
                writeResponse(exchange, convertToMessage("Не верно указан id"), HttpURLConnection.HTTP_BAD_REQUEST);
            }

        } else {
            writeResponse(exchange, convertToMessage("Неправильный формат запроса"), HttpURLConnection.HTTP_BAD_REQUEST);
        }
    }

    private void deleteTaskById(HttpExchange exchange) throws IOException {
        Optional<Integer> id = getId(exchange);

        if (id.isPresent()) {
            Optional<Task> task = taskManager.getTaskById(id.get());

            if (task.isPresent()) {
                taskManager.deleteTaskById(id.get());
                writeResponse(exchange, convertToMessage("Задача с id=" + id.get() + " удалена"), HttpURLConnection.HTTP_OK);
            } else {
                writeResponse(exchange, convertToMessage("Задача с id=" + id.get() + " не найдена"), HttpURLConnection.HTTP_NOT_FOUND);
            }

        } else {
            writeResponse(exchange, convertToMessage("Не верно указан id"), HttpURLConnection.HTTP_BAD_REQUEST);
        }
    }

    private Optional<TaskDto> parseTask(InputStream inputStream) throws IOException {
        String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        JsonElement jsonElement = JsonParser.parseString(body);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        TaskDto task = gson.fromJson(jsonObject, TaskDto.class);

        if (task != null) {
            return Optional.of(task);
        }

        return Optional.empty();
    }

    private Task convertToTask(TaskDto taskDto) {
        return new Task(
                taskDto.taskName(),
                taskDto.description(),
                taskDto.taskStatus(),
                taskDto.duration(),
                taskDto.startTime()
        );
    }

    private Task convertToTask(TaskDto taskDto, int id) {
        return new Task(
                id,
                taskDto.taskName(),
                taskDto.description(),
                taskDto.taskStatus(),
                taskDto.duration(),
                taskDto.startTime()
        );
    }
}