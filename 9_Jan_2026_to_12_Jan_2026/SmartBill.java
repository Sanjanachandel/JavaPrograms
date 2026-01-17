package Collection;

public class SmartBill {

    public static void main(String[] args) {

        MeterReading m1 = new MeterReading(Integer.valueOf(1200), Integer.valueOf(1350));
        MeterReading m2 = new MeterReading(null, Integer.valueOf(1400));
        MeterReading m3 = new MeterReading(Integer.valueOf(900), null);

        Customer c1 = new Customer("C1", "Ramesh", m1);
        Customer c2 = new Customer("C2", "Sita", m2);
        Customer c3 = new Customer("C3", "Mohan", m3);

        BillingService service = new BillingService();

        service.generateBill(c1);
        service.generateBill(c2);
        service.generateBill(c3);
    }
}

class MeterReading {

    private Integer previousReading;
    private Integer currentReading;

    public MeterReading(Integer previousReading, Integer currentReading) {
        this.previousReading = previousReading;
        this.currentReading = currentReading;
    }

    public Integer getPreviousReading() {
        return previousReading;
    }

    public Integer getCurrentReading() {
        return currentReading;
    }
}

class Customer {

    private String customerId;
    private String customerName;
    private MeterReading reading;

    public Customer(String customerId, String customerName, MeterReading reading) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.reading = reading;
    }

    public String getCustomerName() {
        return customerName;
    }

    public MeterReading getReading() {
        return reading;
    }
}
class BillingService {

    Integer calculateUnits(MeterReading reading) {

        if (reading == null ||
            reading.getPreviousReading() == null ||
            reading.getCurrentReading() == null) {
            return null;
        }

        int units = reading.getCurrentReading() - reading.getPreviousReading();

        if (units < 0) {
            return null;
        }

        return units;
    }

    Integer calculateBill(Integer units) {
        return units * 5;
    }

    void generateBill(Customer customer) {

        Integer units = calculateUnits(customer.getReading());

        if (units == null) {
            System.out.println(customer.getCustomerName() + " --> Invalid Meter Data");
            return;  
        }

        Integer bill = calculateBill(units);
        System.out.println(customer.getCustomerName()
                + " --> Units: " + units
                + " --> Bill: " + bill);
    }
}
