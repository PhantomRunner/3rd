import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

class PersonUpDown extends Thread {
    Socket socket;
    Data data;

    PersonUpDown(Socket s, Data d) {
        socket = s;
        data = d;
    }

    public void run() {
        try {
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            int opt = in.readInt();

            if (opt == 0) { //upload

                Person p = new Person();
                p.readInputStream(in);
                data.add(p);
                out.writeBoolean(true);
                socket.close();

            } else { //download

                String sname = in.readUTF();
                ArrayList<Person> lst = data.retrieve(sname);
                out.writeInt(lst.size());

                for (int j = 0; j < lst.size(); j++) {
                    Person p = lst.get(j);
                    p.writeOutputStream(out);
                }
                socket.close();
            }
        } catch (IOException e) {
        }
    }
}