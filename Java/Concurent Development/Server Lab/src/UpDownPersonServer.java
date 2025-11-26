import java.io.*;
import java.net.*;

// Student name: Stanislav Kril
// Student number: 3133810


// Class simulate networked server that accepts incoming connections on a specified port.
public class UpDownPersonServer {

    // dedicated port for communication with clients
    final static int portPerson = 1235; // any number greater than 1024

    public static void main(String[] args) {
        System.out.println("Server running...");

        // create a new data set to store and manage person objects
        Data data = new Data();

        try {
            // create a server socket on the designated port
            ServerSocket serverSocket = new ServerSocket(portPerson);

            // enter infinite loop to continuously accept incoming connections
            while (true) {
                // wait for a service request from a client on the specified port
                Socket socket = serverSocket.accept();

                // start a new thread to handle the incoming request and process it
                new PersonUpDown(socket, data).start();
            }
        } catch (IOException e) {
            // print any error messages that occur during server initialization or operation
            e.printStackTrace();
        }
    }
}



