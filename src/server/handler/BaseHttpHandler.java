package server.handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dto.Message;
import manager.TaskManager;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Optional;

public abstract class BaseHttpHandler implements HttpHandler {
    TaskManager taskManager;
    Gson gson;

    public BaseHttpHandler(TaskManager taskManager, Gson gson) {
        this.taskManager = taskManager;
        this.gson = gson;
    }

    protected void writeResponse(HttpExchange exchange, String responseString, int responseCode) throws IOException {
        try (OutputStream os = exchange.getResponseBody()) {
            exchange.sendResponseHeaders(responseCode, 0);
            os.write(responseString.getBytes());
        }
        exchange.close();
    }

    protected Optional<Integer> getId(HttpExchange exchange) {
        String[] path = exchange.getRequestURI().getPath().split("/");
        try {
            return Optional.of(Integer.parseInt(path[2]));
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            return Optional.empty();
        }
    }

    protected boolean checkHeader(HttpExchange exchange) {
        Headers headers = exchange.getRequestHeaders();
        List<String> contentTypeValues = headers.get("Content-type");

        return (contentTypeValues != null) && (contentTypeValues.contains("application/json"));
    }

    protected String convertToMessage(String message) {
        return gson.toJson(new Message(message), Message.class);
    }
}