class  Arr21
{
	public static void main(String[] args) 
	{
		int arr[]={12,45,8,95,74,25};
		int shmax=arr[0];
		for(int i=arr.length/2;i<arr.length;i++){
			
				if(arr[i]>shmax)
					{
						shmax=arr[i];
					}
			
		}
		System.out.println("Second half max  element: "+shmax);
	}
}
