package server;

import adapter.DurationAdapter;
import adapter.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.EpicDto;
import dto.SubTaskDto;
import dto.TaskDto;
import manager.Managers;
import manager.TaskManager;
import model.Epic;
import model.SubTask;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

class HttpTaskServerTest {
    private final TaskManager taskManager = Managers.getDefault();
    private final HttpTaskServer server = new HttpTaskServer(8080, "localhost", taskManager);
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();
    private final HttpClient client = HttpClient.newHttpClient();

    HttpTaskServerTest() throws IOException {
    }

    @BeforeEach
    public void setUp() {
        taskManager.removeAllTasks();
        taskManager.removeAllEpics();
        taskManager.removeAllSubtasks();
        server.start();
    }

    @AfterEach
    public void shutDown() {
        server.stop(1);
    }

    @Test
    public void testPostTask() throws IOException, InterruptedException {
        TaskDto taskDto = new TaskDto(
                "Task1",
                "Task description",
                TaskStatus.NEW,
                Duration.ofMinutes(5),
                LocalDateTime.of(LocalDate.of(2025, 2, 28), LocalTime.of(9, 0))
        );

        String json = gson.toJson(taskDto);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        int expectedStatusCode = 201;
        int expectedTasksSize = 1;
        String expectedTaskName = "Task1";
        int actuallyStatusCode = response.statusCode();
        int actuallyTasksSize = taskManager.getAllTasks().size();
        String actuallyTaskName = taskManager.getAllTasks().getFirst().getTaskName();

        Assertions.assertEquals(expectedStatusCode, actuallyStatusCode, "Статус код должен быть 201");
        Assertions.assertEquals(expectedTasksSize, actuallyTasksSize, "Некорректное количество задач");
        Assertions.assertEquals(expectedTaskName, actuallyTaskName, "Некорректное имя задачи");
    }

    @Test
    public void testUpdateTaskById() throws IOException, InterruptedException {
        TaskDto taskDto = new TaskDto(
                "Task1",
                "Task description",
                TaskStatus.NEW,
                Duration.ofMinutes(5),
                LocalDateTime.of(LocalDate.of(2025, 2, 28), LocalTime.of(9, 0))
        );

        String json = gson.toJson(taskDto);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        taskDto = new TaskDto(
                "Task2",
                "Task description",
                TaskStatus.NEW,
                Duration.ofMinutes(5),
                LocalDateTime.of(LocalDate.of(2025, 2, 28), LocalTime.of(9, 0))
        );

        json = gson.toJson(taskDto);

        request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/1"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        int expectedStatusCode = 201;
        int expectedTasksSize = 1;
        String expectedTaskName = "Task2";
        int actuallyStatusCode = response.statusCode();
        int actuallyTasksSize = taskManager.getAllTasks().size();
        String actuallyTaskName = taskManager.getAllTasks().getFirst().getTaskName();

        Assertions.assertEquals(expectedStatusCode, actuallyStatusCode, "Статус код должен быть 201");
        Assertions.assertEquals(expectedTasksSize, actuallyTasksSize, "Некорректное количество задач");
        Assertions.assertEquals(expectedTaskName, actuallyTaskName, "Некорректное имя задачи");
    }

    @Test
    public void testGetTasks() throws IOException, InterruptedException {
        Task task = new Task(
                "Task1",
                "Task description",
                TaskStatus.NEW,
                Duration.ofMinutes(5),
                LocalDateTime.of(LocalDate.of(2025, 2, 28), LocalTime.of(9, 0))
        );
        taskManager.addTask(task);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        int expectedStatusCode = 200;
        int actuallyStatusCode = response.statusCode();
        Assertions.assertEquals(expectedStatusCode, actuallyStatusCode, "Статус код должен быть 200");

        String expectedBody = "[{\"id\":1,\"taskName\":\"Task1\",\"description\":\"Task description\",\"taskStatus\":\"NEW\",\"duration\":\"5\",\"startTime\":\"09:00:00/28.02.2025\",\"endTime\":\"09:05:00/28.02.2025\"}]";
        String actuallyBody = response.body();
        Assertions.assertEquals(expectedBody, actuallyBody, "Ответ не совпадает с ожидаемым");
    }

