//wap tp find the avg vaue of even index from an array and   odd   first half and second half  from an array 
//wap to max element
class  Arr10
{
	public static void main(String[] args) 
	{
		
		System.out.print("Max element:  ");
		int arr[] = {1,2,3,44,55,6};
		int max=arr[0];
		for(int i=0;i<arr.length;i++){
			
			if(arr[i]>max){
				max=arr[i];
			}
		}
		System.out.println(max );
	}
}
