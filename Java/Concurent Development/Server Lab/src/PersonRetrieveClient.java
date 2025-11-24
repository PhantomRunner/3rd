import java.io.*;
import java.net.*;
import java.util.*;

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

            int k = in.readInt(); //retrieve number of matches

            if (k == 0) {
                System.out.println("No matches found");
            } else {
                Person p = new Person();
                for (int j = 0; j < k; j++) {
                    p.readInputStream(in);
                    System.out.println(p);
                }
                socket.close();
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
