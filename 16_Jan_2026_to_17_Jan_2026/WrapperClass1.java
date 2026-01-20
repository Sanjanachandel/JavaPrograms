package collection1;

public class WrapperClass1{
    public static void main(String[] args)
    {
        int num=42;
        Integer obj1=new Integer(num);
        Integer obj2= Integer.valueOf(num);
        Integer obj3=num;
        System.out.println("Using constructor: "+obj1);
        System.out.println("Using valueOf(): "+obj2);
        System.out.println("Using auto-boxing: "+obj3);
    }
}

