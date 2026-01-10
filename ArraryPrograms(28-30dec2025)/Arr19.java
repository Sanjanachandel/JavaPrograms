class  Arr19
{
	public static void main(String[] args) 
	{
		int arr[]={12,45,8,96,74,25};
		int emax=arr[0];
		for(int i=0;i<arr.length;i++){
			if(i%2==0){
				if(arr[i]>emax)
					{
						emax=arr[i];
					}
			}
		}
		System.out.println("Max even element: "+emax);
	}
}
