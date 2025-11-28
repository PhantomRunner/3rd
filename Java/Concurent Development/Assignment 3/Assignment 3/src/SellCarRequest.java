import java.io.Serializable;

// Request to mark a car as sold
public class SellCarRequest implements Serializable {

    // Registration of the car to sell
    private String registration;

    public SellCarRequest(String registration) {
        this.registration = registration;
    }

    public String getRegistration() {
        return registration;
    }
}
