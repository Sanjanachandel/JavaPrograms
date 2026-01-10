 class  Arr11
{
	public static void main(String[] args) 
	{
		
		System.out.print("Max element:  ");
		int arr[] = {10,12,3,44,55,6};
		int min=arr[0];
		for(int i=0;i<arr.length;i++){
			
			if(arr[i]<min){
				min=arr[i];
			}
		}
		System.out.println(min );
	}
}
 

