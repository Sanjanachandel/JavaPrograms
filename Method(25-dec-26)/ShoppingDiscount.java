//online shopping Discount engine
//write a method that takes purchase amount and customer type (regular /preminum) and return the final payment amount after applying discounts.
import java.util.Scanner;

class ShoppingDiscount
{
    public static void main(String[] args)
    {
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter purchase amount:");
        double amount = sc.nextDouble();

        sc.nextLine(); 

        System.out.println("Enter customer type (regular / premium):");
        String type = sc.nextLine();

        double finalAmount = calculateAmount(amount, type);
        System.out.println("Final payable amount: Rs" + finalAmount);
    }

    public static double calculateAmount(double amount, String type)
    {
        double discount = 0;

        if (type.equalsIgnoreCase("regular")) {
            if (amount >= 1000) {
                discount = amount * 0.10;
            }
        }
        else if (type.equalsIgnoreCase("premium")) {
            if (amount >= 1000) {
                discount = amount * 0.20;
            }
            else {
                discount = amount * 0.05;
            }
        }
        else {
            System.out.println("Invalid customer type");
            return amount;
        }

        return amount - discount;
    }
}
