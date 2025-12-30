import java.util.Scanner;

class M3
{
	 static Scanner sc = new Scanner(System.in);
    public static void main(String[] args)
    {
       
        String ch;

        do {
            System.out.println("\nEnter choice:");
            System.out.println("1 - Rectangle");
            System.out.println("2 - Circle");
            System.out.println("3 - Square");

            int choice = sc.nextInt();

            switch (choice)
            {
            case 1:
				System.out.println("Area of rectangle: " + rect());
				break;
			 case 2:
				System.out.println("Area of circle: " + cir());
				break;
			case 3:
				System.out.println("Area of square: " + sq());
				break;
            
			default:
				 System.out.println("Invalid choice");
					break;
            
           }
          

            System.out.println("\nDo you want to continue? (yes/no): ");
            ch = sc.next();

        } while (ch.equals("yes"));

        System.out.println("Program exited");
    }

    public static int rect() {
        
        System.out.println("Enter length:");
        int l = sc.nextInt();
        System.out.println("Enter breadth:");
        int b = sc.nextInt();
        return l * b;
    }

    public static double cir() {
        
        System.out.println("Enter radius:");
        double r = sc.nextDouble();
        return 3.14 * r * r;
    }

    public static int sq() {
       
        System.out.println("Enter side:");
        int s = sc.nextInt();
        return s * s;
    }
}
