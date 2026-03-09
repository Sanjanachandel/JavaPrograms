package com.string;

import java.util.LinkedHashSet;
import java.util.Set;

public class FirstRepeatingCharcter {

	public static void main(String[] args) {
			String s="sannnjana";
			Set<Character >ss=new LinkedHashSet<>();
			for(char ch:s.toCharArray())
			{
				if(!ss.add(ch))
				{
					System.out.println(ch);
					break;

				}
			}
	}

}
