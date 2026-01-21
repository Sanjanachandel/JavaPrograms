package FileClass;
import java.io.FileInputStream;


public class FoodApp2 {

	public static void main(String[] args) {
		String path = ("C:\\Java\\Encapsulation\\food.txt");
		
		try(FileInputStream fis = new FileInputStream(path)){
		//byte b[] = new byte[fis.available()];
			int content=fis.read();
			while(content != -1) {
				System.out.print((char)content);
				content=fis.read();
			}
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}

	}

}