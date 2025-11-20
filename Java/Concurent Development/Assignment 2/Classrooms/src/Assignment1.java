// Student Name - Stanislav Kril
// Student Number - 3133810

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class Assignment1 {
    // Simulation method
    public static void main(String[] args) {

        // List of the classrooms with identification Numbers and maximum allocated seats
        List<Classroom> classrooms = Arrays.asList(
                new Classroom("W201", 60),
                new Classroom("W202", 60),
                new Classroom("W101", 20),
                new Classroom("JS101", 30)
        );

        // List fo the Potential lecturers who will be running lectures in classes
        List<Lecturer> lecturers = Arrays.asList(
                new Lecturer("Osama", classrooms),
                new Lecturer("Barry", classrooms),
                new Lecturer("Faheem", classrooms),
                new Lecturer("Alex", classrooms),
                new Lecturer("Aqeel", classrooms),
                new Lecturer("Waseem", classrooms)
        );

        // Start lecturers
        for (Lecturer lecturer : lecturers) {
            lecturer.start();
        }

        // Start students
        for (int i = 0; i < 100; i++) {
            new Student(classrooms.get(new Random().nextInt(classrooms.size()))).start();
        }

        // Start visitors
        for (int i = 0; i < 10; i++) {
            new Visitor(classrooms.get(new Random().nextInt(classrooms.size()))).start();
        }

        // Start the monitor thread to track classroom status
        new Monitor(classrooms).start();
    }
}

class Classroom {
    // Initialize semaphores. One for seats and one for lectures fixed in 1
    private final Semaphore seatSemaphore;
    private final Semaphore lecturerSemaphore = new Semaphore(1);

    // Flag to track progress of lecture
    private boolean isRunning = false;

    // Name of the classroom
    private final String classroomName;

    // Keeps track of the lecturer currently teaching
    private Lecturer currentLecturer = null;

    // Flags to track current number of people in classroom
    private int currentStudents = 0;
    private int currentVisitors = 0;

    // Constructor to initialize classroom wit name and max allocated seats
    public Classroom(String classroomNumber, int capacity) {
        this.classroomName = classroomNumber;
        this.seatSemaphore = new Semaphore(capacity); // max allowed seats
    }

    // Start a lecture in the classroom
    synchronized void startSession(Lecturer lecturer) {
        isRunning = true; // set flag to indicate that lecture in progress
        this.currentLecturer = lecturer; // indicate who currently giving a lecture
    }

    // End a lecture in the classroom
    synchronized void endSession() {
        isRunning = false;
        this.currentLecturer = null;
    }

    // Getters for private fields
    synchronized boolean isSessionActive() {
        return isRunning;
    }
    public String getName() {
        return classroomName;
    }
    public int getCurrentStudents() {
        return currentStudents;
    }
    public int getCurrentVisitors() {
        return currentVisitors;
    }
    public Lecturer getCurrentLecturer() {
        return currentLecturer;
    }
    public Semaphore getSeatSemaphore() {
        return seatSemaphore;
    }
    public Semaphore getLecturerSemaphore() {
        return lecturerSemaphore;
    }

    // Setters fir  private fields
    public void setCurrentStudents(int currentStudents) {
        this.currentStudents = currentStudents;
    }
    public void setCurrentVisitors(int currentVisitors) {
        this.currentVisitors = currentVisitors;
    }

}

// Lecturer, who teaches in the classrooms
class Lecturer extends Thread {
    // Name of the lecturer and  list of classrooms where the lecturer can teach
    private final String lecturerName;
    private final List<Classroom> classrooms;

    // Random object for generating random numbers
    Random rand = new Random();

    // Constructor. Set name of lecturer and allowed classroom
    public Lecturer(String name, List<Classroom> classrooms) {
        this.lecturerName = name;
        this.classrooms = classrooms;
    }

