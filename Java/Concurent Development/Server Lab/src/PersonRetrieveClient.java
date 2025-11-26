import java.io.*;
import java.net.*;

// Student name: stanislav kril
// Student number: 3133810

// Standalone class to simulate a request of clients with specific last name
public class PersonRetrieveClient {

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

            // send a download request to target all records with 'joyce' last name
            out.writeInt(1);
            out.writeUTF("joyce");

            // retrieve the number of matches from the server
            int matches = in.readInt();

            // if no matches are found, print a message
            if (matches == 0) {
                System.out.println("no matches found");
            } else {
                // if found, read each match's data from the server and print it to the console
                Person person = new Person();
                for (int j = 0; j < matches; j++) {
                    person.readInputStream(in);
                    System.out.println(person);
                }
                // close the socket after reading all matches
                socket.close();
            }

        } catch (IOException e) {
            // if there's an error connecting to the server, print the exception message
            System.out.println(e);
        }
    }
}