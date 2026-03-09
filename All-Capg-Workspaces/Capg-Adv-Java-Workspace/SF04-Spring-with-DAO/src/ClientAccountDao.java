import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

//ClientAccountDao.java
public class ClientAccountDao {
	public static void main(String[] args)  //Step :1
	{  
		
ApplicationContext factory = new ClassPathXmlApplicationContext("applicationContext_Dao.xml"); //Step :2
		
AccountDao accDao = (AccountDao)factory.getBean("accountDao"); //Step :3

     // Bean
		Account account = new Account();  //Step 12
		account.setAccno(45);             //Step 13
		account.setAccName("Ravi");
		account.setAccType("Saving1");
		account.setBal(7777.00);
			//System.out.println(account);
		// save bean data into database 	
		accDao.save(account);    // Step 14
			
		// retriving data form beans class         Step 17 
	    System.out.println("Account No : "+account.getAccno());
		System.out.println("Account Name : "+account.getAccName());
		System.out.println("Account Type :"+account.getAccType());
		System.out.println("Account Balance :"+account.getBal());
		
		
	}  //Step 18 close main
}    //Step 19 close class
