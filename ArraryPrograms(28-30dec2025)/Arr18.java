import java.util.Scanner;
class  Arr18
{
	public static void main(String[] args) 
	{
		Scanner sc=new Scanner(System.in);
		int arr[]={12,45,69,78,6};
		System.out.print("Enter the element to check in array or not : ");
		int ele=sc.nextInt();
		for(int i=0;i<arr.length;i++){
			if(arr[i]==ele){
				System.out.println("Element "+ele+" is present is array ");
				break;
			}
			else{
				System.out.println("Not present");
				break;
			}
		}
	}
}
