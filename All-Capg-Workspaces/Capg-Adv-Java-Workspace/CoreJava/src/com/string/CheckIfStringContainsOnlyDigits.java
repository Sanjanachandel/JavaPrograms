package com.string;

public class CheckIfStringContainsOnlyDigits {

	public static void main(String[] args) {
		String s="123s57";
		boolean flag=true;
		for(char ch:s.toCharArray())
		{
			if(!Character.isDigit(ch))
			{
				flag=false;
				break;
			}
		}
		if(flag)
		{
			System.out.println("only digit");
		}
		else System.out.println("mix");
	}

}
