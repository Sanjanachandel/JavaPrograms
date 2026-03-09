package com.string;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

public class practice {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		String n="madam";
//	 StringBuilder nn=new StringBuilder(n).reverse();
//	String n1= nn.toString();
//	 if(n.equals(n1)) {
//	 System.out.println("Plaindrome");
//	}
		
//		String n="education";
//		
//		
//		int v=0;
//		int c=0;
//		for(int i=0;i<n.length();i++)
//		{
//			char ch=n.charAt(i);
//			if(ch=='a'||ch=='e'||ch=='i'||ch=='o'||ch=='u')
//			{
//				v++;
//			}
//			else c++;
//		}
//		System.out.println(v);
//		System.out.println(c);
//		String name="San1JAna12";
//		int k=2;
//		String res1="";
//		
//		String res= new StringBuilder(name).reverse().toString();
//		for(char ch :res.toCharArray())
//		{
//			if(Character.isUpperCase(ch))
//			{
//				res1+=Character.toLowerCase(ch);
//			}
//			else
//				res1+=Character.toUpperCase(ch);
//		}
//		String res2=res1.replaceAll("\\d","");
//		
//		int l=res2.length();
//		 k%=l;
//		 String res3="";
//		  res3+=res2.substring(l-k)+res2.substring(0,l-k);
//		  System.out.println(res2);
//		System.out.println(res3);
//		String s="programming";
//		StringBuilder sb=new StringBuilder();
//		for(int i=0;i<s.length();i+=2)
//		{
//			if(i+1<s.length())
//			{
//				sb.append(s.charAt(i+1));
//				sb.append(s.charAt(i));
//			}
//			else sb.append(s.charAt(i));
//		}
//		String ss=sb.toString();
//		ss=ss.replaceAll("[AEIOUaeiou]","*");
//		Set<Character> n=new LinkedHashSet<>();
//		for(char ch :ss.toCharArray())
//		{
//			n.add(ch);
//		}
//		String new1="";
//		for(char ch:n) {
//		 new1 +=ch;
//		}
//		System.out.println(new1);
//		
		String in="bannnana";
		int c=1;
		String res="";
		Map<Character,Integer>mp=new TreeMap<>();
		for(char ch:in.toCharArray())
		{
			if(mp.containsKey(ch))
			{
				mp.put(ch,mp.get(ch)+1 );
			}
			else mp.put(ch,1);
		}
		String neew="";
		for(Map.Entry<Character,Integer>e:mp.entrySet())
			
		{
			neew+=e.getKey();
			neew+=e.getValue();
		}
		
		System.out.println(neew);
		
		
		
	}

}
