import java.util.Scanner;
class  Calculator
{
	public static void main(String[] args) 
	{
		Scanner sc=new Scanner(System.in);
		System.out.println("Enter first number");
		int a= sc.nextInt();
		System.out.println("Enter second number");
		int b= sc.nextInt();
		System.out.println("Enter choice for calculation +,-,*,/");
		char choice =sc.next().charAt(0);
		switch(choice){
			case '+' :
				System.out.println("addition : "+ (a+b));
				break;
			case '-' :
				System.out.println("addition : "+ (a-b));
				break;
			case '*' :
				System.out.println("addition : "+ (a*b));
				break;
			case '/' :
				System.out.println("addition : "+ (a/b));
				break;
			default:
				System.out.println("enter valid choice");
				break;
		}
	}
}
