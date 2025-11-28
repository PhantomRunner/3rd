import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler extends Thread {
    private Socket clientSocket;
    private CarDatabase carDatabase;

    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;


    public ClientHandler(Socket socket, CarDatabase carDatabase) {
        this.clientSocket = socket;
        this.carDatabase = carDatabase;
    }

    public void run() {
        try {
            outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            inputStream = new ObjectInputStream(clientSocket.getInputStream());

            while (true) {

                Object object = inputStream.readObject();
                // Debug statement
                // System.out.println("[Server] Received request: " + object.getClass().getSimpleName());

                if (object instanceof AddCarRequest) {
                    AddCarRequest addCarRequest = (AddCarRequest) object;

                    // Call the database method — returns true if added, false if already exists
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

                    // Debug statement
//                    System.out.println("[Server] Sending response: success=" + response.isSuccess() +
//                            ", message='" + response.getFeedback() + "'");

                    // Send the Response back to the client
                    outputStream.writeObject(response);
                    outputStream.flush();

                } else if (object instanceof ListCarsByMakeRequest) {
                    ListCarsByMakeRequest listCarsByMakeRequest = (ListCarsByMakeRequest) object;
                    ArrayList<Car> makeList = carDatabase.listCarsByMake(((listCarsByMakeRequest)).getMake());

                    Response response = new Response(
                            true,
                            "Cars with specific make", makeList);

                    // Debug statement
//                    System.out.println("[Server] Sending response: success=" + response.isSuccess() +
//                            ", message='" + response.getFeedback() + "'");

                    outputStream.writeObject(response);
                    outputStream.flush();

                } else if (object instanceof ListCarsForSaleRequest) {
                    ArrayList<Car> saleList = carDatabase.listCarsForSale();

                    Response response = new Response(
                            true,
                            "Cars for sale", saleList);
                    // Debug statement
//                    System.out.println("[Server] Sending response: success=" + response.isSuccess() +
//                            ", message='" + response.getFeedback() + "'");

                    outputStream.writeObject(response);
                    outputStream.flush();

                } else if (object instanceof SellCarRequest) {
                    SellCarRequest sellRequest = (SellCarRequest) object;

                    boolean sold = carDatabase.sellCar(sellRequest.getRegistration());
                    String message;

                    if (sold) {
                        message = "Car sold successfully!";
                    } else {
                        message = "Car already sold successfully!";
                    }

                    Response response = new Response(
                            sold,
                            message
                    );

                    // Debug statement
//                    System.out.println("[Server] Sending response: success=" + response.isSuccess() +
//                            ", message='" + response.getFeedback() + "'");

                    outputStream.writeObject(response);
                    outputStream.flush();

                }else if (object instanceof TotalSellValueRequest) {
                    double total = carDatabase.totalSellValue();
                    Response response = new Response(true, "Total value of sold cars", total);

                    // Debug statement
//                    System.out.println("[Server] Sending response: success=" + response.isSuccess() +
//                            ", message='" + response.getFeedback() + "'");

                    outputStream.writeObject(response);
                    outputStream.flush();
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
}

