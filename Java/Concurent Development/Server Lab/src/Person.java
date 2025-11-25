import java.io.*;

final public class Person {
    private String firstName;
    private String lastName;
    private int age;

    public Person(String firstName, String lastName, int age) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }

    public Person() {
        firstName = null;
        lastName = null;
        age = 0;
    }

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

    public boolean equals(Object object) { //equality based on surname only
        if (!(object instanceof Person)) {
            return false;
        }
        Person person = (Person) object;
        return lastName.equals(person.lastName);
    }

    //======================================================
    //Methods used to read and write to streams over sockets
    public void writeOutputStream(DataOutputStream out) {
        try {
            out.writeUTF(firstName);
            out.writeUTF(lastName);
            out.writeInt(age);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readInputStream(DataInputStream in) {
        try {
            firstName = in.readUTF();
            lastName = in.readUTF();
            age = in.readInt();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}