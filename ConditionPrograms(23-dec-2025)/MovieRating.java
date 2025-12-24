import java.util.Scanner;
class  MovieRating
{
	public static void main(String[] args) 
	{
		Scanner sc=new Scanner(System.in);
		int rat=0;
		do{
			System.out.println("Enter rating 1-4");
			System.out.println("1-poor");
			System.out.println("2-average");
			System.out.println("3-good");
			System.out.println("4-excellent");
			 rat=sc.nextInt();
			switch(rat){
				case 1:
					System.out.println("poor");
					break;
				case 2:
					System.out.println("average");
					break;
				case 3:
					System.out.println("good");
					break;
				case 4:
					System.out.println("excellent");
					break;
					
			}
		}while(rat!=5);
			
	}
}
