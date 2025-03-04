package server.handler;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import dto.SubTaskDto;
import exception.ManagerException;
import manager.TaskManager;
import model.Epic;
import model.SubTask;
import server.HttpMethod;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

public class SubTaskHandler extends BaseHttpHandler {
    public SubTaskHandler(TaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        HttpMethod httpMethod = HttpMethod.valueOf(exchange.getRequestMethod());

        String[] path = exchange.getRequestURI().getPath().split("/");

        switch (httpMethod) {
            case GET -> {
                if (path.length == 3) {
                    getSubTaskById(exchange);
                } else {
                    getAllSubTasks(exchange);
                }
            }
            case POST -> {
                if (path.length == 3) {
                    updateSubTask(exchange);
                } else {
                    addSubTask(exchange);
                }
            }
            case DELETE -> deleteSubTaskById(exchange);
        }
    }

    private void getAllSubTasks(HttpExchange exchange) throws IOException {
        List<SubTask> subTasks = taskManager.getAllSubTasks();
        writeResponse(exchange, gson.toJson(subTasks), HttpURLConnection.HTTP_OK);
    }

    private void getSubTaskById(HttpExchange exchange) throws IOException {
        Optional<Integer> id = getId(exchange);

        if (id.isPresent()) {
            Optional<SubTask> subTask = taskManager.getSubTaskById(id.get());

            if (subTask.isPresent()) {
                writeResponse(exchange, gson.toJson(subTask.get()), HttpURLConnection.HTTP_OK);
            } else {
                writeResponse(exchange, convertToMessage("Подзадача с id=" + id.get() + " не найдена"), HttpURLConnection.HTTP_NOT_FOUND);
            }

        } else {
            writeResponse(exchange, convertToMessage("Не верно указан id"), HttpURLConnection.HTTP_BAD_REQUEST);
        }
    }

    private void addSubTask(HttpExchange exchange) throws IOException {
        if (checkHeader(exchange)) {
            InputStream inputStream = exchange.getRequestBody();
            Optional<SubTaskDto> subTaskDto = parseSubTask(inputStream);

            Optional<Integer> id = getId(exchange);

            if (id.isEmpty()) {
                if (subTaskDto.isPresent()) {
                    SubTask subTask = convertToSubtask(subTaskDto.get());
                    Optional<Epic> epic = taskManager.getEpicById(subTask.getEpicId());

                    if (epic.isPresent()) {
                        try {
                            taskManager.addSubTask(subTask);
                            writeResponse(exchange, convertToMessage("Подзадача добавлена"), HttpURLConnection.HTTP_CREATED);
                        } catch (ManagerException e) {
                            writeResponse(exchange, convertToMessage(e.getMessage()), HttpURLConnection.HTTP_NOT_ACCEPTABLE);
                        }
                    } else {
                        writeResponse(exchange, convertToMessage("Невозможно добавить подзадачу. Эпика с id=" + subTask.getEpicId() + " не существует"), HttpURLConnection.HTTP_NOT_ACCEPTABLE);
                    }
                }
            }
        } else {
            writeResponse(exchange, convertToMessage("Неправильный формат запроса"), HttpURLConnection.HTTP_BAD_REQUEST);
        }
    }

    private void updateSubTask(HttpExchange exchange) throws IOException {
        if (checkHeader(exchange)) {
            InputStream inputStream = exchange.getRequestBody();
            Optional<SubTaskDto> subTaskDto = parseSubTask(inputStream);

            Optional<Integer> id = getId(exchange);

            if (id.isPresent()) {
                if (subTaskDto.isPresent()) {
                    Optional<SubTask> subTask = taskManager.getSubTaskById(id.get());

                    if (subTask.isPresent()) {
                        Optional<Epic> epic = taskManager.getEpicById(subTaskDto.get().epicId());
                        if (epic.isPresent()) {
                            taskManager.updateSubTask(convertToSubtask(subTaskDto.get(), id.get()));
                            writeResponse(exchange, convertToMessage("Подзадача обновлена"), HttpURLConnection.HTTP_CREATED);
                        } else {
                            writeResponse(exchange, convertToMessage("Невозможно обновить подзадачу. Эпика с id=" + subTaskDto.get().epicId() + " не существует"), HttpURLConnection.HTTP_NOT_ACCEPTABLE);
                        }
                    } else {
                        writeResponse(exchange, convertToMessage("Подзадача с id=" + id.get() + " не найдена"), HttpURLConnection.HTTP_NOT_FOUND);
                    }
                }
            } else {
                writeResponse(exchange, convertToMessage("Не верно указан id"), HttpURLConnection.HTTP_BAD_REQUEST);
            }
        } else {
            writeResponse(exchange, convertToMessage("Неправильный формат запроса"), HttpURLConnection.HTTP_BAD_REQUEST);
        }
    }

    private void deleteSubTaskById(HttpExchange exchange) throws IOException {
        Optional<Integer> id = getId(exchange);

        if (id.isPresent()) {
            Optional<SubTask> subTask = taskManager.getSubTaskById(id.get());

            if (subTask.isPresent()) {
                taskManager.deleteSubTaskById(id.get());
                writeResponse(exchange, convertToMessage("Подзадача с id=" + id.get() + " удалена"), HttpURLConnection.HTTP_OK);
            } else {
                writeResponse(exchange, convertToMessage("Подзадача с id=" + id.get() + " не найдена"), HttpURLConnection.HTTP_NOT_FOUND);
            }
        } else {
            writeResponse(exchange, convertToMessage("Не верно указан id"), HttpURLConnection.HTTP_BAD_REQUEST);
        }
    }

    private Optional<SubTaskDto> parseSubTask(InputStream inputStream) throws IOException {
        String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        JsonElement jsonElement = JsonParser.parseString(body);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        SubTaskDto subTask = gson.fromJson(jsonObject, SubTaskDto.class);

        if (subTask != null) {
            return Optional.of(subTask);
        }

        return Optional.empty();
    }

    private SubTask convertToSubtask(SubTaskDto subTaskDto) {
        return new SubTask(
                subTaskDto.epicId(),
                subTaskDto.taskName(),
                subTaskDto.description(),
                subTaskDto.taskStatus(),
                subTaskDto.duration(),
                subTaskDto.startTime()
        );
    }

    private SubTask convertToSubtask(SubTaskDto subTaskDto, int id) {
        return new SubTask(
                id,
                subTaskDto.epicId(),
                subTaskDto.taskName(),
                subTaskDto.description(),
                subTaskDto.taskStatus(),
                subTaskDto.duration(),
                subTaskDto.startTime()
        );
    }
}