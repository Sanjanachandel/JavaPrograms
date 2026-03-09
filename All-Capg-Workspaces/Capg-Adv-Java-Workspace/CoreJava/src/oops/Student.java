package oops;

import java.util.ArrayList;
public class Student{
	 public static void main(String[] args)
	 {
	 	StudentManagement s=new  StudentManagement();
	 	s.addStudent(new Student1(1,"sankana"));
	 	s.addStudent(new Student1(2,"mohan"));
	 	s.display();
	 	s.updateStudent(1,"sana");
	 	
	 	s.display();
	 }
}
class Student1 {
	int sid;
	String name;
	Student1(int sid, String name)
	{
		this.sid=sid;
		this.name=name;
	}
	
		
}
 class StudentManagement{
	ArrayList<Student1> s=new ArrayList<>();
	public void addStudent(Student1 ss)
	{
		s.add(ss);
	}
	public void updateStudent(int id, String name)
	{
		for(Student1 s1:s)
		{
			if(s1.sid==id)
			{
				s1.name=name;
				System.out.println(s1.name);
				
			}
			break;
		}
	}
	public void display()
	{
		for(Student1 w:s)
		{
			System.out.println(w.name);
		}
	}
}
 


 
