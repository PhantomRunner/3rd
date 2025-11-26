import java.io.*;

// Student name: Stanislav Kril
// Student number: 3133810

// Person class represent the person object and its params to stored on server
final public class Person {
    private String firstName;
    private String lastName;
    private int age;

    // Constructor. Take the first and last name with age
    public Person(String firstName, String lastName, int age) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }

    // Overload constructor. Take no arguments and create an empty entity
    public Person() {
        firstName = null;
        lastName = null;
        age = 0;
    }

    // Getters
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public int getAge() {
        return age;
    }

    public String toString() {
        return lastName + " " + firstName + " " + age;
    }

    // Method to determine equality based on last name
    public boolean equals(Object object) {
        if (!(object instanceof Person)) {
            return false;
        }
        Person person = (Person) object;
        return lastName.equals(person.lastName);
    }

    // Method to write into the streams over sockets
    public void writeOutputStream(DataOutputStream out) {
        try {
            // write each of the params to the output stream
            out.writeUTF(firstName);
            out.writeUTF(lastName);
            out.writeInt(age);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to read from the streams over sockets
    public void readInputStream(DataInputStream in) {
        try {
            // read each of the params from the input stream
            firstName = in.readUTF();
            lastName = in.readUTF();
            age = in.readInt();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}