import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

// Student name: Stanislav Kril
// Student number: 3133810

class Data {
    // Original array list that hold all data stored on server
    private ArrayList<Person> data = new ArrayList<Person>();

    // lock to secure critical zone
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
        // lock the critical zone
        lock.lock();

        try {
            // list for storing person objects
            ArrayList<Person> dataSet = new ArrayList<Person>();

            // new person object with specif last name to search for matches in data set
            Person p = new Person("", lastName, 0);

            // loop through the original data
            for (int j = 0; j < data.size(); j++) {

                // get the current person object
                Person person1 = data.get(j);

                // if it matches => add to the new list copy
                if (person1.equals(p)) {
                    dataSet.add(person1);
                }
            }

            // return a copy of a new list
            return dataSet;

        } finally {
            // release the lock
            lock.unlock();
        }
    }

    // Method to download entire data set
    ArrayList<Person> retrieve() {
        // lock critical zone before accessing it
        lock.lock();

        try {
            // new list to return results
            ArrayList<Person> dataSet = new ArrayList<Person>();

            // add all data stored on server to the list
            dataSet.addAll(data);

            // return results
            return dataSet;

        } finally {
            // release the lock
            lock.unlock();
        }
    }

    Person retrieveOldest() {
        // lock before accessing critical zone
        lock.lock();

        try {
            // set variable to track maximum age of founded object
            int maxAge = Integer.MIN_VALUE;
            Person oldestPerson = null;

            // Loop through the list of data
            for (Person person : data) {

                // get current age
                int currentAge = person.getAge();

                // if current age bigger then maximum => update maximum age and flag that person object as oldest
                if (currentAge > maxAge) {
                    maxAge = currentAge;
                    oldestPerson = person;
                }
            }

            // return oldest found object
            return oldestPerson;

        } finally {
            // release the lock
            lock.unlock();
        }
    }

    // Method to download date set with person in specific range of people
    ArrayList<Person> retrieve(int min, int max) {
        // lock critical zone
        lock.lock();

        try {
            // new list to return to the client
            ArrayList<Person> dataSet = new ArrayList<Person>();

            // loop through the data
            for (int j = 0; j < data.size(); j++) {
                // get current person object
                Person currentPerson = data.get(j);

                // compare current object to the max and min age gap, if it fits => add to the list to be returned
                if (currentPerson.getAge() >= min && currentPerson.getAge() <= max) {
                    dataSet.add(currentPerson);
                }
            }
            // return the list to client
            return dataSet;

        } finally {
            // release the lock
            lock.unlock();
        }
    }
}