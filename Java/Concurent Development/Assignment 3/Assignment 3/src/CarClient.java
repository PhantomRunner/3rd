
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

public class CarClient {

    final static int PORT = 1234;

    public static void main(String[] args) {

        try {
            Socket socket = new Socket(InetAddress.getLocalHost(), PORT);

            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            // Add cars to the database
            Car[] cars = {
                    new Car("ABC123", "Ferrari", 100000, 5000, true),
                    new Car("DEF456", "Ford Fiesta", 15000, 20000, true),
                    new Car("GHI789", "Ford Focus", 20000, 25000, true),
                    new Car("JKL012", "Mercedes Maybach", 22000, 30000, true),
                    new Car("MNO345", "Porsche 911", 35000, 15000, true),
                    new Car("PQR678", "Ford Mustang", 45000, 10000, false),
                    new Car("STU901", "Tesla Cybertruck", 18000, 22000, true),
                    new Car("VWX234", "Skoda Octavia", 20000, 18000, true),
                    new Car("YZA567", "Hyundai Solaris", 27000, 14000, true),
                    new Car("BCD890", "Toyota Corolla", 21000, 25000, false),
                    new Car("EFG123", "Audi q7", 12000, 30000, true),
                    new Car("HIJ456", "Lamborghini Urus", 25000, 20000, true),
                    new Car("KLM789", "Lamborghini Aventador", 18000, 22000, true),
                    new Car("NOP012", "BMV X5", 40000, 35000, true),
                    new Car("QRS345", "Range Rover", 15000, 28000, false)
            };

            for (Car car : cars) {
                AddCarRequest addReq = new AddCarRequest(car);
                System.out.println("[Client] Sending request: " + addReq.getClass().getSimpleName());
                out.writeObject(addReq);
                out.flush();

                Response response = (Response) in.readObject();
                System.out.println(response.getFeedback());
            }

            // List cars for sale
            ListCarsForSaleRequest listForSale = new ListCarsForSaleRequest();

            out.writeObject(listForSale);
            out.flush();

            Response responseForSale = (Response) in.readObject();
            System.out.println("\nCars for sale:");
            ArrayList<Car> saleList = (ArrayList<Car>) responseForSale.getData();
            for (Car car : saleList) {
                System.out.println(car);
            }

            //  Sell a car
            SellCarRequest sellReq = new SellCarRequest("DEF456"); // sell Ford Fiesta
            out.writeObject(sellReq);
            out.flush();

            Response sellResponse = (Response) in.readObject();
            System.out.println("\n" + sellResponse.getFeedback());

            // 4List cars of a specific make
            ListCarsByMakeRequest listByMakeReq = new ListCarsByMakeRequest("Ford");
            out.writeObject(listByMakeReq);
            out.flush();

            Response listByMakeResp = (Response) in.readObject();
            System.out.println("\nFord cars in database:");
            ArrayList<Car> fordCars = (ArrayList<Car>) listByMakeResp.getData();
            for (Car car : fordCars) {
                System.out.println(car);
            }

            TotalSellValueRequest req = new TotalSellValueRequest();
            out.writeObject(req);
            out.flush();

            Response totalResp = (Response) in.readObject();
            double totalValue = (Double) totalResp.getData();
            System.out.println("Total value of sold cars: " + totalValue);



        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
