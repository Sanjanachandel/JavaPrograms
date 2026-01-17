package Collection;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

public class ArrayList1{
	public static void main(String[] args)
	{
		ArrayList a=new ArrayList();
		a.add("java");
		a.add("C");
		a.add("cpp");
		a.add("python");
		
		Iterator i=  a.iterator();
		while(i.hasNext())
		{
			System.out.println(i.next());
		}
		i.remove();
		System.out.println();
		Iterator b=  a.iterator();
		while(b.hasNext())
		{
			
			System.out.println(b.next());
		}
		
		LinkedList l=new LinkedList();
		l.add(a);
		l.add("sanjana");
//		l.remove(1);
		System.out.println(l);
		
		
	}
}
