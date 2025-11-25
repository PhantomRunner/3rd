import java.io.*;
import java.net.*;


public class PersonRetrieveClient {
    final static int portPerson = 1235;

    public static void main(String[] args) {
        try {

            Socket socket;

            socket = new Socket(InetAddress.getLocalHost(), portPerson);

            DataInputStream in = new DataInputStream(socket.getInputStream());

            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            out.writeInt(1); // send download option
            out.writeUTF("Joyce");

            int matches = in.readInt(); //retrieve number of matches

            if (matches == 0) {
                System.out.println("No matches found");
            } else {
                Person person = new Person();

                for (int j = 0; j < matches; j++) {
                    person.readInputStream(in);
                    System.out.println(person);
                }

                socket.close();
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
