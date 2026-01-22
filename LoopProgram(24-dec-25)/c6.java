class  c6
{
	public static void main(String[] args) 
	{
		int a=1;
		int b=5;
		int evenP=1;
		int oddP=1;
		for(int i=a;i<=b;i++){
			if(i%2==0){
				evenP=evenP*i;
				
			}
			
			else{
				oddP=oddP*i;
				
			}
		}
		System.out.println("even number sum is "+evenP);
		System.out.println("odd number sum is "+oddP);
	}
}
