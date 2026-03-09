package com.string;

public class ReverseString {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String str = "Hello Java World";
		
		StringBuilder sb1 = new StringBuilder();
		StringBuilder sb2 = new StringBuilder();
		
		for (int i = str.length() - 1; i >= 0; i--) {

            char ch = str.charAt(i);
			if(ch != ' ') {
				sb1.append(ch);
			}
			else {
				sb2.append(sb1.reverse());
				sb2.append(' ');
				sb1.setLength(0);
			}
		}
		
		sb2.append(sb1.reverse());
		System.out.println(sb2.toString());

	}

}