    // Thread execution logic
    @Override
    public void run() {
        try {
            while (true) {
                // Randomly choose a classroom
                Classroom room = classrooms.get(rand.nextInt(classrooms.size()));

                // Try to enter classroom
                if (room.getLecturerSemaphore().tryAcquire()) {
                    synchronized (room) {
                        // Check if there's an ongoing lecture
                        if (!room.isSessionActive()) {
                            // Start the session if class is currently empty
                            room.startSession(this);
                            System.out.println(lecturerName + " started a lecture in " + room.getName());
                        }
                    }

                    // Simulate lecture duration
                    Thread.sleep(3000 + rand.nextInt(3000));

                    // End the session after the thread woke up from sleep
                    synchronized (room) {
                        room.endSession();
                        System.out.println(lecturerName + " ended lecture in " + room.getName());
                    }

                    // Release lecturer semaphore so other threads can access classroom
                    // Free the class so other lectures can access it
                    room.getLecturerSemaphore().release();

                    // Move to another class
                    Thread.sleep(2000);

                } else {
                    // Wait before retrying if failed to acquire semaphores
                    Thread.sleep(1000);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    // Getter for private fields
    public String getLecturerName() { return lecturerName; }
}

// Student who attends lectures in classrooms
class Student extends Thread {
    // Specific classroom where the student is attending
    private final Classroom classroom;

    // Generating random numbers
    Random rand = new Random();

    // Constructor, setting classroom that student attend
    public Student(Classroom classroom) {
        this.classroom = classroom;
    }

    @Override
    public void run() {
        try {
            while (true) {
                // Check if there's an ongoing lecture and seats are available
                if (!classroom.isSessionActive() && classroom.getSeatSemaphore().tryAcquire()) {
                    // Enter if there are a seat in class
                    synchronized (classroom) {
                        // Increment student count in the classroom. Decrement the number of available seats.
                        classroom.setCurrentStudents(classroom.getCurrentStudents() + 1);
                    }

                    // Wait before lecture starts
                    Thread.sleep(1000 + rand.nextInt(2000));

                    // Wait until lecture ends
                    while (classroom.isSessionActive()) {
                        Thread.sleep(1000);
                    }

                    // Decrement student count in the classroom when lecture ends. Free number of seats
                    synchronized (classroom) {
                        classroom.setCurrentStudents(classroom.getCurrentStudents() - 1);
                    }

                    // Release seat semaphore so other students can access seats
                    classroom.getSeatSemaphore().release();

                    // Wait before trying again
                    Thread.sleep(3000 + rand.nextInt(3000));
                } else {
                    // Wait if failed to acquire semaphores or lecture is ongoing
                    Thread.sleep(500);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

// Visitor who enters classrooms for other reasons than attending lectures
class Visitor extends Thread {

    // Specific classroom where the visitor is entering
    private final Classroom classroom;

    // Generating numbers
    Random rand = new Random();

    public Visitor(Classroom classroom) {
        this.classroom = classroom;
    }

    @Override
    public void run() {
        try {
            while (true) {
                // Check if there's an ongoing lecture and seats are available
                if (!classroom.isSessionActive() && classroom.getSeatSemaphore().tryAcquire()) {
                    synchronized (classroom) {
                        // Increment visitor count in the classroom
                        classroom.setCurrentVisitors(classroom.getCurrentVisitors() + 1);
                    }
                    // Visit duration
                    Thread.sleep(1000 + rand.nextInt(2000));

                    synchronized (classroom) {
                        // Decrement visitor count in the classroom once visit ends
                        classroom.setCurrentVisitors(classroom.getCurrentVisitors() + 1);
                    }
                    // Release seat semaphore so other threads can access seats
                    classroom.getSeatSemaphore().release();

                    Thread.sleep(3000 + rand.nextInt(4000)); // wander time
                } else {
                    // Wait, if failed to acquire semaphores or lecture is ongoing
                    Thread.sleep(1000);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
// Track the status of all classrooms and prints status every 2 seconds
class Monitor extends Thread {
    private final List<Classroom> classrooms;

    public Monitor(List<Classroom> classrooms) {
        this.classrooms = classrooms;
    }

    @Override
    public void run() {
        try {
            while (true) {
                // Header
                System.out.println();
                System.out.println("==================================================================================");
                System.out.println("Classroom  Lecturer  InSession  Students  Visitors");
                System.out.println("==================================================================================");

                // Loop through the classrooms and print lectures, number of students with visitors and classroom name
                for (Classroom classroom : classrooms) {
                    synchronized (classroom) {
                        String lecturerName = (classroom.getCurrentLecturer() == null) ? "None" : classroom.getCurrentLecturer().getLecturerName();
                        System.out.printf("%-10s %-10s %-10s %-10d %-10d%n",
                                classroom.getName(), lecturerName, classroom.isSessionActive(),
                                classroom.getCurrentStudents(), classroom.getCurrentVisitors()
                        );

                    }
                }
                System.out.println("==================================================================================");
                System.out.println();
                Thread.sleep(2000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

