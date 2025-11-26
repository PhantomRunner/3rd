import java.io.*;
import java.net.*;
import java.util.*;

// Student name: Stanislav Kril
// Student number: 3133810

// Represents a data package that can be sent over a network.
public final class DataPackage {
    // assumes that it is on a local host
    // otherwise need the ip address as well
    private final int port; // The port to connect to


    // list of integers representing the data in this package.
    private final ArrayList<Integer> data = new ArrayList<Integer>();

    // constructor for a DataPackage object.
    DataPackage(int p) {
        port = p;
    }

    // adds an integer to the data in this package.
    void add(Integer k) {
        data.add(k);
    }


    // writes the data in this package to a client at the specified port.
    void writeClient() {
        try {
            // create a new socket to connect to the server
            Socket socket;
            socket = new Socket(InetAddress.getLocalHost(), port);

            // get an output stream for writing data to the socket
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            // write the size of the data to the client
            out.writeInt(data.size());

            // write each integer in the data list to the client
            for (Integer k : data) {
                out.writeInt(k);
            }

            // flush the output stream to ensure all data is written
            out.flush();

            // close the socket when we're done with it
            socket.close();
        } catch (IOException e) {
            // if there's an error sending the data, print an error message
            System.out.println("Error writing data package: ");
            System.out.println(e);
        }
    }
}