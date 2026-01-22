package FileHandling;
import java.io.Serializable;

public class Student implements Serializable{
	
	private static final long serialVersionUID = 8373460913594053566L;
	
	private int id;
	private String name;
	private double marks;
	
	public Student(int id, String name, double marks) {
		super();
		this.id = id;
		this.name = name;
		this.marks = marks;
	}
	
	@Override
	public String toString() {
		return "ID: "+id+" Name: "+name+" Marks: "+marks; 
	}
	
}