    @Test
    public void testGetTaskById() throws IOException, InterruptedException {
        Task task = new Task(
                "Task1",
                "Task description",
                TaskStatus.NEW,
                Duration.ofMinutes(5),
                LocalDateTime.of(LocalDate.of(2025, 2, 28), LocalTime.of(9, 0))
        );
        taskManager.addTask(task);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/1"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        int expectedStatusCode = 200;
        int actuallyStatusCode = response.statusCode();
        Assertions.assertEquals(expectedStatusCode, actuallyStatusCode, "Статус код должен быть 200");

        String expectedBody = "{\"id\":1,\"taskName\":\"Task1\",\"description\":\"Task description\",\"taskStatus\":\"NEW\",\"duration\":\"5\",\"startTime\":\"09:00:00/28.02.2025\",\"endTime\":\"09:05:00/28.02.2025\"}";
        String actuallyBody = response.body();
        Assertions.assertEquals(expectedBody, actuallyBody, "Ответ не совпадает с ожидаемым");
    }

    @Test
    public void testDeleteTaskById() throws IOException, InterruptedException {
        TaskDto taskDto = new TaskDto(
                "Task1",
                "Task description",
                TaskStatus.NEW,
                Duration.ofMinutes(5),
                LocalDateTime.of(LocalDate.of(2025, 2, 28), LocalTime.of(9, 0))
        );

        String json = gson.toJson(taskDto);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        client.send(request, HttpResponse.BodyHandlers.ofString());

        request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/1"))
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        int expectedStatusCode = 200;
        int expectedTasksSize = 0;
        int actuallyStatusCode = response.statusCode();
        int actuallyTasksSize = taskManager.getAllTasks().size();

        Assertions.assertEquals(expectedStatusCode, actuallyStatusCode, "Статус код должен быть 200");
        Assertions.assertEquals(expectedTasksSize, actuallyTasksSize, "Менеджере задач не пуст");
    }

    @Test
    public void testPostEpic() throws IOException, InterruptedException {
        EpicDto epicDto = new EpicDto("Epic1", "Epic1 description");

        String json = gson.toJson(epicDto);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/epics"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        int expectedStatusCode = 201;
        int expectedEpicsSize = 1;
        String expectedEpicName = "Epic1";
        int actuallyStatusCode = response.statusCode();
        int actuallyEpicsSize = taskManager.getAllEpics().size();
        String actuallyEpicsName = taskManager.getAllEpics().getFirst().getTaskName();

        Assertions.assertEquals(expectedStatusCode, actuallyStatusCode, "Статус код должен быть 201");
        Assertions.assertEquals(expectedEpicsSize, actuallyEpicsSize, "Некорректное количество эпиков");
        Assertions.assertEquals(expectedEpicName, actuallyEpicsName, "Некорректное имя задачи");
    }

    @Test
    public void testGetEpics() throws IOException, InterruptedException {
        Epic epic = new Epic(
                "Epic1",
                "Epic1 description",
                Duration.ofMinutes(0),
                LocalDateTime.of(LocalDate.of(2025, 2, 28), LocalTime.of(9, 0))
        );

        taskManager.addEpic(epic);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/epics"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        int expectedStatusCode = 200;
        String expectedBody = "[{\"subTasksIdList\":[],\"id\":1,\"taskName\":\"Epic1\",\"description\":\"Epic1 description\",\"taskStatus\":\"NEW\",\"duration\":\"0\",\"startTime\":\"09:00:00/28.02.2025\",\"endTime\":\"09:00:00/28.02.2025\"}]";
        int actuallyStatusCode = response.statusCode();
        String actuallyBody = response.body();

        Assertions.assertEquals(expectedStatusCode, actuallyStatusCode, "Статус код должен быть 200");
        Assertions.assertEquals(expectedBody, actuallyBody, "Ответ не совпадает с ожидаемым");
    }

    @Test
    public void testGetEpicById() throws IOException, InterruptedException {
        Epic epic = new Epic(
                "Epic1",
                "Epic1 description",
                Duration.ofMinutes(0),
                LocalDateTime.of(LocalDate.of(2025, 2, 28), LocalTime.of(9, 0))
        );

        taskManager.addEpic(epic);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/epics/1"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        int expectedStatusCode = 200;
        String expectedBody = "{\"subTasksIdList\":[],\"id\":1,\"taskName\":\"Epic1\",\"description\":\"Epic1 description\",\"taskStatus\":\"NEW\",\"duration\":\"0\",\"startTime\":\"09:00:00/28.02.2025\",\"endTime\":\"09:00:00/28.02.2025\"}";
        int actuallyStatusCode = response.statusCode();
        String actuallyBody = response.body();

        Assertions.assertEquals(expectedStatusCode, actuallyStatusCode, "Статус код должен быть 200");
        Assertions.assertEquals(expectedBody, actuallyBody, "Ответ не совпадает с ожидаемым");
    }

