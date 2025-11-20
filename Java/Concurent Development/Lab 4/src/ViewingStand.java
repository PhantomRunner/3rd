// NAME: Stanislav Kril
// STUDENT NUMBER: 3133810
public class ViewingStand {

    // Array to keep track of seat occupancy, where true means occupied and false means free
    private boolean[] seats;

    public ViewingStand(int numberOfSeats) {
        // Initialize the seats array with the given number of seats
        this.seats = new boolean[numberOfSeats];
    }

    // Attempts to find an available seat and returns its index.
    // If all seats are occupied, returns -1.
    public synchronized int findSeat() {

        // Iterate through each seat in the viewing stand
        for (int i = 0; i < seats.length; i++) {

            // Check if the current seat is vacant
            if (!seats[i]) {

                // Mark the seat as occupied and print a message to indicate that it's no longer available
                seats[i] = true;
                System.out.println("Seat " + i + " is already in use");
                return i; // Return the index of the found seat
            }
        }
        // If all seats are occupied, return -1 to indicate failure
        return -1;
    }

    // Releases occupied seat
    public synchronized void leaveSeat(int seatIndex) {

        // Validate the provided seat index
        if (seatIndex >= 0 && seatIndex < seats.length && seats[seatIndex]) {
            // Free the seat
            seats[seatIndex] = false;
            System.out.println("Seat " + seatIndex + " now free");
        }

    }

}
