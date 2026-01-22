import java.util.Scanner;
class c2 
{
	public static void main(String[] args) 
	{
		Scanner sc=new Scanner(System.in);
		System.out.println("Enter the number correspnding that alphabet value you got");
		int num=sc.nextInt();
		char ch='A';
		for(int i=2;i<=num;i++){
			ch++;
		}
		System.out.println(ch);
	
	}
	}