    @Test
    public void testGetEpicSubtasks() throws IOException, InterruptedException {
        Epic epic = new Epic(
                "Epic1",
                "Epic1 description",
                Duration.ofMinutes(0),
                LocalDateTime.of(LocalDate.of(2025, 2, 28), LocalTime.of(9, 0))
        );

        taskManager.addEpic(epic);

        SubTask subTask = new SubTask(
                epic,
                "SubTask1",
                "SubTask description",
                TaskStatus.NEW,
                Duration.ofMinutes(10),
                LocalDateTime.of(LocalDate.of(2025, 2, 28), LocalTime.of(9, 0))
        );

        taskManager.addSubTask(subTask);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/epics/1/subtasks"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        int expectedStatusCode = 200;
        String expectedBody = "[{\"epicId\":1,\"id\":2,\"taskName\":\"SubTask1\",\"description\":\"SubTask description\",\"taskStatus\":\"NEW\",\"duration\":\"10\",\"startTime\":\"09:00:00/28.02.2025\",\"endTime\":\"09:10:00/28.02.2025\"}]";
        int actuallyStatusCode = response.statusCode();
        String actuallyBody = response.body();

        Assertions.assertEquals(expectedStatusCode, actuallyStatusCode, "Статус код должен быть 200");
        Assertions.assertEquals(expectedBody, actuallyBody, "Ответ не совпадает с ожидаемым");
    }

    @Test
    public void testDeleteEpicById() throws IOException, InterruptedException {
        Epic epic = new Epic(
                "Epic1",
                "Epic1 description",
                Duration.ofMinutes(0),
                LocalDateTime.of(LocalDate.of(2025, 2, 28), LocalTime.of(9, 0))
        );

        taskManager.addEpic(epic);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/epics/1"))
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        int expectedStatusCode = 200;
        int expectedEpicsSize = 0;
        int actuallyStatusCode = response.statusCode();
        int actuallyEpicsSize = taskManager.getAllTasks().size();

        Assertions.assertEquals(expectedStatusCode, actuallyStatusCode, "Статус код должен быть 200");
        Assertions.assertEquals(expectedEpicsSize, actuallyEpicsSize, "Менеджере задач не пуст");
    }

    @Test
    public void testPostSubTask() throws IOException, InterruptedException {
        Epic epic = new Epic(
                "Epic1",
                "Epic1 description",
                Duration.ofMinutes(0),
                LocalDateTime.of(LocalDate.of(2025, 2, 28), LocalTime.of(9, 0))
        );

        taskManager.addEpic(epic);

        SubTaskDto subTaskDto = new SubTaskDto(
                1,
                "Subtask1",
                "Subtask1 description",
                TaskStatus.NEW,
                Duration.ofMinutes(5),
                LocalDateTime.of(LocalDate.of(2025, 2, 28), LocalTime.of(9, 0))
        );

        String json = gson.toJson(subTaskDto);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/subtasks"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        int expectedStatusCode = 201;
        int expectedEpicsSize = 1;
        String expectedSubtaskName = "Subtask1";
        int actuallyStatusCode = response.statusCode();
        int actuallySubtasksSize = taskManager.getAllSubTasks().size();
        String actuallySubtaskName = taskManager.getAllSubTasks().getFirst().getTaskName();

        Assertions.assertEquals(expectedStatusCode, actuallyStatusCode, "Статус код должен быть 201");
        Assertions.assertEquals(expectedEpicsSize, actuallySubtasksSize, "Некорректное количество подзадач");
        Assertions.assertEquals(expectedSubtaskName, actuallySubtaskName, "Некорректное имя задачи");
    }

