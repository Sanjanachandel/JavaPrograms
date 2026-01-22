package FileHandling;



import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.List;

public class StudentDesiralizable {

	public static void main(String[] args) {
		String path = ("C:\\Java\\Encapsulation\\StudentDetails.ser");
		
		try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path))){
			List<Student> l1 = (List<Student>) ois.readObject();
			
			for(Student s:l1) {
				System.out.println(s);
				
			}
			System.out.println("Student Desialized Successfully");
			
			ois.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}

	}

}
