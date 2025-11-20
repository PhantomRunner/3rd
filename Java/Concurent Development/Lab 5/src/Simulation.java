// NAME: Stanislav Kril
// STUDENT NUMBER: 3133810

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Simulation {
    public static void main(String[] args) {

        // Create a new viewing stand object with 10 seats
        ViewingStand stand = new ViewingStand(10);

        // Create an executor service to manage threads, using the number of available processors as the thread pool
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        // Simulate 20 visitors
        for (int i = 0; i < 20; i++) {

            final int visitorId = i;

            executor.execute(() -> {
                // Initial flag for seats, the inial state is that evey seat is free
                int seat = -1;

                // Search for an available seat
                while (seat == -1) {

                    // Attempt to find a vacant seat in the viewing stand
                    seat = stand.findSeat();

                    // If no vacant seats are available, wait for 1 second and try again
                    if (seat == -1) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }

                }

                // Simulate viewing time by pausing execution for 1 second
                try {
                    System.out.println("Visitor " + visitorId + " is viewing the painting...");

                    // Pause execution for 1 second to simulate viewing time
                    Thread.sleep(1000);

                } catch (InterruptedException e) {
                    // If interrupted, cancel the task and return from the current thread
                    Thread.currentThread().interrupt();
                }

                // Once finished viewing, leave the seat and finish the task
                stand.leaveSeat(seat);
                System.out.println("Visitor " + visitorId + " left the museum.");
            });
        }
        // Shut down the executor service once all tasks have been completed
        executor.shutdown();
    }
}

