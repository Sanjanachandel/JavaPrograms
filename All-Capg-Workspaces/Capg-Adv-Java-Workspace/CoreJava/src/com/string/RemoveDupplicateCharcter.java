package com.string;

import java.util.LinkedHashSet;
import java.util.Set;

public class RemoveDupplicateCharcter {

	public static void main(String[] args) {
		String s="programming";
		Set<Character>ss=new LinkedHashSet<>();
		StringBuilder sb=new StringBuilder();
		for(char ch:s.toCharArray())
		{
			if(ss.add(ch)) {
				sb.append(ch);
			}
		}
		System.out.println(sb);
	}

}
