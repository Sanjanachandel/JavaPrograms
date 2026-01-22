class  c5
{
	public static void main(String[] args) 
	{
		int a=1;
		int b=12;
		int evenS=0;
		int oddS=0;
		for(int i=a;i<=b;i++){
			if(i%2==0){
				evenS=evenS+i;
				
			}
			
			else{
				oddS=oddS+i;
				
			}
		}
		System.out.println("even number sum is "+evenS);
		System.out.println("odd number sum is "+oddS);
	}
}
