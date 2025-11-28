import java.io.Serializable;

// Request for listing cars filtered by a specific make
public class ListCarsByMakeRequest implements Serializable {

    // Car make filter
    private String make;

    public ListCarsByMakeRequest(String make) {
        this.make = make;
    }

    public String getMake() {
        return make;
    }
}
