// Stanislav Kril - 3133810

import java.util.concurrent.*;

public class EvenCounter {

    public static void main(String[] args) {
        // Number of values to be analyzed
        int N = 1000000;

        // Number of available threads
        int numberOfThreads = Runtime.getRuntime().availableProcessors();

        ExecutorService ThreadPool = Executors.newFixedThreadPool(numberOfThreads);

        // Create an array to store the indices for each thread (size: number of threads + 1)
        // This is because we will use index[0] as a starting point, and index[numberOfThreads] as the end point
        int[] index = new int[numberOfThreads + 1];

        // Calculate the start and end indices for each thread
        for (int j = 0; j <= numberOfThreads; j++) {
            // For each thread, calculate its corresponding range in the array
            index[j] = (j * N) / numberOfThreads;
        }

        // Create an array to store random data with 'N' elements
        int[] data = new int[N];

        // Fill the data array with random numbers between 0 and 100000
        for (int j = 0; j < data.length; j++) {
            data[j] = (int) (Math.random() * 100000);
        }

        // Create an array of Future interface placeholder to store results
        Future<Integer[]>[] futures = new Future[numberOfThreads];

        // Begin computations. Start measuring performance
        long startTime = System.currentTimeMillis();

        // Submit work for each thread
        for (int i = 0; i < numberOfThreads; i++) {
            futures[i] = ThreadPool.submit(new Search(data, index[i], index[i + 1]));
        }

        // Shutdown thread pool when all work done
        ThreadPool.shutdown();

        // Initialize variables
        int freq = 0;
        int maximum = Integer.MIN_VALUE;

        try {
            for (Future<Integer[]> future : futures) {
                Integer[] results = future.get();
                freq = results[0];
                maximum = results[1];
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        long endTime = System.currentTimeMillis();
        long runningTime = endTime - startTime;

        System.out.println("Freq: " + freq);
        System.out.println("Maximum assigned value:" + maximum);
        System.out.println(runningTime + " millisecond (" + (runningTime / 1000.0) + ")");
        
    }
}
