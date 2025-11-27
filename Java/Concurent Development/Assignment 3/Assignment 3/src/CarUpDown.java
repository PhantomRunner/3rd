import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


// Class simulating a thread,
public class CarUpDown extends Thread {
    Socket socket;

    Data data;

    public CarUpDown(Socket socket, Data data) {
        this.socket = socket;
        this.data = data;
    }

    public void run() {
        try {
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            int option = in.readInt();

            if (option == 0) {
                Car carToUpload = new Car();
                carToUpload.readInputStream(in);

                data.add(carToUpload);

                // return confirmation
                out.writeBoolean(true);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
