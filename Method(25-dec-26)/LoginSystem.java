//login authentication system
//design a method that accpets username and password as parameters and validates them against predefined credentials
//the method should return a staud code not boolean
import java.util.Scanner;
class  LoginSystem
{
	public static void main(String[] args) 
	{
		Scanner sc=new Scanner(System.in);
		System.out.println("Enter username :");
		String name=sc.nextLine();
		System.out.println("Enter password and it should include @/num/Alphabet :");
		String password=sc.nextLine();
		int res=login(name,password);
		System.out.println("Status code is : "+res);
		
	}
	public static int login(String name,String password){
		int status_code=401;
		String prename="Sanjana chandel";
		String prepassword="Sana@123";
		if(prename.equals(name) && prepassword.equals(password)){
			status_code =200;
			return status_code;
		}
		else{
			return  status_code;
		}
		
	}
}
