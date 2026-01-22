class c10 
{
	public static void main(String[] args) 
	{
		int a=1;
		int b=20;
		int sum=0;
		for(int i=a;i<=b;i++){
			if(i%5==0){
				sum=sum+i;
			}
		}
		System.out.println("sum of number who are divisible by 5 btw 1 and 20= "+sum);
	}
}
