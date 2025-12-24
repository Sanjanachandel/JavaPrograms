import java.util.Scanner;
class Shopping 
{
	public static void main(String[] args) 
	{
		Scanner sc=new Scanner(System.in);
		System.out.println("cart is empty or not true/false");
		boolean isCartEmpty=sc.nextBoolean();
		if(!isCartEmpty)
		{
		System.out.println("addres is valid or not true/false");
		String isvalid=sc.next();
		if(isvalid.equals("valid")){
			System.out.println("Checkout");
		}
			
		}
		else
		{
			System.out.println("not able to checkout");
		}
		
		
	}
}
