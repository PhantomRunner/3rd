import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;


// Class who simulates an upload action on the server
public class UploadDataOnServer {
    final static int PORT = 1234;

    public static void main(String[] args) {
    write(new Car("1","Audi",2588,890,true));
    }

    static void write(Car carOject) {
        try {
            // create a new socket connection to the server on PORT 1235
            Socket socket;
            socket = new Socket(InetAddress.getLocalHost(), PORT);

            // get input and output streams for communication with the server
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            // send upload option to server
            out.writeInt(0);

            // send person object data to server using output stream
            carOject.writeOutputStream(out);

            // read confirmation from server (boolean indicating success or failure)
            boolean confirmation = in.readBoolean();

            // print result of upload operation
            if (confirmation) {
                System.out.println("Uploaded: " + carOject.toString());
            } else {
                System.out.println("Error during upload");
            }

            // close socket connection to server
            socket.close();

        } catch (IOException e) {
            // handle any I/O exceptions that occur during upload
            System.out.println(e);
        }
    }
}

