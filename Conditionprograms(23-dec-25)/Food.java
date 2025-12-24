import java.util.Scanner;
class Food 
{
	public static void main(String[] args) 
	{
		Scanner sc=new Scanner(System.in);
		System.out.println("Enter choice: 1-4 ");
		int choice=sc.nextInt();
		switch(choice){
			case 1:
				System.out.println("South Indian");
				break;
			case 2:
				System.out.println("North Indian");
				break;
			case 3:
				System.out.println("Chinese");
				break;
			case 4:
				System.out.println("Italian");
				break;
			default:
				System.out.println("Enter valid range");
		}
		
	}
}
