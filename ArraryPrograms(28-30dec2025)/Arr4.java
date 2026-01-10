class  Arr4
{
	public static void main(String[] args) 
	{
		int arr[] = {1,2,3,4,5};
		System.out.println("odd index elements : ");
		for(int i=0;i<arr.length;i++){
			System.out.println(arr[i+i]);
		}
	}
}
