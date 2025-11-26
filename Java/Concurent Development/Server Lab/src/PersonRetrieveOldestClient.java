import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class PersonRetrieveOldestClient {
    final static int portPerson = 1235;

    public static void main(String[] args) {
        try {
            Socket socket;

            socket = new Socket(InetAddress.getLocalHost(), portPerson);

            DataInputStream in = new DataInputStream(socket.getInputStream());

            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            out.writeInt(3); // send download option

            int count = in.readInt();

            if (count == 0) {
                System.out.println("No oldest person found");
            } else {
                Person person = new Person();
                person.readInputStream(in);
                System.out.println(person);
            }
            socket.close();
        } catch (IOException e) {
            System.out.println(e);
        }

    }

}
