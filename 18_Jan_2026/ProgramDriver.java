package FileClass;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class ProgramDriver {
	

	public static void main(String[] args)
	{
		List<Program1> l=new ArrayList<>();
		
		l.add(new Program1(101, "  sanjana", "Developer", 1000000000.0));
		l.add(new Program1(102, "  sagar", "Tester", 1000000000.0));
		l.add(new Program1(103, "  Dikshya", "Developer", 1000000000.0));
		l.add(new Program1(104, "  Harsh", "Developer", 1000000000.0));
		l.add(new Program1(105, "  Hunny", "Developer", 1000000000.0));
		l.add(new Program1(106, "  Shashi", "Developer", 1000000000.0));
		String path="C:\\Java\\Encapsulation\\Fh";
		try (BufferedWriter bw =new BufferedWriter(new FileWriter(path)))
				
		{
			bw.write("ID     Name      Dept     Salary");
			bw.newLine();
			bw.write("---------------------------------------------------");
			bw.newLine();
			for(Program1 e: l) {
				bw.write(e.toString());
				bw.newLine();
			}
			
			bw.close();
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
		
	}
}

