import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Data {
    private ArrayList<Person> data = new ArrayList<Person>();
    private Lock lock = new ReentrantLock();

    void add(Person p) {
        lock.lock();
        try {
            data.add(p);
        } finally {
            lock.unlock();
        }
    }

    boolean search(Person p) {
        lock.lock();
        try {
            return data.contains(p);
        } finally {
            lock.unlock();
        }
    }

    ArrayList<Person> retrieve(String sname) {
        lock.lock();
        try {
            ArrayList<Person> dt = new ArrayList<Person>();

            Person p = new Person("", sname, 0); //use for search

            for (int j = 0; j < data.size(); j++) {
                Person p1 = data.get(j);
                if (p1.equals(p)) dt.add(p1);
            }
            return dt;
        } finally {
            lock.unlock();
        }
    }
}