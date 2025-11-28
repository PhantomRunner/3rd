import java.io.Serializable;

public class ListCarsByMakeRequest implements Serializable {

    private String make;

    public ListCarsByMakeRequest(String make) {
        this.make = make;
    }

    public String getMake() {
        return make;
    }
}
