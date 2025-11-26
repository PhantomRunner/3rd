import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

// Student name: Stanislav Kril
// Student number: 3133810


// Standalone class to simulate a request of all clients stored on server
public class PersonRetrieveAllClient {

    // dedicated port for communication with the server
    final static int portPerson = 1235;

    public static void main(String[] args) {
        try {
            // create a new socket to connect to the server at the specified port
            Socket socket;
            socket = new Socket(InetAddress.getLocalHost(), portPerson);

            // get an input stream for reading data from the socket
            DataInputStream in = new DataInputStream(socket.getInputStream());

            // get an output stream for writing data to the socket
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            // send a download request for all clients stored on server
            out.writeInt(2);

            // read the number of founded records from the server
            int count = in.readInt();

            // if no matches are found, print a warning
            if (count == 0) {
                System.out.println("no matches found");

            } else {
                // if found, read each match's data from the server and print it to the console
                for (int i = 0; i < count; i++) {
                    Person person = new Person();
                    person.readInputStream(in);
                    System.out.println(person);
                }
            }

            // close the socket when we're done with it
            socket.close();

        } catch (IOException e) {
            // if there's an error connecting to the server, print the exception message
            System.out.println(e);
        }

    }
}
