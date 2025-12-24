
 

public class CheckCharacter {
    public static void main(String[] args) {

        char ch='l';
        String result =
                (ch >= 'a' && ch <= 'z')
                        ? "Alphabet (Lowercase) - " +
                          ((ch=='a'||ch=='e'||ch=='i'||ch=='o'||ch=='u')
                                  ? "Vowel" : "Consonant")

                        : (ch >= 'A' && ch <= 'Z')
                        ? "Alphabet (Uppercase) - " +
                          ((ch=='A'||ch=='E'||ch=='I'||ch=='O'||ch=='U')
                                  ? "Vowel" : "Consonant")

                        : (ch >= '0' && ch <= '9')
                        ? "Number"

                        : "Special Character";

        System.out.println(result);
        
    }
}


/*
pubic static void main (String []args){
	char ch='i';
	boolean UC=ch>='A' &&ch<='Z';
	boolean LC=ch>='a'&& ch<='z';
	boolean alp=UC||LC;
	boolean dig=ch>='0' && ch<='9';
	boolean UVC=ch=='A'||'E'||'I'||'O'||'U';
	boolean LVC=ch=='a'||'e'||'i'||'o'||'u';
	System.out.println(
			alp?"Alphabet: "+(UC?"UpperCase : "+(UCV?"Vowel":"Consonant"):"lowerCase :"+(LCV?"Vowel":"Consonant"))
			:(dig?"Digit":"Specail Character"))
			
}
*/


