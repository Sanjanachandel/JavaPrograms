

import java.util.Scanner;

class OnlineCab
{
    public static void main(String[] args)
    {
        Scanner sc = new Scanner(System.in);

        System.out.println("Is driver available? (true/false)");
        boolean driverAvl = sc.nextBoolean();

        String driverName = "";

        if (driverAvl) {
            System.out.println("Enter driver name:");
            driverName = sc.next();
        }

        System.out.println("Is service available? (true/false)");
        boolean serviceAvl = sc.nextBoolean();

        System.out.println("Enter payment method (cash/online):");
        String payMethod = sc.next();

        if (driverAvl && serviceAvl &&
           (payMethod.equalsIgnoreCase("cash") ||
            payMethod.equalsIgnoreCase("online")))
        {
            System.out.println("Car booked successfully");
            System.out.println("Driver Name: " + driverName);
        }
        else
        {
            System.out.println("Car not booked");
        }

        sc.close();
    }
}
