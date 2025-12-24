import java.util.Scanner;
class  BankAccount
{
	public static void main(String[] args) 
	{
		Scanner sc=new Scanner(System.in);
		System.out.println("enter your balance: ");
		double balance=sc.nextDouble();
		System.out.println("enter your Minibalance: ");
		double Minibalance=sc.nextDouble();
		System.out.println("true/false for account is frozen or not ");
		double frozen=sc.nextBoolean();
		if(!frozen){
		System.out.println("enter your Minibalance: ");
		double Minibalance=sc.nextDouble();
		if(balance>=Minibalance ){
			System.out.println("Active");
		}
		else{
			System.out.println("Low balance");
		}
		else{
			System.out.println("Account is frozen");
		}
		
	}
}
