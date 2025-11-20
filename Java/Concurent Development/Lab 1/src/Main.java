// Stanislav Kril - 3133810

import java.util.Scanner;

public static void main(String[] args) {

    try {
        // Initializing instance of coin Thread
        Thread coin = new CoinTosser();
        coin.start(); // start thread

        Thread.sleep(1500); // put 2-second break between tasks

        // Initializing instance of Printer Thread with message
        Printer printer = new Printer("I am printer");
        printer.start(); // start thread

        Scanner scanner = new Scanner(System.in);

        // If the input in console appear different from provided message, terminate thread
        if (!printer.getName().equals(scanner.nextLine())) {
            printer.terminate();

            // Notify that thread has been terminated
            System.out.println("Process terminated");
        }
    } catch (Exception e) {
        throw new RuntimeException(e);
    }


}
