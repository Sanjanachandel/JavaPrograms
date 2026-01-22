package FileHandling;



import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class StudentSerializable {

	public static void main(String[] args) {
		List <Student> l1 = new ArrayList<>();
		
		l1.add(new Student(101,"Amit",89.0));
		l1.add(new Student(101,"Sameer",94.0));
		l1.add(new Student(101,"Rajesh",83.0));
		
		String path = ("C:\\Java\\Encapsulation\\StudentDetails.ser");
		
		try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path))){
			oos.writeObject(l1);
			System.out.println("Student list Serialized Successfully");
			oos.close();
		}
		
		catch(Exception e) {
			e.printStackTrace();
		}
		

	}

}