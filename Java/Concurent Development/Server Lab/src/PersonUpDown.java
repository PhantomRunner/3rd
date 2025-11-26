import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

// Student name: Stanislav Kril
// Student number: 3133810

// Class represents a thread that handles communication between the server and clients.
class PersonUpDown extends Thread {

    // socket for communication with the client
    Socket socket;

    // data set to interact with
    Data data;

    // constructor to initialize the thread with a socket and data set
    PersonUpDown(Socket socket, Data data) {
        this.socket = socket;
        this.data = data;
    }

    public void run() {
        try {
            // get input/output streams for communication with the client
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            // read option sent by the client
            int option = in.readInt();

            if (option == 0) { // upload data to the server

                // create a person object from input stream
                Person matches = new Person();
                matches.readInputStream(in);

                // add person to data set
                data.add(matches);

                // send confirmation to client
                out.writeBoolean(true);

            } else if (option == 1) { // download by last name

                // read last name from input stream
                String lastName = in.readUTF();

                // retrieve list of people with matching last name
                ArrayList<Person> personList = data.retrieve(lastName);

                // send size of list to client
                out.writeInt(personList.size());

                // send each person's data to client
                for (int j = 0; j < personList.size(); j++) {
                    Person person = personList.get(j);
                    person.writeOutputStream(out);
                }

            } else if (option == 2) { // download all data from the server

                // retrieve list of all people in data set
                ArrayList<Person> personList = data.retrieve();

                // send size of list to client
                out.writeInt(personList.size());

                // send each person's data to client
                for (int j = 0; j < personList.size(); j++) {
                    Person person = personList.get(j);
                    person.writeOutputStream(out);
                }

            } else if (option == 3) { // download only oldest person in the data set

                // retrieve oldest person from data set
                Person person = data.retrieveOldest();

                // send confirmation to client if person is found, otherwise send size 0
                if (person == null) {
                    out.writeInt(0);
                } else {
                    out.writeInt(1);
                    person.writeOutputStream(out);
                }

            } else if (option == 4) { // download data set of people in given age gap

                // read minimum and maximum required ages from input stream
                int minimumRequiredAge = in.readInt();
                int maximumRequiredAge = in.readInt();

                // retrieve list of people within the specified age range
                ArrayList<Person> personList = data.retrieve(minimumRequiredAge, maximumRequiredAge);

                // send size of list to client
                out.writeInt(personList.size());

                // send each person's data to client
                for (int j = 0; j < personList.size(); j++) {
                    Person person = personList.get(j);
                    person.writeOutputStream(out);
                }
            }

            System.out.println("Received option " + option + " from client " + socket.getInetAddress());
        } catch (IOException e) {
            // print any error messages
            e.printStackTrace();
        }
    }
}