package FileHandling;

class Order {

    String orderId;
    int customerId;
    String productName;
    int quantity;
    double totalPrice;

    Order(String orderId, int customerId, String productName, int quantity, double totalPrice) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.productName = productName;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    public String toString() {
        return orderId + "," + customerId + "," + productName + "," + quantity + "," + totalPrice;
    }
}
