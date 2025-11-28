import java.io.Serializable;

// Request to add a new car to the database
public class AddCarRequest implements Serializable {

    // Car object which will be added
    private Car car;

    public AddCarRequest(Car car) {
        this.car = car;
    }

    public Car getCar() {
        return car;
    }
}
