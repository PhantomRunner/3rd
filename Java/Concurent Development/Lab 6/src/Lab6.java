import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

// Student Name - Stanislav Kril
// Student Number - 3133810

public class Lab6 {

    // Constant number of threads to be created
    private static final int THREAD_NUMBER = 10;

    // Current turn
    private static int currentTurn = 0;

    // Lock object for synchronization
    static Lock lock = new ReentrantLock();

    // Condition object for thread signaling
    static Condition condition = lock.newCondition();

    public static void main(String[] args) {
        // Create and start a thread for each index from 0 to THREAD_NUMBER - 1
        for (int i = 0; i < THREAD_NUMBER; i++) {
            Thread thread = new Worker(i);
            thread.start();
        }
    }

    static class Worker extends Thread {

        // The index of this worker thread
        private int threadOrder;

        // Constructor for the Worker class
        public Worker(int number) {
            this.threadOrder = number;
        }

        public void run() {
            // Acquire the lock before entering critical section
            lock.lock();

            try {
                // Wait for thread turn (i.e., currentTurn == threadOrder)
                while (threadOrder != currentTurn) {
                    // Pause execution and wait for a signal from another thread
                    condition.await();
                }

                // Print a message indicating the current turn
                System.out.println("Currently running " + threadOrder + " thread");

                // Increment the current turn index so put in motion other threads
                currentTurn++;

                // Signal all waiting threads that a new turn has started
                condition.signalAll();
                
            } catch (Exception e) {
                // Handle any exceptions thrown by the critical section code
                e.printStackTrace();
            } finally {
                // Release the lock after executing the critical section
                lock.unlock();
            }
        }
    }
}