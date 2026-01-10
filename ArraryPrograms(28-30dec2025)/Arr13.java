class Arr13 
{
	public static void main(String[] args) 
	{
		int arr[] ={92,41,58,69,35,67};
		int n=arr.length;
		for(int i=0;i<n;i++){
			for(int j=i+1;j<n;j++){
				if(arr[i]>arr[j]){
					int temp=arr[i];
					arr[i]=arr[j];
					arr[j]=temp;
				}
				
			}
		}
		System.out.println("Ascending order: ");
		for(int i=0;i<n;i++){
			System.out.print(arr[i]+" ");
		}
	}
}
