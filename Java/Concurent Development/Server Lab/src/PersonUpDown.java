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

            } else if (option == 1) { //download by last name

                String lastName = in.readUTF();

                ArrayList<Person> personList = data.retrieve(lastName);

                out.writeInt(personList.size());

                for (int j = 0; j < personList.size(); j++) {
                    Person person = personList.get(j);
                    person.writeOutputStream(out);
                }
                socket.close();

            } else if (option == 2) { // download all data from the data set

                ArrayList<Person> personList = data.retrieve();
                out.writeInt(personList.size());

                for (int j = 0; j < personList.size(); j++) {
                    Person person = personList.get(j);
                    person.writeOutputStream(out);
                }

                socket.close();

            } else if (option == 3) { // download only oldest person in the data set

               Person person = data.retrieveOldest();

               if (person == null) {
                   out.writeInt(0);
               }else {
                   out.writeInt(1);
                   person.writeOutputStream(out);
               }

                socket.close();

            } else if (option == 4) { // download data set of people in given age gap
                int minimumRequiredAge = in.readInt();
                int maximumRequiredAge = in.readInt();
                ArrayList<Person> personList = data.retrieve(minimumRequiredAge,maximumRequiredAge);

                out.writeInt(personList.size());

                for (int j = 0; j < personList.size(); j++) {
                    Person person = personList.get(j);
                    person.writeOutputStream(out);
                }

                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}