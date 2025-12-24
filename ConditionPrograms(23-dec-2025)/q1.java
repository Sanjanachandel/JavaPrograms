//Check whether character is lowercase or not
class  q1
{
	public static void main(String[] args) 
	{
	char ch = '9';
	String res = ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z')) ? "Alphabet" : "Not Alphabet";
	System.out.println(res);
	}
}