
// Printer class  prints give messages in a terminal until get interrupted

public class Printer extends Thread {

    // Instance variables
    String name; // variable carrying message to be printed in terminal
    boolean running;

    public Printer(String name) { // constructor taking only message as an argument
        this.name = name;
        running = true; // setting up running flag to start thread
    }

    @Override
    public void run() {
        while (running) {
            try {
                // Printing message with 100ms pause
                System.out.println(name);
                Thread.sleep(100);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    // Public method to stop thread
    public void terminate() {
        running = false;
    }

}
