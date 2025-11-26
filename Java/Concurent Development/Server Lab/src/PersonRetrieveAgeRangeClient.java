import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class PersonRetrieveAgeRangeClient {
    final static int portPerson = 1235;

    public static void main(String[] args) {
        try {
            Socket socket;

            socket = new Socket(InetAddress.getLocalHost(), portPerson);

            DataInputStream in = new DataInputStream(socket.getInputStream());

            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            out.writeInt(4);// send download option

            int mininumRequiredAge = 20;
            int maximumAge = 40;

            out.writeInt(mininumRequiredAge);
            out.writeInt(maximumAge);

            int count = in.readInt();

            if (count == 0) {
                System.out.println("No peron found in this age category");
            } else {
                for (int i = 0; i < count; i++) {
                    Person person = new Person();
                    person.readInputStream(in);
                    System.out.println(person);
                }
            }
            socket.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
