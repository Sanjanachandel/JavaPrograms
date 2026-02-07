import java.util.*;

public class SmartCityTrafficManagement {

    private static Scanner sc = new Scanner(System.in);

    private static List<Vehicle> allVehicles = new ArrayList<>();
    private static Map<String, Vehicle> vehicleNumberMap = new HashMap<>();
    private static Map<String, List<Vehicle>> checkpointMap = new HashMap<>();
    private static LinkedList<Vehicle> lastFiveVehicles = new LinkedList<>();

    private static PriorityQueue<Vehicle> emergencyQueue =
            new PriorityQueue<>((v1, v2) -> Integer.compare(v2.getPriority(), v1.getPriority()));

    public static void main(String[] args) {

        while (true) {
            System.out.println("\n=== SMART CITY TRAFFIC MANAGEMENT ===");
            System.out.println("1. Add Vehicle Entry");
            System.out.println("2. Remove Duplicate Entries");
            System.out.println("3. Display Vehicles by Checkpoint");
            System.out.println("4. Process Emergency Vehicle Queue");
            System.out.println("5. Traffic Report");
            System.out.println("6. Show Last 5 Vehicles");
            System.out.println("7. Exit");
            System.out.print("Choose: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    addVehicle();
                    break;
                case 2:
                    removeDuplicates();
                    break;
                case 3:
                    displayByCheckpoint();
                    break;
                case 4:
                    processEmergencyQueue();
                    break;
                case 5:
                    trafficReport();
                    break;
                case 6:
                    showLastFive();
                    break;
                case 7:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static void addVehicle() {
        System.out.print("Vehicle Number: ");
        String number = sc.nextLine();

        System.out.print("Type (Car/Bike/Bus/Truck/Ambulance/FireTruck): ");
        String type = sc.nextLine();

        System.out.print("Checkpoint: ");
        String checkpoint = sc.nextLine();

        long timestamp = System.currentTimeMillis();
        Vehicle vehicle = new Vehicle(number, type, checkpoint, timestamp);

        allVehicles.add(vehicle);
        vehicleNumberMap.put(number, vehicle);

        checkpointMap.putIfAbsent(checkpoint, new ArrayList<>());
        checkpointMap.get(checkpoint).add(vehicle);

        if (vehicle.getPriority() > 0) {
            emergencyQueue.add(vehicle);
        }

        lastFiveVehicles.add(vehicle);
        if (lastFiveVehicles.size() > 5) {
            lastFiveVehicles.removeFirst();
        }

        System.out.println("Entry added!");
    }

    private static void removeDuplicates() {
        Set<Vehicle> uniqueVehicles = new HashSet<>(allVehicles);
        allVehicles.clear();
        allVehicles.addAll(uniqueVehicles);
        System.out.println("Duplicates removed!");
    }

    private static void displayByCheckpoint() {
        System.out.print("Enter checkpoint name: ");
        String checkpoint = sc.nextLine();

        List<Vehicle> vehicles = checkpointMap.get(checkpoint);
        if (vehicles == null || vehicles.isEmpty()) {
            System.out.println("No vehicles found.");
            return;
        }

        System.out.println("Vehicles at " + checkpoint + ":");
        for (Vehicle v : vehicles) {
            System.out.println(v);
        }
    }

    private static void processEmergencyQueue() {
        if (emergencyQueue.isEmpty()) {
            System.out.println("No emergency vehicles.");
            return;
        }

        Vehicle vehicle = emergencyQueue.poll();
        System.out.println("Processing emergency vehicle:");
        System.out.println(vehicle);
    }

    private static void trafficReport() {
        Map<String, Integer> congestionMap = new HashMap<>();

        for (Vehicle v : allVehicles) {
            congestionMap.put(v.checkpointName,
                    congestionMap.getOrDefault(v.checkpointName, 0) + 1);
        }

        if (congestionMap.isEmpty()) {
            System.out.println("No traffic data.");
            return;
        }

        System.out.println("--- Traffic Report ---");
        System.out.println("Checkpoint congestion:");

        String busiest = null;
        String leastBusy = null;

        for (String cp : congestionMap.keySet()) {
            System.out.println(cp + ": " + congestionMap.get(cp));

            if (busiest == null || congestionMap.get(cp) > congestionMap.get(busiest)) {
                busiest = cp;
            }
            if (leastBusy == null || congestionMap.get(cp) < congestionMap.get(leastBusy)) {
                leastBusy = cp;
            }
        }

        System.out.println("Busiest: " + busiest);
        System.out.println("Least Busy: " + leastBusy);
    }

    private static void showLastFive() {
        System.out.println("Last 5 vehicles:");
        for (Vehicle v : lastFiveVehicles) {
            System.out.println(v);
        }
    }
}

class Vehicle {

    String vehicleNumber;
    String type;
    String checkpointName;
    long timestamp;

    public Vehicle(String vehicleNumber, String type, String checkpointName, long timestamp) {
        this.vehicleNumber = vehicleNumber;
        this.type = type;
        this.checkpointName = checkpointName;
        this.timestamp = timestamp;
    }

    public int getPriority() {
        if (type.equalsIgnoreCase("Ambulance")) return 3;
        if (type.equalsIgnoreCase("FireTruck")) return 2;
        return 1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vehicle)) return false;
        Vehicle v = (Vehicle) o;
        return vehicleNumber.equals(v.vehicleNumber) && timestamp == v.timestamp;
    }

    @Override
    public int hashCode() {
        return Objects.hash(vehicleNumber, timestamp);
    }

    @Override
    public String toString() {
        return "Vehicle[" + vehicleNumber + ", " + type + ", " +
                checkpointName + ", " + timestamp + "]";
    }
}