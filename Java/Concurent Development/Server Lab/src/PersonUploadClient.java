import java.io.*;
import java.net.*;

// Student name: Stanislav Kril
// Student number: 3133810

// Standalone class to upload the data to the server
public class PersonUploadClient {

    // dedicated port for communication with the server
    final static int portPerson = 1235;

    public static void main(String[] args) {
        // create and upload a list of person objects to the server
        write(new Person("James", "Joyce", 50));
        write(new Person("Nora", "Joyce", 40));
        write(new Person("Stan", "Joyce", 60));
        write(new Person("Sam", "Beckett", 40));
        write(new Person("Lucia", "Joyce", 10));
    }

    // method to upload a single person object to the server
    static void write(Person personObject) {
        try {
            // create a new socket connection to the server on port 1235
            Socket socket;
            socket = new Socket(InetAddress.getLocalHost(), portPerson);

            // get input and output streams for communication with the server
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            // send upload option to server
            out.writeInt(0);

            // send person object data to server using output stream
            personObject.writeOutputStream(out);

            // read confirmation from server (boolean indicating success or failure)
            boolean ok = in.readBoolean();

            // print result of upload operation
            if (!ok) {
                System.out.println("Error");
            } else {
                System.out.println("Uploaded: " + personObject);
            }

            // close socket connection to server
            socket.close();
        } catch (IOException e) {
            // handle any I/O exceptions that occur during upload
            System.out.println(e);
        }
    }
}
