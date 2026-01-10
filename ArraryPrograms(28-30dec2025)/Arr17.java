class Arr17
{
	public static void main(String[] args) 
	{
		int arr[] ={92,41,58,91,35,67};
		int n=arr.length;
		for(int i=0;i<n;i++){
			for(int j=i+1;j<n;j++){
				if(arr[i]<arr[j]){
					int temp=arr[i];
					arr[i]=arr[j];
					arr[j]=temp;
				}
				
			}
		}
		System.out.println("second highest element: "+ arr[1]);
		
	}
}
