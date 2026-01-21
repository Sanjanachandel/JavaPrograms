package FileClass;

import java.io.BufferedReader;

import java.io.FileReader;
;

public class ProgramReader {
	public static void main(String[] args) {
	String path="C:\\Java\\Encapsulation\\Fh";
	int file;
	try (BufferedReader br =new BufferedReader(new FileReader(path)))
			
	{
		file=br.read();
		while(file != -1) {
			System.out.print((char)file);
			file=br.read();
		}
		br.close();
	}
	catch(Exception e)
	{
		System.out.println(e.getMessage());
		e.printStackTrace();
	}
	
	
}
}
