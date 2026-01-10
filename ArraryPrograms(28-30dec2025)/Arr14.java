import java.util.Scanner;
class Arr14
 
{
	public static void main(String[] args) 
	{
		
		Scanner sc=new Scanner(System.in);
		System.out.println("enter size; ");
		int s=sc.nextInt();
		int arr[]=new int[s];
		for(int i=0;i<arr.length;i++){
			if(i%2==0){
				arr[i]=sc.nextInt();
			}
		}
		for(int i=0;i<arr.length;i++){
			System.out.println(arr[i] +" " );
		}
	}
}
