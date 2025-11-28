import java.util.ArrayList;

// Stores and manages all cars on the server
public class CarDatabase {

    // List of all cars
    private ArrayList<Car> data = new ArrayList<Car>();

    // Adds a new car if its registration does not already exist in data set
    public synchronized boolean addCar(Car car) {

        for (Car c : data) {
            if (c.getRegistration().equals(car.getRegistration())) {
                // Car already exists so reject it
                return false;
            }
        }
        // Add car and return postive flag
        data.add(car);
        return true;
    }

    // Marks a car as sold based on its registration
    public synchronized boolean sellCar(String registration) {
        for (Car car : data) {
            if (car.getRegistration().equals(registration) && car.forSale()) {
                car.setForSale(false);
                return true; // Car sold
            }
        }
        return false; // Car not found or already sold
    }

    // Returns all cars currently for sale
    public synchronized ArrayList<Car> listCarsForSale() {
        ArrayList<Car> saleList = new ArrayList<Car>();
        for (Car car : data) {
            if (car.forSale()) {
                saleList.add(car);
            }
        }
        return saleList;
    }

    // Returns all cars matching a specific make
    public synchronized ArrayList<Car> listCarsByMake(String make) {
        ArrayList<Car> makeList = new ArrayList<Car>();
        for (Car car : data) {
            if (car.getMake().equalsIgnoreCase(make)) {
                makeList.add(car);
            }
        }
        return makeList;
    }

    // Calculates total value of all cars that have been sold
    public synchronized double totalSellValue() {
        double totalSellValue = 0;
        for (Car car : data) {
            if (!car.forSale()) {
                totalSellValue += car.getPrice();
            }
        }
        return totalSellValue;
    }
}
