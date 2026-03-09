package com.string;

public class ReplaceAllVowel {

	public static void main(String[] args) {
		String s="programming";
		StringBuilder ss=new StringBuilder();
		for(char ch :s.toCharArray())
		{
			if("aeiouAEIOU".indexOf(ch)!=-1)
			{
				ss.append("*");
			}
			else
			{
				ss.append(ch);
			}
		}
		System.out.println(ss);
	}

}
