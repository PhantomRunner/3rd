import java.util.Random;


// CoinTosser class simulates flipping a coin 1000 times and keeps track of heads and tails.
public class CoinTosser extends Thread {
    // Number of head and tails thrown
    private int head;
    private int tail;


    @Override
    public void run() {
        try {
            // Create a new Random instance to generate random numbers for coin flips
            Random rand = new Random();

            // Simulate 1000 coin flips
            for (int flip = 0; flip < 1000; flip++) {

                // Generate a random number between 0 and 1, if it's 0, record as head, otherwise tail
                if (rand.nextInt(2) == 0) {

                    head++; // increase counter to keep record
                } else {
                    tail++; // increase counter to keep record
                }
            }
            // Print out the results
            System.out.println("Number of tails: " + tail + "\n" + "Number of heads: " + head);

        } catch (Exception e) {
            throw new RuntimeException(e); // Rethrow any exceptions encountered during simulation
        }
    }


}
