import java.util.Scanner;
class  M5
{
	static Scanner sc=new Scanner(System.in);
	static double newB=0;
	
	public static void main(String[] args) 
	{
		
		System.out.println("Enter initial balance:");
		newB = sc.nextDouble();

		int choice=0;
		do{
		System.out.println("Enter choice: ");
		System.out.println("1-withdraw");
		System.out.println("2-deposit");
		System.out.println("3-balance");
		System.out.println("4-exit");
		choice=sc.nextInt();
		switch(choice){
			case 1:
			
				System.out.println("enter withdraw amount:");
				double Wamt=sc.nextDouble();
				if(Withdraw(newB,Wamt)){
					System.out.println("Amount is withdrawed");
				}
				else{
					System.out.println("Not sufficient amount");
				}
				break;
			case 2:
					
					System.out.println("enter deposit amount:");
					double Damt=sc.nextDouble();
					
					if(Deposit( newB,Damt)){
						System.out.println("deposited");
					}
					else{
						System.out.println("Value cannt be negative deposit wont happen");
					}
					
					break;
			case 3:
					
					
					double res=Display();
					System.out.println("account num is 23254876464");
					System.out.println("Name is Sanjana");
					System.out.println("balance is : "+res);
					break;
			case 4:
					System.exit(0);
					break;
			default:
					System.out.println("Invalid chocie");
		}
	
		}while(choice !=4);
		
	}
	public static boolean Withdraw(double bal,double wamt){
		{
			newB=bal-wamt;
			return (wamt >  0 && wamt<=bal);
		}
	}
	public static boolean Deposit(double bal,double damt){
		newB=newB+damt;
		return damt>0 ;
		
		
		
	}
	public static double Display(){
		return newB;
	}
	
	
}
