package com.string;

import java.util.LinkedHashMap;
import java.util.Map;

public class FindFirstNonRepeatingCharacter {

	public static void main(String[] args) {
		String s="programming";
		Map<Character,Integer> mp=new LinkedHashMap<>();
		for(char ch:s.toCharArray())
		{
			if(mp.containsKey(ch))
			{
				mp.put(ch,mp.get(ch)+1);
			}
			else {
				mp.put(ch, 1);
			}
		}
		for(Map.Entry<Character,Integer> e:mp.entrySet())
		{
			if(e.getValue()==1) {
				System.out.println(e.getKey());
				break;
			}
		}
	}

}
