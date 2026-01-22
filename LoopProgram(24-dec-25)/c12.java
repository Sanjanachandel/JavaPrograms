import java.util.Scanner;

class c12 {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter dividend: ");
        int dividend = sc.nextInt();

        System.out.print("Enter divisor: ");
        int divisor = sc.nextInt();

        int quotient = 0;

        if (divisor == 0) {
            System.out.println("Division by zero not possible");
        } else {
            while (dividend >= divisor) {
                dividend = dividend - divisor;
                
            }

            System.out.println("Remainder = " + dividend);
        }
    }
}
