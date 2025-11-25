import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Data {
    private ArrayList<Person> data = new ArrayList<Person>();
    private Lock lock = new ReentrantLock();

    void add(Person person) {
        lock.lock();
        try {
            data.add(person);
        } finally {
            lock.unlock();
        }
    }

    boolean search(Person person) {
        lock.lock();

        try {
            return data.contains(person);
        } finally {
            lock.unlock();
        }
    }

    // Method to download the data set based on specific person last name
    ArrayList<Person> retrieve(String lastName) {
        lock.lock();

        try {
            ArrayList<Person> dataSet = new ArrayList<Person>();

            Person p = new Person("", lastName, 0); //use for search

            for (int j = 0; j < data.size(); j++) {
                Person person1 = data.get(j);

                if (person1.equals(p)) {
                    dataSet.add(person1);
                }
            }
            return dataSet;
        } finally {
            lock.unlock();
        }
    }

    // Method to download entire data set
    ArrayList<Person> retrieve() {
        lock.lock();

        try {
            ArrayList<Person> dataSet = new ArrayList<Person>();

            dataSet.addAll(data);

            return dataSet;

        } finally {
            lock.unlock();
        }
    }

    Person retrieveOldest() {
        lock.lock();
        try {
            int maxAge = Integer.MIN_VALUE;
            Person oldestPerson = null;

            for (Person person : data) {

                int currentAge = person.getAge();

                if (currentAge > maxAge) {
                    maxAge = currentAge;
                    oldestPerson = person;

                }
            }

            return oldestPerson;
        } finally {
            lock.unlock();
        }
    }

    // Method to download date set with person in specific range of people
    ArrayList<Person> retrieve(int min, int max) {
        lock.lock();

        try {
            ArrayList<Person> dataSet = new ArrayList<Person>();

            for (int j = 0; j < data.size(); j++) {
                Person currentPerson = data.get(j);

                if (currentPerson.getAge() >= min && currentPerson.getAge() <= max) {
                    dataSet.add(currentPerson);
                }
            }
            return dataSet;
        } finally {
            lock.unlock();
        }
    }
}