class  Arr7
{
	public static void main(String[] args) 
	{
		int sum=0;
		System.out.println("odd index elmenets sum: ");
		int arr[] = {1,2,3,4,5};
		for(int i=0;i<arr.length;i=i+2){
			sum=sum+arr[i];
		}
		System.out.print(sum);
	}
}
