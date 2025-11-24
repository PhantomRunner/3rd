import java.io.*;
import java.net.*;
import java.util.*;

public final class DataPackage {
    // assumes that it is on a local host
    // otherwise need the ip address as well
    private final int port;
    private final ArrayList<Integer> data = new ArrayList<Integer>();

    DataPackage(int p) {
        port = p;
    }

    void add(Integer k) {
        data.add(k);
    }

    void writeClient() {
        try {
            Socket socket;

            socket = new Socket(InetAddress.getLocalHost(), port);

            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            out.writeInt(data.size());

            for (Integer k : data) {
                out.writeInt(k);
            }

            out.flush();

            socket.close();

        } catch (IOException e) {
            System.out.println("Package");
            System.out.println(e);
        }
    }
}