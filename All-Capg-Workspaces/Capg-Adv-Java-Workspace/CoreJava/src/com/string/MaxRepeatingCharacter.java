package com.string;

import java.util.LinkedHashMap;
import java.util.Map;

public class MaxRepeatingCharacter {

	public static void main(String[] args) {
		String str="programmmming";
		Map<Character,Integer>mp=new LinkedHashMap<>();
		for(char ch:str.toCharArray())
		{
			if(mp.containsKey(ch))
			{
				mp.put(ch, mp.get(ch)+1);
			}
			else
			{
				mp.put(ch, 1);
			}
		}
		int maxi=0;
		char ch=' ';
		for(Map.Entry<Character, Integer>e:mp.entrySet())
		{
			if(e.getValue()>maxi)
			{
				maxi=e.getValue();
				ch=e.getKey();			}
		}
		System.out.println(ch+" = "+maxi);
	}

}
