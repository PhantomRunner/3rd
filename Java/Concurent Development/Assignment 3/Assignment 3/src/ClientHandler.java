import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

// Handles a single client connection on the server
public class ClientHandler extends Thread {

    // Client socket for communication
    private Socket clientSocket;

    // Reference to server database
    private CarDatabase carDatabase;

    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;

    // Constructor initializes socket and database reference
    public ClientHandler(Socket socket, CarDatabase carDatabase) {
        this.clientSocket = socket;
        this.carDatabase = carDatabase;
    }

    // Main thread loop to process incoming requests
    public void run() {
        try {
            // Setup input/output streams for client communication
            outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            inputStream = new ObjectInputStream(clientSocket.getInputStream());

            while (true) {
                Object object = inputStream.readObject();
                // Request type detection

                if (object instanceof AddCarRequest) {
                    AddCarRequest addCarRequest = (AddCarRequest) object;

                    // Add car to database
                    boolean isAdded = carDatabase.addCar(addCarRequest.getCar());
                    String message;

                    if (isAdded) {
                        message = "Car added successfully!";
                    } else {
                        message = "Car already  in the database";
                    }

                    // Create a Response object with proper success flag and message
                    Response response = new Response(
                            isAdded,  // success = true if car was added
                            message
                    );

                    // Send the Response back to the client
                    outputStream.writeObject(response);
                    outputStream.flush();

                } else if (object instanceof ListCarsByMakeRequest) {
                    ListCarsByMakeRequest listCarsByMakeRequest = (ListCarsByMakeRequest) object;
                    ArrayList<Car> makeList = carDatabase.listCarsByMake(((listCarsByMakeRequest)).getMake());

                    Response response = new Response(
                            true,
                            "Cars with specific make", makeList);

                    outputStream.writeObject(response);
                    outputStream.flush();

                } else if (object instanceof ListCarsForSaleRequest) {
                    // List all cars currently for sale
                    ArrayList<Car> saleList = carDatabase.listCarsForSale();

                    Response response = new Response(
                            true,
                            "Cars for sale", saleList);

                    outputStream.writeObject(response);
                    outputStream.flush();

                } else if (object instanceof SellCarRequest) {
                    SellCarRequest sellRequest = (SellCarRequest) object;

                    // Attempt to sell car
                    boolean sold = carDatabase.sellCar(sellRequest.getRegistration());
                    String message;

                    if (sold) {
                        message = "Car sold successfully!";
                    } else {
                        message = "Car already sold successfully!";
                    }

                    Response response = new Response(sold, message);
                    outputStream.writeObject(response);
                    outputStream.flush();

                } else if (object instanceof TotalSellValueRequest) {
                    // Calculate total sold car value
                    double total = carDatabase.totalSellValue();

                    Response response = new Response(true, "Total value of sold cars", total);
                    outputStream.writeObject(response);
                    outputStream.flush();
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e); // Crash if an unexpected error occurs
        }
    }
}
