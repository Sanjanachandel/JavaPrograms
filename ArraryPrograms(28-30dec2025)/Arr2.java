import java.util.Scanner;
import java.util.Arrays;
class Arr2 
{
	public static void main(String[] args) 
	{
		Scanner s=new Scanner(System.in);
		System.out.print("Enter size : ");
		int arr[]=new int[s.nextInt()];
		for(int i=0;i<arr.length;i++){
			System.out.print("Element "+(i+1)+": ");
			arr[i]=s.nextInt();
			}
		for(int i=0;i<arr.length;i++){
			System.out.println(arr[i]);
			
			}
			System.out.println();
			System.out.println(Arrays.toString(arr));//it is static way
	}
}
