import java.util.Arrays;
class  Arr1
{
	public static void main(String[] args) 
	{
		//ways to print the element in array
		int arr[] ={1,2,3,4};
		for(int i=0;i<arr.length;i++){
			System.out.println(arr[i] +" ");
		}
		System.out.println();
		System.out.println(Arrays.toString(arr));
	}
}