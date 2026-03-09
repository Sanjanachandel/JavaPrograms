package com.string;

public class CountWords {

	public static void main(String[] args) {
		String s="hello world ";
		
		int c=0;
		
		if(s.length()>0)
		{
			c++;
		}
		
		for(char ch:s.toCharArray())
		{
			if(ch==' ')
			{
				c++;
			}
		}
		System.out.println("count "+c);
	}

}
