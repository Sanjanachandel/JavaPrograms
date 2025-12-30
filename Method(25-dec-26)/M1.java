import java.util.Scanner;

class M1 
{
	public static void main(String[] args) 
	{
		Scanner sc = new Scanner(System.in);
		System.out.println("Write add for addition , sub for subtraction , mul for multiplication");
		String val=sc.next();
		if(val.equals("add"))
		{
		addition();
		}
		else if(val.equals("sub"))
		{
			subtraction();
		}
		else
		{
		multiplication();
		}
	}

	public static void addition() {
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter one num:");
		int a = sc.nextInt();
		System.out.println("Enter second num:");
		int b = sc.nextInt();
		int res = a + b;
		System.out.println("Addition of two numbers: " + res);
	}

	public static void subtraction() {
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter one num:");
		int a = sc.nextInt();
		System.out.println("Enter second num:");
		int b = sc.nextInt();
		int res = a - b;
		System.out.println("Subtraction of two numbers: " + res);
	}

	public static void multiplication() {
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter one num:");
		int a = sc.nextInt();
		System.out.println("Enter second num:");
		int b = sc.nextInt();
		int res = a * b;
		System.out.println("Multiplication of two numbers: " + res);
	}
}
