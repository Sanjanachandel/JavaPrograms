//Electricity bill calculator
//create a parameterized method that accepts units consumed and calculates the bill based on slab rated
//think: how will you handle boundary values?
import java.util.Scanner;

class ElectricityBill
{
    public static void main(String[] args)
    {
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter units consumed:");
        int units = sc.nextInt();

        double bill = calculateBill(units);
        System.out.println("Total Electricity Bill: ?" + bill);
    }

    public static double calculateBill(int units)
    {
        double amount = 0;
		
        if (units <= 100) {
            amount = units * 1;
        }
        else if (units <= 200) {
            amount = (100 * 1) + (units - 100) * 2;
        }
        else if (units <= 300) {
            amount = (100 * 1) + (100 * 2) + (units - 200) * 3;
        }
        else {
            amount = (100 * 1) + (100 * 2) + (100 * 3) + (units - 300) * 5;
        }

        return amount;
    }
}
