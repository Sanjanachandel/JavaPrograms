
// A shop will give discount of % if the cost of purchased quantity is more than 1000 based on quantity suppose ,one unit will cost 100 judge and print the total cost for user
import java.util.Scanner;

class ShopDiscount {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("Enter the quantity you want:");
        int quantity = sc.nextInt();

        int pricePerUnit = 100;
        int cost = quantity * pricePerUnit;

        if (cost > 1000) {
            double discount = cost * 0.10;
            double totalCost = cost - discount;

            System.out.println("Original Cost: " + cost);
            System.out.println("Discount: " + discount);
            System.out.println("Total Cost after discount: " + totalCost);
        } else {
            System.out.println("No discount applied.");
            System.out.println("Total Cost: " + cost);
        }
    }
}

