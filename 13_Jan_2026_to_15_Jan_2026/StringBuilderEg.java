package ObjectHandling;

public class StringBuilderEg{
    public static void main(String[] args){
        StringBuilder sb=new StringBuilder("HelloJava");
        sb.delete(5,9);
        System.out.println(sb);
    }
}
