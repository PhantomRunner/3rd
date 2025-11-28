import java.io.Serializable;

public class AddCarRequest implements Serializable {
    private Car car;

    public AddCarRequest(Car car) {
        this.car = car;
    }

    public Car getCar() {
        return car;
    }
}
