import java.net.ServerSocket;
import java.net.Socket;

// Class who simulates the server
public class Server {

    final static int PORT = 1234;

    public static void main(String[] args) {

        System.out.println("Server starting...");

        Data data = new Data();

        try {
            ServerSocket serverSocket = new ServerSocket(PORT);

            while (true) {
                Socket socket = serverSocket.accept();
                new CarUpDown(socket, data).start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
