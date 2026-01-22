package Day8;

public class Ride_Sharing_System {
    public static void main(String[] args) {
        Driver driver = new Driver("Ramesh");

        Payment payment1 = new Payment(true);
        Ride ride1 = new Ride(101, driver, payment1);
        ride1.startRide();

        Payment payment2 = new Payment(true);
        Ride ride2 = new Ride(102, driver, payment2);
        ride2.startRide();
    }
}

class Driver {
    private String name;
    private boolean available;

    public Driver(String name) {
        this.name = name;
        this.available = true; 
    }

    public boolean isAvailable() {
        return available;
    }

    public void markUnavailable() {
        available = false;
    }

    public String getName() {
        return name;
    }
}

class Payment {
    private boolean successful;

    public Payment(boolean successful) {
        this.successful = successful;
    }

    public boolean isSuccessful() {
        return successful;
    }
}

class Ride {
    private int rideId;
    private Driver driver;
    private Payment payment;

    public Ride(int rideId, Driver driver, Payment payment) {
        this.rideId = rideId;
        this.driver = driver;
        this.payment = payment;
    }

    public void startRide() {
        if (driver.isAvailable() && payment.isSuccessful()) {
            System.out.println("Ride " + rideId + " started with driver " + driver.getName());
            driver.markUnavailable(); 
        } else {
            System.out.println("Ride " + rideId + " failed");
        }
    }
}


