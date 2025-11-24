import java.io.*;
import java.net.*;
import java.util.*;

public class PersonUploadClient {
    final static int portPerson = 1235;

    public static void main(String[] args) {
        write(new Person("James", "Joyce", 50));
        write(new Person("Nora", "Joyce", 40));
        write(new Person("Stan", "Joyce", 60));
        write(new Person("Sam", "Beckett", 40));
        write(new Person("Lucia", "Joyce", 10));
    }

    static void write(Person p) {
        try {
            Socket socket;

            socket = new Socket(InetAddress.getLocalHost(), portPerson);

            DataInputStream in = new DataInputStream(socket.getInputStream());

            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            out.writeInt(0); //send upload option
            p.writeOutputStream(out);

            boolean ok = in.readBoolean();

            if (!ok) {
                System.out.println("Error");
            }
            socket.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
