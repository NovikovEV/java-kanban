package server;

import adapter.DurationAdapter;
import adapter.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import manager.TaskManager;
import server.handler.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;

public class HttpTaskServer {
    private final int PORT;
    private final String HOSTNAME;
    private final HttpServer httpServer;

    public HttpTaskServer(int PORT, String HOSTNAME, TaskManager taskManager) throws IOException {
        this.PORT = PORT;
        this.HOSTNAME = HOSTNAME;

        httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(HOSTNAME, PORT), 0);

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
        httpServer.createContext("/tasks", new TaskHandler(taskManager, gson));
        httpServer.createContext("/epics", new EpicHandler(taskManager, gson));
        httpServer.createContext("/subtasks", new SubTaskHandler(taskManager, gson));
        httpServer.createContext("/history", new HistoryHandler(taskManager, gson));
        httpServer.createContext("/prioritized", new PrioritizedHandler(taskManager, gson));
    }

    public void start() {
        System.out.printf("Сервер доступен по адресу http://%s:%d\n", HOSTNAME, PORT);
        httpServer.start();
    }

    public void stop(int delay) {
        httpServer.stop(delay);
    }
}