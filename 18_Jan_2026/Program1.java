package FileClass;

public class Program1 {

	int id;
	String name;
	String dept;
	double salary;
	public Program1(int id, String name, String dept, double salary) {
		super();
		this.id = id;
		this.name = name;
		this.dept = dept;
		this.salary = salary;
	}
	@Override
	public String toString() {
		return "Program1 [id=" + id + ", name=" + name + ", dept=" + dept + ", salary=" + salary + "]";
	}
	

}
