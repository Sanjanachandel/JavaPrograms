
//bank transction  validation create parameterized method that accepts the amount bal and withraw amount and 
//decide whetere the tranction can proceed the method should retirn a meaningful msg insted of printing directly
import java.util.Scanner;

class Bank
{
    public static void main(String[] args)
    {
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter account balance:");
        double accBal = sc.nextDouble();

        System.out.println("Enter withdraw amount:");
        double withdrawAmt = sc.nextDouble();

        String res = bankTransaction(accBal, withdrawAmt);
        System.out.println(res);   
    }

    public static String bankTransaction(double accBal, double withdrawAmt)
    {
        if (withdrawAmt <= accBal) {
            double newAcc = accBal - withdrawAmt;
            return "Transaction successful. Remaining balance: " + newAcc;
        }
        else {
            return "Transaction failed. Insufficient balance.";
        }
    }
}
