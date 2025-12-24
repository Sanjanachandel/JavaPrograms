import java.util.Scanner;
class Atm 
{
	public static void main(String[] args) 
	{
		Scanner sc=new Scanner(System.in);
		System.out.println("enter the balance amount greater than 0 and in double number");
		double balance=sc.nextDouble();
		int choice=0;
		do{
		System.out.println("Enter choice: ");
		System.out.println("1-withdraw ");
		System.out.println("2-deposit ");
		System.out.println("3-balance Enquiry ");
		System.out.println("4-exit");
		 choice=sc.nextInt();
		switch(choice){
			case 1:
				System.out.println("Enter amount to withdraw: ");
				double amountW=sc.nextDouble();
				if(amountW>0 && amountW<=balance){
				balance=(balance-amountW);
				System.out.println("Withdrawed");}
				else{
					System.out.println("Not possible");
				}
				break;
			case 2:
				System.out.println("Enter amount to deposit: ");
				double amountD=sc.nextDouble();
				balance=(balance+amountD);
				System.out.println("Deposited");
				break;
			case 3:
				System.out.println("your balance is: "+balance);
				break;
			case 4:
				System.out.println("Exit");
				break;
			default:
				System.out.println("Invalid choice");
		}
		}while(choice !=4);
		
	}
}
