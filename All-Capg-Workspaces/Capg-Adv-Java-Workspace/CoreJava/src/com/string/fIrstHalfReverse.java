package com.string;

import java.util.LinkedHashSet;
import java.util.Set;

public class fIrstHalfReverse {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		//Given a string:
		//Convert to lowercase
		//Remove all non-alphabetic characters
		//Remove duplicate characters (order preserved)
		//Reverse only the first half of the string
		String str="jJAaahello123@jk";
		str=str.toLowerCase();
		StringBuilder sb=new StringBuilder();
		for(char ch:str.toCharArray())
		{
			if(Character.isAlphabetic(ch))
				
			{
				sb.append(ch);
			}
		}
		Set<Character> ss=new LinkedHashSet<>();
		for(char ch:sb.toString().toCharArray())
		{
			ss.add(ch);
		}
		StringBuilder unique = new StringBuilder();
		for(char ch:ss) {
			unique.append(ch);
		}
		
		
		// 4.reverse only first half
		int n = unique.length();
		int mid = n/2;
		
		String firstHalf = unique.substring(0,mid);
		String secondHalf = unique.substring(mid);
		
		String res = new StringBuilder(firstHalf).reverse().toString() + secondHalf;
		System.out.println(res);
	}

}
