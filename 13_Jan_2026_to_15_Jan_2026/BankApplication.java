package ObjectHandling;

public class BankApplication{
    public static void main(String[] args)
    {
        Account ac=new Account(555);
     Account a=new Account(555);
     if(ac.hashCode()==a.hashCode()){
        System.out.println("true");
     }

        


    }
}
class Account{
    public int acnum;
    public Account(int acnum){
        this.acnum=acnum;
    }
@Override
public int hashCode(){
    return acnum;
}
}
