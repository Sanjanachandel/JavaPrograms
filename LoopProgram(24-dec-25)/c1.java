//WAP TO PRINT EVEN ALPHABET BY USING FOR LOOP?
import java.util.Scanner; 
class c1
{
	public static void main(String[] args) 
	{
		for(char ch='A';ch<='Z';ch++){
			if(ch%2==0){
				System.out.print(ch+" ");
			}
		}
		
	}
}
