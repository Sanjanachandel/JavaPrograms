package collection1;

public class BankingTranscationSystem {
    public static void main(String[] args) {
		Account account = new Account("A001", 10000);

        Transaction t1 = new Transaction("T001", TransactionType.DEPOSIT, 5000);
        Transaction t2 = new Transaction("T002", TransactionType.WITHDRAWAL, 3000);
        Transaction t3 = new Transaction("T003", TransactionType.WITHDRAWAL, 15000);

        TransactionService service = new TransactionService();

        service.executeTransaction(account, t1);
        service.executeTransaction(account, t2);
        service.executeTransaction(account, t3);

	}
}

enum TransactionType {
    DEPOSIT{
    	@Override
        public boolean apply(Account account,double amount){
        	account.setCurrentBalance(account.getCurrentBalance() + amount);
        	System.out.println("Deposit of "+(int)amount +" successful. Balance: "+(int)account.getCurrentBalance());
        	return true;
        }
    },

    WITHDRAWAL{
    	@Override
        public boolean apply(Account account,double amount){
            if(account.getCurrentBalance() >= amount) {
            	account.setCurrentBalance(account.getCurrentBalance() - amount);
            	System.out.println("Withdrawal of " + (int)amount +" successful. Balance: " + (int)account.getCurrentBalance());
            	return true;
            }
            else {
                System.out.println("Withdrawal failed due to insufficient balance");
                return false;
            }
        }
    },

    TRANSFER{
    	@Override
        public boolean apply(Account account,double amount){
            return false;
        }
    };
    
    public abstract boolean apply(Account account, double amount);
}

class Account{
    private String accountNum;
    private double currentBalance;

    Account(String accountNum,double currentBalance){
        this.accountNum = accountNum;
        this.currentBalance = currentBalance;
    }

    public void applyTransaction(double amount,TransactionType transactionType){
        transactionType.apply(this,amount);
    }

    public void displayBalance(){
        System.out.println("Current Balance: "+currentBalance);
    }

    public double getCurrentBalance(){
        return currentBalance;
    }

    public void setCurrentBalance(double currentBalance){
        this.currentBalance = currentBalance;
    }
}

class Transaction{
    private String transactionId;
    private TransactionType transactionType;
    private double amount;

    Transaction(String transactionId,TransactionType transactionType,double amount){
        this.transactionId = transactionId;
        this.transactionType = transactionType;
        this.amount = amount;
    }
    
    public TransactionType getTransactionType() {
    	return transactionType;
    }
    
    public double getAmount() {
    	return amount;
    }
}

class TransactionService{
	public void executeTransaction(Account account,Transaction transaction) {
		account.applyTransaction(transaction.getAmount(), transaction.getTransactionType());
	}
}