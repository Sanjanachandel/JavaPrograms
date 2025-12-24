import java.util.Scanner;

class Reverse {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter a number: ");
        String num = sc.next();

        String rev = "";

        int i = num.length() - 1;

        while (i >= 0) {
            rev = rev + num.charAt(i);
            i--;
        }

        System.out.println("Reverse is: " + rev);
    }
}
