import manager.Managers;
import server.HttpTaskServer;

import java.io.IOException;

public class JavaKanban {
    private static final int PORT = 8080;
    private static final String HOST = "localhost";

    public static void main(String[] args) throws IOException {

        HttpTaskServer httpTaskServer = new HttpTaskServer(PORT, HOST, Managers.getDefault());
        httpTaskServer.start();
    }
}
