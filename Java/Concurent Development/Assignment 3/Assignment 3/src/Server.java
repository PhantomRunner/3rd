import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// Class who simulates the server
public class Server {
     static final int PORT = 1234;
     static CarDatabase carDatabase;;

    public static void main(String[] args) {

        System.out.println("Server starting...");

        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            carDatabase = new CarDatabase();
            ExecutorService threadPool = Executors.newFixedThreadPool(50);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                ClientHandler handler = new ClientHandler(clientSocket, carDatabase);
                threadPool.submit(handler);


            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
