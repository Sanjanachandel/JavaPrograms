package FileHandling;
import java.io.*;

public class ShoppingOrder {

    public static void main(String[] args) {

        OrderFileHandler handler = new OrderFileHandler("orders.txt");

        handler.saveOrder(new Order("ORD101", 201, "Laptop", 1, 65000));
        handler.saveOrder(new Order("ORD102", 202, "Mouse", 2, 1200));
        handler.saveOrder(new Order("ORD103", 201, "Keyboard", 1, 2500));

        System.out.println("\nAll Orders:");
        handler.readAllOrders();

        System.out.println("\nOrders for Customer ID: 201");
        handler.getOrdersByCustomerId(201);

        System.out.println("\nHigh Value Orders (>5000.0):");
        handler.getHighValueOrders(5000);
    }
}

class Customer{
    int customerId;

    Customer(int customerId) {
        this.customerId = customerId;
    }
}



class OrderFileHandler {

    String filePath;

    OrderFileHandler(String filePath) {
        this.filePath = filePath;
        clearFile(); 
    }

    private void clearFile() {
        try {
            new FileWriter(filePath, false).close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveOrder(Order order) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) {
            bw.write(order.toString());
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

   
    public void readAllOrders() {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getOrdersByCustomerId(int id) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (Integer.parseInt(data[1]) == id) {
                    System.out.println(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getHighValueOrders(double minAmount) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                double price = Double.parseDouble(data[4]);
                if (price > minAmount) {
                    System.out.println(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
