import java.util.Scanner;
class  Arr22
{
	public static void main(String[] args) 
	{
		Scanner sc=new Scanner(System.in);
		int arr[]={12,45,69,78,78,66,66,6,66,66};
		
		System.out.println("enter the element to see how many times occurred : ");
		int occ=sc.nextInt();
		int c=0;
		for(int i=0;i<arr.length;i++){
			if(arr[i]==occ){
				++c;
			}
		}
		System.out.println("Element "+occ+ "is occurred "+c+ " times");
	}
}
