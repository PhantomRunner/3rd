import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// Simulates the server that handles multiple clients
public class Server {

    static final int PORT = 1234;       // Port number for client connections
    static CarDatabase carDatabase;      // Shared database for all clients

    public static void main(String[] args) {

        System.out.println("Server starting...");

        try {
            // Create server socket to listen for client connections
            ServerSocket serverSocket = new ServerSocket(PORT);

            // Initialize shared car database
            carDatabase = new CarDatabase();

            // Thread pool to handle multiple clients concurrently
            ExecutorService threadPool = Executors.newFixedThreadPool(50);

            // Continuously accept client connections
            while (true) {
                Socket clientSocket = serverSocket.accept();

                // Create a handler for the new client
                ClientHandler handler = new ClientHandler(clientSocket, carDatabase);

                // Submit handler to thread pool
                threadPool.submit(handler);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
