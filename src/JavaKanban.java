import manager.Managers;
import server.HttpTaskServer;

import java.io.IOException;

public class JavaKanban {
    public static void main(String[] args) throws IOException {
        HttpTaskServer httpTaskServer = new HttpTaskServer(8080, "localhost", Managers.getDefault());
        httpTaskServer.start();
    }
}