    @Test
    public void testUpdateSubTask() throws IOException, InterruptedException {
        Epic epic = new Epic(
                "Epic1",
                "Epic1 description",
                Duration.ofMinutes(0),
                LocalDateTime.of(LocalDate.of(2025, 2, 28), LocalTime.of(9, 0))
        );

        taskManager.addEpic(epic);

        SubTask subTask = new SubTask(
                epic,
                "Subtask1",
                "Subtask description",
                TaskStatus.NEW,
                Duration.ofMinutes(5),
                LocalDateTime.of(LocalDate.of(2025, 2, 28), LocalTime.of(9, 0))
        );

        taskManager.addSubTask(subTask);


        SubTaskDto subTaskDto = new SubTaskDto(
                1,
                "Subtask1 updated",
                "Subtask1 description",
                TaskStatus.NEW,
                Duration.ofMinutes(5),
                LocalDateTime.of(LocalDate.of(2025, 2, 28), LocalTime.of(9, 0))
        );

        String json = gson.toJson(subTaskDto);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/subtasks/2"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        int expectedStatusCode = 201;
        int expectedSubTasksSize = 1;
        String expectedSubtasksName = "Subtask1 updated";
        int actuallyStatusCode = response.statusCode();
        int actuallySubTasksSize = taskManager.getAllSubTasks().size();
        String actuallySubtaskName = taskManager.getAllSubTasks().getFirst().getTaskName();

        Assertions.assertEquals(expectedStatusCode, actuallyStatusCode, "Статус код должен быть 201");
        Assertions.assertEquals(expectedSubTasksSize, actuallySubTasksSize, "Некорректное количество подзадач");
        Assertions.assertEquals(expectedSubtasksName, actuallySubtaskName, "Некорректное имя подзадачи");
    }

    @Test
    public void testGetSubTasks() throws IOException, InterruptedException {
        Epic epic = new Epic(
                "Epic1",
                "Epic1 description",
                Duration.ofMinutes(0),
                LocalDateTime.of(LocalDate.of(2025, 2, 28), LocalTime.of(9, 0))
        );

        taskManager.addEpic(epic);

        SubTask subTask = new SubTask(
                epic,
                "Subtask1",
                "Subtask description",
                TaskStatus.NEW,
                Duration.ofMinutes(5),
                LocalDateTime.of(LocalDate.of(2025, 2, 28), LocalTime.of(9, 0))
        );

        taskManager.addSubTask(subTask);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/subtasks"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        int expectedStatusCode = 200;
        String expectedBody = "[{\"epicId\":1,\"id\":2,\"taskName\":\"Subtask1\",\"description\":\"Subtask description\",\"taskStatus\":\"NEW\",\"duration\":\"5\",\"startTime\":\"09:00:00/28.02.2025\",\"endTime\":\"09:05:00/28.02.2025\"}]";
        int actuallyStatusCode = response.statusCode();
        String actuallyBody = response.body();

        Assertions.assertEquals(expectedStatusCode, actuallyStatusCode, "Статус код должен быть 200");
        Assertions.assertEquals(expectedBody, actuallyBody, "Ответ не совпадает с ожидаемым");
    }

    @Test
    public void testGetSubTaskById() throws IOException, InterruptedException {
        Epic epic = new Epic(
                "Epic1",
                "Epic1 description",
                Duration.ofMinutes(0),
                LocalDateTime.of(LocalDate.of(2025, 2, 28), LocalTime.of(9, 0))
        );

        taskManager.addEpic(epic);

        SubTask subTask = new SubTask(
                epic,
                "Subtask1",
                "Subtask description",
                TaskStatus.NEW,
                Duration.ofMinutes(5),
                LocalDateTime.of(LocalDate.of(2025, 2, 28), LocalTime.of(9, 0))
        );

        taskManager.addSubTask(subTask);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/subtasks/2"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        int expectedStatusCode = 200;
        String expectedBody = "{\"epicId\":1,\"id\":2,\"taskName\":\"Subtask1\",\"description\":\"Subtask description\",\"taskStatus\":\"NEW\",\"duration\":\"5\",\"startTime\":\"09:00:00/28.02.2025\",\"endTime\":\"09:05:00/28.02.2025\"}";
        int actuallyStatusCode = response.statusCode();
        String actuallyBody = response.body();

        Assertions.assertEquals(expectedStatusCode, actuallyStatusCode, "Статус код должен быть 200");
        Assertions.assertEquals(expectedBody, actuallyBody, "Ответ не совпадает с ожидаемым");
    }

