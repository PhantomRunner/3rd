import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

class PersonUpDown extends Thread {
    Socket socket;
    Data data;

    PersonUpDown(Socket socket, Data data) {
        this.socket = socket;
        this.data = data;
    }

    public void run() {
        try {
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            int option = in.readInt();

            if (option == 0) { //upload

                Person matches = new Person();

                matches.readInputStream(in);
                data.add(matches);

                out.writeBoolean(true);

                socket.close();

            } else { //download

                String lastName = in.readUTF();

                ArrayList<Person> personList = data.retrieve(lastName);

                out.writeInt(personList.size());

                for (int j = 0; j < personList.size(); j++) {
                    Person person = personList.get(j);
                    person.writeOutputStream(out);
                }
                socket.close();
            }
        } catch (IOException e) {
        }
    }
}