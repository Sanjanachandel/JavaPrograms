package OnlinebankingSystem1;
public class Account {
    private int accountNumber;
    private String accountHolderName;
    private double balance;

    Account(int accountNumber,String accountHolderName,double balance){
        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
        this. balance = balance;
    }

    public int getAccountNumber(){
        return this.accountNumber;
    }

    public double getBalance(){
        return this.balance;
    }

    public void deposit(double amount){
        if(amount <= 0){
            throw new IllegalArgumentException("Amount cannot be negative or zero");
        }

        balance += amount;
    }

    public void withdraw(double amount) throws InsufficientFundsException{
        if(amount <= 0 ){
            throw new IllegalArgumentException("Amount cannot be negative or zero");
        }

        if(amount > balance){
            throw new InsufficientFundsException("Insufficient funds in account "+accountNumber);
        }

        balance -= amount;
    }

    @Override
    public String toString(){
        return "Account[AccountNumber="+accountNumber+", Holder="+accountHolderName+", Balance="+balance+"]";
    }
}