    @Test
    public void testDeleteSubTaskById() throws IOException, InterruptedException {
        Epic epic = new Epic(
                "Epic1",
                "Epic1 description",
                Duration.ofMinutes(0),
                LocalDateTime.of(LocalDate.of(2025, 2, 28), LocalTime.of(9, 0))
        );

        taskManager.addEpic(epic);

        SubTask subTask = new SubTask(
                epic,
                "Subtask1",
                "Subtask description",
                TaskStatus.NEW,
                Duration.ofMinutes(5),
                LocalDateTime.of(LocalDate.of(2025, 2, 28), LocalTime.of(9, 0))
        );

        taskManager.addSubTask(subTask);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/subtasks/2"))
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        int expectedStatusCode = 200;
        int expectedSubTasksSize = 0;
        int actuallyStatusCode = response.statusCode();
        int actuallySubTasksSize = taskManager.getAllSubTasks().size();

        Assertions.assertEquals(expectedStatusCode, actuallyStatusCode, "Статус код должен быть 200");
        Assertions.assertEquals(expectedSubTasksSize, actuallySubTasksSize, "Менеджере задач не пуст");
    }

    @Test
    public void testGetHistory() throws IOException, InterruptedException {
        Epic epic = new Epic(
                "Epic1",
                "Epic1 description",
                Duration.ofMinutes(0),
                LocalDateTime.of(LocalDate.of(2025, 2, 28), LocalTime.of(9, 0))
        );

        taskManager.addEpic(epic);

        SubTask subTask = new SubTask(
                epic,
                "Subtask1",
                "Subtask description",
                TaskStatus.NEW,
                Duration.ofMinutes(5),
                LocalDateTime.of(LocalDate.of(2025, 2, 28), LocalTime.of(10, 0))
        );

        taskManager.addSubTask(subTask);

        Task task = new Task(
                "Task1",
                "Task description",
                TaskStatus.NEW,
                Duration.ofMinutes(5),
                LocalDateTime.of(LocalDate.of(2025, 2, 28), LocalTime.of(9, 0))
        );

        taskManager.addTask(task);

        taskManager.getSubTaskById(2);
        taskManager.getTaskById(3);
        taskManager.getEpicById(1);
        taskManager.getSubTaskById(2);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/history"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        int expectedStatusCode = 200;
        String expectedBody = "[{\"id\":3,\"taskName\":\"Task1\",\"description\":\"Task description\",\"taskStatus\":\"NEW\",\"duration\":\"5\",\"startTime\":\"09:00:00/28.02.2025\",\"endTime\":\"09:05:00/28.02.2025\"},{\"subTasksIdList\":[2],\"id\":1,\"taskName\":\"Epic1\",\"description\":\"Epic1 description\",\"taskStatus\":\"NEW\",\"duration\":\"5\",\"startTime\":\"10:00:00/28.02.2025\",\"endTime\":\"10:05:00/28.02.2025\"},{\"epicId\":1,\"id\":2,\"taskName\":\"Subtask1\",\"description\":\"Subtask description\",\"taskStatus\":\"NEW\",\"duration\":\"5\",\"startTime\":\"10:00:00/28.02.2025\",\"endTime\":\"10:05:00/28.02.2025\"}]";
        int actuallyStatusCode = response.statusCode();
        String actuallyBody = response.body();

        Assertions.assertEquals(expectedStatusCode, actuallyStatusCode, "Статус код должен быть 200");
        Assertions.assertEquals(expectedBody, actuallyBody, "Ответ не совпадает с ожидаемым");
    }

