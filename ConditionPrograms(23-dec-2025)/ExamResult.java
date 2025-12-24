import java.util.Scanner;
class ExamResult 
{
	public static void main(String[] args) 
	{
		Scanner sc=new Scanner(System.in);
		System.out.println("enter the marks: ");
		int marks=sc.nextInt();
		if(marks>=40)
		{
			System.out.println("Student is pass");
			if(marks>=80){
				System.out.println("A");
			}
			else if(marks>60 && marks<79){
				System.out.println("B");
			}
		}
		else
		{
			System.out.println("Fail");
		}
		
	}
}
