import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;


// Class to store data on server
public class Data {

    private ArrayList<Car> data = new ArrayList<Car>();

    private ReentrantLock lock = new ReentrantLock();

    void add(Car car) {
        lock.lock();
        try {
            data.add(car);
        } finally {
            lock.unlock();
        }
    }



}
