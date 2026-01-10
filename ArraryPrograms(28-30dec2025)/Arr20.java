class  Arr20
{
	public static void main(String[] args) 
	{
		int arr[]={12,45,8,96,74,25};
		int fhmax=arr[0];
		for(int i=0;i<arr.length/2;i++){
			
				if(arr[i]>fhmax)
					{
						fhmax=arr[i];
					}
			
		}
		System.out.println("First half max element: "+fhmax);
	}
}
