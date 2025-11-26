import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

// Student name: Stanislav Kril
// Student number: 3133810

// Standalone class to simulate a request of clients in specific age range
public class PersonRetrieveAgeRangeClient {

    // dedicated port
    final static int portPerson = 1235;

    public static void main(String[] args) {
        try {
            // create a new socket to connect to the server
            Socket socket;
            socket = new Socket(InetAddress.getLocalHost(), portPerson);

            // get an input stream for reading data from the socket
            DataInputStream in = new DataInputStream(socket.getInputStream());

            // get an output stream for writing data to the socket
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            // send a download request for all entities in specif age gap to the server
            out.writeInt(4);

            // declare age range to target records in data set
            int mininumRequiredAge = 20;
            int maximumAge = 40;

            // send the age range to the server
            out.writeInt(mininumRequiredAge);
            out.writeInt(maximumAge);

            // read number of people found in given age category
            int count = in.readInt();

            // if no one is found, print a message and close the socket
            if (count == 0) {
                System.out.println("No peron found in this age category");
            } else {
                // if found, read each person's data from the server and print it to the console
                for (int i = 0; i < count; i++) {
                    Person person = new Person();
                    person.readInputStream(in);
                    System.out.println(person);
                }
            }
            // close the socket when connection no longer needed
            socket.close();

        } catch (IOException e) {
            // if there's an error connecting to the server, print the exception message
            System.out.println(e);
        }
    }
}