    @Test
    public void testGetPrioritizedTasks() throws IOException, InterruptedException {
        Epic epic = new Epic(
                "Epic1",
                "Epic1 description",
                Duration.ofMinutes(0),
                LocalDateTime.of(LocalDate.of(2025, 2, 28), LocalTime.of(9, 0))
        );

        taskManager.addEpic(epic);

        SubTask subTask = new SubTask(
                epic,
                "Subtask1",
                "Subtask description",
                TaskStatus.NEW,
                Duration.ofMinutes(5),
                LocalDateTime.of(LocalDate.of(2025, 2, 28), LocalTime.of(10, 0))
        );

        taskManager.addSubTask(subTask);

        Task task = new Task(
                "Task1",
                "Task description",
                TaskStatus.NEW,
                Duration.ofMinutes(5),
                LocalDateTime.of(LocalDate.of(2025, 2, 28), LocalTime.of(8, 0))
        );

        taskManager.addTask(task);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/prioritized"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        int expectedStatusCode = 200;
        String expectedBody = "[{\"id\":3,\"taskName\":\"Task1\",\"description\":\"Task description\",\"taskStatus\":\"NEW\",\"duration\":\"5\",\"startTime\":\"08:00:00/28.02.2025\",\"endTime\":\"08:05:00/28.02.2025\"},{\"epicId\":1,\"id\":2,\"taskName\":\"Subtask1\",\"description\":\"Subtask description\",\"taskStatus\":\"NEW\",\"duration\":\"5\",\"startTime\":\"10:00:00/28.02.2025\",\"endTime\":\"10:05:00/28.02.2025\"}]";
        int actuallyStatusCode = response.statusCode();
        String actuallyBody = response.body();

        Assertions.assertEquals(expectedStatusCode, actuallyStatusCode, "Статус код должен быть 200");
        Assertions.assertEquals(expectedBody, actuallyBody, "Ответ не совпадает с ожидаемым");
    }

    @Test
    public void testShouldBe404CodeGetTaskById() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/1"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        int expectedStatusCode = 404;
        int actuallyStatusCode = response.statusCode();

        Assertions.assertEquals(expectedStatusCode, actuallyStatusCode, "Статус код должен быть 404");
    }

    @Test
    public void testShouldBe404CodeGetEpicById() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/epics/1"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        int expectedStatusCode = 404;
        int actuallyStatusCode = response.statusCode();

        Assertions.assertEquals(expectedStatusCode, actuallyStatusCode, "Статус код должен быть 404");
    }

    @Test
    public void testShouldBe404CodeGetSubTasksById() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/subtasks/1"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        int expectedStatusCode = 404;
        int actuallyStatusCode = response.statusCode();

        Assertions.assertEquals(expectedStatusCode, actuallyStatusCode, "Статус код должен быть 404");
    }

    @Test
    public void testShouldBe404CodeGetEpicSubTasksById() throws IOException, InterruptedException {
        Epic epic = new Epic(
                "Epic1",
                "Epic1 description",
                Duration.ofMinutes(0),
                LocalDateTime.of(LocalDate.of(2025, 2, 28), LocalTime.of(9, 0))
        );

        taskManager.addEpic(epic);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/epics/1/subtasks"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        int expectedStatusCode = 404;
        int actuallyStatusCode = response.statusCode();

        Assertions.assertEquals(expectedStatusCode, actuallyStatusCode, "Статус код должен быть 404");
    }

    @Test
    public void testShouldBe406CodePostTask() throws IOException, InterruptedException {
        TaskDto taskDto = new TaskDto(
                "Task1",
                "Task description",
                TaskStatus.NEW,
                Duration.ofMinutes(5),
                LocalDateTime.of(LocalDate.of(2025, 2, 28), LocalTime.of(9, 0))
        );

        String json = gson.toJson(taskDto);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        int expectedStatusCode = 406;
        int actuallyStatusCode = response.statusCode();

        Assertions.assertEquals(expectedStatusCode, actuallyStatusCode, "Статус код должен быть 406");
    }

    @Test
    public void testShouldBe406CodePostSubTasks() throws IOException, InterruptedException {
        Epic epic = new Epic(
                "Epic1",
                "Epic1 description",
                Duration.ofMinutes(0),
                LocalDateTime.of(LocalDate.of(2025, 2, 28), LocalTime.of(9, 0))
        );

        taskManager.addEpic(epic);

        SubTaskDto subTaskDto = new SubTaskDto(
                1,
                "Subtask1",
                "Subtask1 description",
                TaskStatus.NEW,
                Duration.ofMinutes(5),
                LocalDateTime.of(LocalDate.of(2025, 2, 28), LocalTime.of(9, 0))
        );

        String json = gson.toJson(subTaskDto);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/subtasks"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/subtasks"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        int expectedStatusCode = 406;
        int actuallyStatusCode = response.statusCode();

        Assertions.assertEquals(expectedStatusCode, actuallyStatusCode, "Статус код должен быть 406");
    }
}