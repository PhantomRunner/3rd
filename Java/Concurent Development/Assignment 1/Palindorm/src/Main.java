// Student Name - Stanislav Kril
// Student number - 3133810

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Main {

    // Constants for matrix dimensions
    static final int ROWS = 1000;
    static final int COLS = 1000;

    // Constants for palindrome search parameters
    static final int minPalindromeLength = 3; // Minimum length of a palindrome
    static final int maxPalindromeLength = 6; // Maximum length of a palindrome

    // Matrix data, initialized with random chars
    static char[][] data = new char[ROWS][COLS];

    // Run the simulation
    public static void main(String[] args) throws InterruptedException, ExecutionException {

        // Call Generate matrix method first
        generateMatrix();

        // Get the number of available CPU threads and create a fixed thread pool
        int threads = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(threads);

        // Create a list to store the futures returned by each task
        List<Future<Result>> futures = new ArrayList<>();

        // Iterate over the palindrome lengths from min to max and submit tasks to search for palindromes of that length
        for (int length = minPalindromeLength; length <= maxPalindromeLength; length++) {

            final int len = length;
            // Submit a task to search for palindromes of length "len" using the searchPalindromesOfLength() method
            futures.add(executor.submit(() -> searchPalindromesOfLength(len)));
        }

        // Iterate over the tasks and print the results
        for (Future<Result> f : futures) {
            Result r = f.get();
            System.out.println(r.getCount() + " palindromes of size " + r.getLength() + " found in " + r.getTimeSeconds()
                    + "s" + " using " + threads + " number of threads");
        }

        // Shut down the executor service
        executor.shutdown();
    }


     // Generates a matrix with random characters.
    public static void generateMatrix() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                // Fill the matrix with random characters
                data[i][j] = (char) ('a' + Math.random() * 26);
            }
        }
    }

    // Checks if a string is a palindrome.
    // String param is actual string to check if it's a palindrome.
    // Return true if is palindrome false otherwise
    public static boolean isPalindrome(String string) {
        int number = string.length();

        // Iterate over half of the string and check for equality with the corresponding characters from the end of the string
        for (int i = 0; i < number / 2; i++) {
            if (string.charAt(i) != string.charAt(number - 1 - i)) {
                return false;
            }
        }
        return true;
    }

     // Searches for palindromes in a matrix
     // with length param responsible for what length search for and return result
    public static Result searchPalindromesOfLength(int length) {

        // Record the start time
        long start = System.nanoTime();

        // Counter for storing found number of palindromes
        int counter = 0;

        // Horizontal search - left to right
        for (int y = 0; y < ROWS; y++) { // Looping through each row

            for (int x = 0; x <= COLS - length; x++) { // Loop through each valid starting column

                // Build a string from the current position and length "length"
                StringBuilder str = new StringBuilder(length);

                // Collect characters horizontally from left to right
                for (int k = 0; k < length; k++) {
                    str.append(data[y][x + k]);
                }

                // Check if the string is a palindrome
                if (isPalindrome(str.toString())) {
                    counter++; // Increment the counter if it's a palindrome
                }

            }
        }

        // Vertical search - top to bottom
        for (int x = 0; x < COLS; x++) {  // Loop through each column
            for (int y = 0; y <= ROWS - length; y++) {  // Loop through valid starting rows

                StringBuilder str = new StringBuilder(length);

                // Collect characters vertically
                for (int k = 0; k < length; k++) {
                    str.append(data[y + k][x]);
                }

                // Check for palindrome
                if (isPalindrome(str.toString())) {
                    counter++;
                }

            }
        }

        // Diagonal search - left to right
        for (int y = 0; y <= ROWS - length; y++) { // Loop through valid starting rows
            for (int x = 0; x <= COLS - length; x++) { // Loop through valid starting columns

                StringBuilder str = new StringBuilder(length);

                // Collect characters diagonally down-right
                for (int k = 0; k < length; k++) {
                    str.append(data[y + k][x + k]);
                }

                // Check for palindrome
                if (isPalindrome(str.toString())) {
                    counter++;
                }

            }
        }

        // Diagonal search - right to left
        for (int y = 0; y <= ROWS - length; y++) { // Loop through valid starting rows
            for (int x = length - 1; x < COLS; x++) { // Start at least 'length-1' to avoid negative index
                StringBuilder str = new StringBuilder(length);

                // Collect characters diagonally down-left
                for (int k = 0; k < length; k++) {
                    str.append(data[y + k][x - k]);
                }

                // Check for palindrome
                if (isPalindrome(str.toString())) {
                    counter++;
                }
            }
        }

        // Record the end time in nanoseconds
        long end = System.nanoTime();

        // Calculate the elapsed time in seconds
        double elapsedSeconds = (end - start) / 1e9;

        // Return a Result object containing the length,count and elapsed time
        return new Result(length, counter, elapsedSeconds);
    }
}

// Blueprint for Result object
class Result {

    private final int length;
    private final int count;
    private final double timeSeconds;

    public Result(int length, int count, double timeSeconds) {
        this.length = length;
        this.count = count;
        this.timeSeconds = timeSeconds;
    }

    // Getters
    public int getLength() {
        return length;
    }

    public int getCount() {
        return count;
    }

    public double getTimeSeconds() {
        return timeSeconds;
    }

    // Display the object parameters
    @Override
    public String toString() {
        return count + " palindromes of size" + length + " found in" + timeSeconds + " s.";
    }
}
