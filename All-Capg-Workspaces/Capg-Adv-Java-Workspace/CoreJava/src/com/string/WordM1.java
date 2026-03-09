package com.string;

public class WordM1 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String str="snajna hello";
		// Way 2(Easy)
				String words[] = str.split(" ");
				int cnt = words.length;
				
				String regex = "[a-zA-Z]+( [a-zA-Z]+)*$";
				if(!str.matches(regex)) {
					System.out.println("Invalid String");
				}
				else {
					StringBuilder sb = new StringBuilder();
					if(cnt %2 == 0) {
						for(int i=words.length-1;i>=0;i--) {
							sb.append(words[i]+" ");
						}
					}
					else{
						for(int i=0;i<words.length;i++) {
							sb.append(new StringBuilder(words[i]).reverse()+" ");
						}
					}
					
					System.out.println(sb.toString());
				}

}
}
