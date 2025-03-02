package server.handler;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import dto.EpicDto;
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

public class EpicHandler extends BaseHttpHandler {
    public EpicHandler(TaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        HttpMethod httpMethod = HttpMethod.valueOf(exchange.getRequestMethod());

        String[] path = exchange.getRequestURI().getPath().split("/");

        switch (httpMethod) {
            case GET -> {
                if (path.length > 3) {
                    getEpicSubtasks(exchange);
                } else if (path.length > 2) {
                    getEpicById(exchange);
                } else {
                    getAllEpics(exchange);
                }
            }
            case POST -> addEpic(exchange);
            case DELETE -> deleteEpicById(exchange);
        }
    }

    private void getAllEpics(HttpExchange exchange) throws IOException {
        List<Epic> epics = taskManager.getAllEpics();
        writeResponse(exchange, gson.toJson(epics), HttpURLConnection.HTTP_OK);
    }

    private void getEpicById(HttpExchange exchange) throws IOException {
        Optional<Integer> id = getId(exchange);

        if (id.isPresent()) {
            Optional<Epic> epic = taskManager.getEpicById(id.get());

            if (epic.isPresent()) {
                writeResponse(exchange, gson.toJson(epic.get()), HttpURLConnection.HTTP_OK);
            } else {
                writeResponse(exchange, convertToMessage("Эпик с id=" + id.get() + " не найдена"), HttpURLConnection.HTTP_NOT_FOUND);
            }
        } else {
            writeResponse(exchange, convertToMessage("Не верно указан id"), HttpURLConnection.HTTP_BAD_REQUEST);
        }
    }

    private void getEpicSubtasks(HttpExchange exchange) throws IOException {
        String[] path = exchange.getRequestURI().getPath().split("/");
        Optional<String> parameter = Optional.of(path[3]);

        Optional<Integer> id = getId(exchange);

        if (parameter.get().equals("subtasks")) {
            if (id.isPresent()) {
                Optional<Epic> epic = taskManager.getEpicById(id.get());

                if (epic.isPresent()) {
                    List<SubTask> subTasks = taskManager.getListSubTasksByEpicId(epic.get().getSubTasksIdList());
                    if (subTasks.isEmpty()) {
                        writeResponse(exchange, convertToMessage("Список подзадач в эпике пуст"), HttpURLConnection.HTTP_NOT_FOUND);
                    }
                    writeResponse(exchange, gson.toJson(subTasks), HttpURLConnection.HTTP_OK);
                } else {
                    writeResponse(exchange, convertToMessage("Эпик с id=" + id.get() + " не найдена"), HttpURLConnection.HTTP_NOT_FOUND);
                }
            } else {
                writeResponse(exchange, convertToMessage("Не верно указан id"), HttpURLConnection.HTTP_BAD_REQUEST);
            }
        } else {
            writeResponse(exchange, convertToMessage("Неправильный формат запроса"), HttpURLConnection.HTTP_BAD_REQUEST);
        }
    }

    private void addEpic(HttpExchange exchange) throws IOException {
        if (checkHeader(exchange)) {
            InputStream inputStream = exchange.getRequestBody();
            Optional<EpicDto> epicDto = parseEpic(inputStream);

            Optional<Integer> id = getId(exchange);

            if (id.isEmpty()) {
                if (epicDto.isPresent()) {
                    Epic epic = convertToEpic(epicDto.get());
                    taskManager.addEpic(epic);
                    writeResponse(exchange, convertToMessage("Эпик добавлен"), HttpURLConnection.HTTP_CREATED);
                }
            } else {
                writeResponse(exchange, convertToMessage("Неправильный формат запроса"), HttpURLConnection.HTTP_BAD_REQUEST);
            }

        } else {
            writeResponse(exchange, convertToMessage("Неправильный формат запроса"), HttpURLConnection.HTTP_BAD_REQUEST);
        }
    }

    private void deleteEpicById(HttpExchange exchange) throws IOException {
        Optional<Integer> id = getId(exchange);

        if (id.isPresent()) {
            Optional<Epic> epic = taskManager.getEpicById(id.get());

            if (epic.isPresent()) {
                taskManager.deleteEpicById(id.get());
                writeResponse(exchange, convertToMessage("Эпик с id=" + id.get() + " удален"), HttpURLConnection.HTTP_OK);
            } else {
                writeResponse(exchange, convertToMessage("Эпик с id=" + id.get() + " не найден"), HttpURLConnection.HTTP_NOT_FOUND);
            }
        } else {
            writeResponse(exchange, convertToMessage("Не верно указан id"), HttpURLConnection.HTTP_BAD_REQUEST);
        }
    }

    private Optional<EpicDto> parseEpic(InputStream inputStream) throws IOException {
        String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        JsonElement jsonElement = JsonParser.parseString(body);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        EpicDto epic = gson.fromJson(jsonObject, EpicDto.class);

        if (epic != null) {
            return Optional.of(epic);
        }

        return Optional.empty();
    }

    private Epic convertToEpic(EpicDto epicDto) {
        return new Epic(
                epicDto.taskName(),
                epicDto.description()
        );
    }
}


