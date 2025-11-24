import java.io.*;

final public class Person {
    private String firstName;
    private String secondName;
    private int age;

    public Person(String fn, String sn, int a) {
        firstName = fn;
        secondName = sn;
        age = a;
    }

    public Person() {
        firstName = null;
        secondName = null;
        age = 0;
    }

    public String fName() {
        return firstName;
    }

    public String sName() {
        return secondName;
    }

    public int age() {
        return age;
    }

    public String toString() {
        return secondName + " " + firstName + " " + age;
    }

    public boolean equals(Object ob) { //equality based on surname only
        if (!(ob instanceof Person)) {
            return false;
        }
        Person p = (Person) ob;
        return secondName.equals(p.secondName);
    }

    //======================================================
    //Methods used to read and write to streams over sockets
    public void writeOutputStream(DataOutputStream out) {
        try {
            out.writeUTF(firstName);
            out.writeUTF(secondName);
            out.writeInt(age);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readInputStream(DataInputStream in) {
        try {
            firstName = in.readUTF();
            secondName = in.readUTF();
            age = in.readInt();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}