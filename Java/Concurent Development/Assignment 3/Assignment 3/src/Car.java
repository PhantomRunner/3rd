import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

// Class to represent the Car object
public class Car {
    // Fields of each Car instance
    private String registration;
    private String make;
    private int price;
    private int mileAge;
    private boolean forSale;

    // Constructors
    public Car(String registration, String make, int price, int mileAge, boolean forSale) {
        this.registration = registration;
        this.make = make;
        this.price = price;
        this.mileAge = mileAge;
        this.forSale = forSale;
    }
    public Car() {
        this.registration = "";
        this.make = "";
        this.price = 0;
        this.mileAge = 0;
        this.forSale = false;
    }

    // Getters
    public String getRegistration() {
        return registration;
    }
    public boolean isForSale() {
        return forSale;
    }
    public String getMake() {
        return make;
    }
    public int getPrice() {
        return price;
    }
    public int getMileAge() {
        return mileAge;
    }

    // Setters
    public void setPrice(int price) {
        this.price = price;
    }
    public void setMileAge(int mileAge) {
        this.mileAge = mileAge;
    }
    public void setMake(String make) {
        this.make = make;
    }
    public void setRegistration(String registration) {
        this.registration = registration;
    }
    public void setForSale(boolean forSale) {
        this.forSale = forSale;
    }

    public String toString() {
        return "Registration: " + registration + " Make: " + make + " Price: " + price + " Mileage: " + mileAge;
    }

    public void writeOutputStream(DataOutputStream out) {
        try{
            out.writeUTF(registration);
            out.writeUTF(make);
            out.writeInt(price);
            out.writeInt(mileAge);
            out.writeBoolean(forSale);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void readInputStream(DataInputStream in) {
        try {
            registration = in.readUTF();
            make = in.readUTF();
            price = in.readInt();
            mileAge = in.readInt();
            forSale = in.readBoolean();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
