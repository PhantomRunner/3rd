import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

// Class to represent the Car object
public class Car {

    // Fields of each Car instance
    private String registration;
    private String make;
    private double price;
    private int mileage;
    private boolean forSale;

    // Constructors
    public Car(String registration, String make, double price, int mileAge, boolean forSale) {
        this.registration = registration;
        this.make = make;
        this.price = price;
        this.mileage = mileAge;
        this.forSale = forSale;
    }

    public Car() {
        this.registration = "";
        this.make = "";
        this.price = 0;
        this.mileage = 0;
        this.forSale = false;
    }

    // Getters
    public String getRegistration() {
        return registration;
    }
    public boolean forSale() {
        return forSale;
    }
    public String getMake() {
        return make;
    }
    public double getPrice() {
        return price;
    }
    public int getMileage() {
        return mileage;
    }

    // Setters
    public void setPrice(int price) {
        this.price = price;
    }
    public void setMileage(int mileage) {
        this.mileage = mileage;
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
        return make + " - " + price + " - Mileage - " + mileage + " - Registration - " + registration.toUpperCase() + " - For sale: " + forSale;
    }

    public void writeOutputStream(DataOutputStream out) {
        try {
            out.writeUTF(registration);
            out.writeUTF(make);
            out.writeDouble(price);
            out.writeInt(mileage);
            out.writeBoolean(forSale);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void readInputStream(DataInputStream in) {
        try {
            registration = in.readUTF();
            make = in.readUTF();
            price = in.readInt();
            mileage = in.readInt();
            forSale = in.readBoolean();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
