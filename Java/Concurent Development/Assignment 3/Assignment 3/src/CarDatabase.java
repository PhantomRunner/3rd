import java.util.ArrayList;


// Class to store data on server
public class CarDatabase {

    private ArrayList<Car> data = new ArrayList<Car>();


    public synchronized boolean addCar(Car car) {

        for (Car c : data) {
            if ((c.getRegistration().equals(car.getRegistration()))) {
                return false;
            }
        }
        data.add(car);
        return true;
    }

    public synchronized boolean sellCar(String registration) {
        for (Car car : data) {
            if (car.getRegistration().equals(registration) && car.forSale()) {
                car.setForSale(false);
                return true;
            }
        }
        return false;
    }

    public synchronized ArrayList<Car> listCarsForSale() {
        ArrayList<Car> saleList = new ArrayList<Car>();
        for (Car car : data) {
            if (car.forSale()) {
                saleList.add(car);
            }
        }
        return saleList;
    }

    public synchronized ArrayList<Car> listCarsByMake(String make) {
        ArrayList<Car> makeList = new ArrayList<Car>();
        for (Car car : data) {
            if (car.getMake().equalsIgnoreCase(make)) {
                makeList.add(car);
            }
        }
        return makeList;
    }

    public synchronized double totalSellValue() {
        double totalSellValue = 0;
        for (Car car : data) {
            if (!car.forSale()) {
                totalSellValue = totalSellValue + car.getPrice();
            }
        }
        return totalSellValue;
    }
}
