import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

// Student name: Stanislav Kril
// Student number: 3133810


// Standalone class to simulate a request a record of oldest person
public class PersonRetrieveOldestClient {

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

            // send a download request for the oldest person stored on the server
            out.writeInt(3);

            // retrieve the count of people from the server
            int count = in.readInt();

            // if no oldest person is found, print a message
            if (count == 0) {
                System.out.println("no oldest person found");
            } else {
                // otherwise, read the data of the oldest person from the server and print it to the console
                Person person = new Person();
                person.readInputStream(in);
                System.out.println(person);
            }

            // close the socket after reading the oldest person's data
            socket.close();

        } catch (IOException e) {
            // if there's an error connecting to the server, print the exception message
            System.out.println(e);
        }
    }
}
