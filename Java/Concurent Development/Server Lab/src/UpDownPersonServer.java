import java.io.*;
import java.net.*;

public class UpDownPersonServer {
    final static int portPerson = 1235; // any number > 1024

    public static void main(String[] args) {

        System.out.println("Server running...");
        Data data = new Data();

        try {
            ServerSocket serverSocket = new ServerSocket(portPerson);

            while (true) {

                // wait for a service request on port portSqrt
                Socket socket = serverSocket.accept();

                // start thread to service request
                new PersonUpDown(socket, data).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



