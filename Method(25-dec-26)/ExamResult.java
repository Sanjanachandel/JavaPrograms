//Exam result analyzer
//create  a method that accepts marks of three subjects as parameters and return the grade
//the calling method should decide whether the student is eligible for placement training
import java.util.Scanner;
class ExamResult 
{
	public static void main(String[] args) 
	{
		Scanner sc=new Scanner(System.in);
		System.out.println("Enter first subject makrs: ");
		int a=sc.nextInt();
		System.out.println("Enter second subject makrs: ");
		int b=sc.nextInt();
		System.out.println("Enter third subject makrs: ");
		int c=sc.nextInt();
		char res=result(a,b,c);
		 System.out.println("Grade: " +res);
		if(res=='A' || res=='B'){
			System.out.println("Student is eligible for placement training");
		}
		
		else{
			System.out.println("Student is not eligible for placement training");
		}
	}
	public static char result(int a,int b,int c){
		double avg=(a+b+c)/3.0;
		if(avg>=90 && avg<=100){
			return 'A';
		}
		 else if(avg>=85 ){
			return 'B';
		}
		else if(avg>=65 ){
			return 'C';
		}
		else if(avg>=45){
			return 'D';
		}
		else{
			return 'F';
		}
	
	}
}

