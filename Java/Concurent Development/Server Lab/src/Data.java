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
}