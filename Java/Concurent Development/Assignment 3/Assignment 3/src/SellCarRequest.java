import java.io.Serializable;

public class SellCarRequest implements Serializable {
    private String registration;

    public SellCarRequest(String registration) {
        this.registration = registration;
    }

    public String getRegistration() {
        return registration;
    }
}
