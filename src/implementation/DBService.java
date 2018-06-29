package implementation;

import java.util.*;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;

public class DBService 
{
	
    				
    private Connection connect() 
    {
    	Connection connection = null;
    	try 
    	{
            Class.forName("com.mysql.jdbc.Driver");     
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bankmanagement?useSSL=false","root","12345");

	    }
    	catch (SQLException e)
    	{
            connection = null;
            System.out.println("Connection Interrputed!!!!!");
    	}
    	catch (ClassNotFoundException e)
    	{
			e.printStackTrace();
		}
    	return connection;
	}
    
    
    int openAccount(Customer newAccount)
    {
    	Connection conn = connect();
        try 
        {		
        	String sqlQuery = " INSERT INTO customer_info (cust_id, first_name, last_name, contact_no, date_of_birth, account_number, aadhar_id, account_balance)"
        	        + " VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
         
            PreparedStatement preparedStmt = conn.prepareStatement(sqlQuery);
			preparedStmt.setString(1, newAccount.customer_id);
			preparedStmt.setString(2, newAccount.first_name);
			preparedStmt.setString(3, newAccount.last_name);
			preparedStmt.setString(4, newAccount.contact_no);
			preparedStmt.setDate(5, newAccount.date_of_birth);
			preparedStmt.setString(6, newAccount.account_no);
			preparedStmt.setString(7, newAccount.aadhar_id);
			preparedStmt.setDouble(8, 0.00);
			preparedStmt.executeUpdate();
	
        
			try
			{
				
				String accountAdd = " INSERT INTO account_details (account_number, customer_id, opening_balance, account_opening_date, account_type, account_status)"
						+ " VALUES (?, ?, ?, ?, ?, ?)";
				PreparedStatement addAccount = conn.prepareStatement(accountAdd);
				addAccount.setString(1, newAccount.account_no);
				addAccount.setString(2, newAccount.customer_id);
				addAccount.setDouble(3, 0.00);
				//Inserting Date
				java.sql.Date sqlDate = new java.sql.Date(new java.util.Date().getTime());
				addAccount.setDate(4, sqlDate);
				addAccount.setString(5, "SAVINGS");
				addAccount.setString(6, "OPEN");
				addAccount.executeUpdate();
				System.out.println("Account Created Successfully..!!!");
			}
			
			catch (SQLException e) 
	        {
				e.printStackTrace();
			}
			
			
        }
        catch (SQLException e) 
        {
			e.printStackTrace();
		}

    	return 0;
    }
    
    
    public void login(String customer_id)
    {
    	Connection conn = connect();
    	String sqlQuery = " SELECT account_no from customer_info WHERE cust_id = ?";
		try 
		{
			PreparedStatement stmt = conn.prepareStatement(sqlQuery);
			ResultSet result =  stmt.executeQuery(sqlQuery);
			System.out.println(result);
			while(result.next())
				System.out.println(result.getString("account_no"));
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
    }
    
   
  //Update (Edit Account)
    boolean UpdateAccountBalance(String account_number, Double newBalance)
    {
        boolean success = false;
        Connection connection = connect();
        try
        {
        	PreparedStatement updateBalance = connection.prepareStatement("UPDATE customer_info SET account_balance = ? WHERE account_number = ?");
            updateBalance.setDouble(1, newBalance);
            updateBalance.setString(2, account_number);
            updateBalance.executeUpdate();
            PreparedStatement updateBal = connection.prepareStatement("UPDATE account_details SET opening_balance = ? WHERE account_number = ?");
            updateBal.setDouble(1, newBalance);
            updateBal.setString(2, account_number);
            updateBal.executeUpdate();
            success = true;
        }
        catch (SQLException ex)
        {
            System.err.println("An error has occured." + ex.getMessage());
        }
        return success;
    }
    
    
    public Customer getAccount(String customer_id)
    {
 
    	Customer get_account = new Customer() ;
    	Connection connection = connect();
    	try
    	{
    		
    		String sqlQuery = "SELECT * FROM customer_info WHERE cust_id = ?";
    		PreparedStatement getAcc = connection.prepareStatement(sqlQuery);
    		getAcc.setString(1, customer_id);
    		ResultSet result = getAcc.executeQuery();
    		result.next();
    		get_account.customer_id=result.getString(1);
    		get_account.first_name= result.getString(2);
    		get_account.last_name = result.getString(3);
    		get_account.contact_no = result.getString(4);
    		get_account.date_of_birth = result.getDate(5);
    		get_account.account_no = result.getString(6);
    		get_account.aadhar_id = result.getString(7);
    		get_account.balance = result.getDouble(8);

    	}
    	
    	catch(SQLException e)
    	{
    		e.printStackTrace();
    	}
    	
		return get_account;
    }
    
    public void debit(Customer account)
    {
    	Scanner scan = new Scanner(System.in);
    	System.out.println("Enter the amount you want to withdraw : ");
    	double amount = scan.nextDouble();
    	account.balance = account.balance-amount;
    	System.out.println("Debit Successful\nUpdated Balance : "+account.balance+"\n");

    	UpdateAccountBalance(account.account_no, account.balance);
    	saveTransactionDetails("Debit", amount, account.account_no);
    }
    
    
    public void saveTransactionDetails(String type, double amount, String account_no)
    {
    	Connection conn = connect();
    	String sqlQuery = " INSERT INTO transaction_details (transaction_id, account_number, date_of_transaction, transaction_type, transaction_amount, transaction_time)"
	        + " VALUES (?, ?, ?, ?, ?, ?)";
    	try
    	{
        	PreparedStatement saveTransaction = conn.prepareStatement(sqlQuery);
        	saveTransaction.setString(1, Integer.toString(new Random().nextInt(10000000)));
        	saveTransaction.setString(2, account_no);
        	saveTransaction.setDate(3, java.sql.Date.valueOf(LocalDate.now()));
        	saveTransaction.setString(4, type);
        	saveTransaction.setDouble(5, amount);
        	saveTransaction.setTime(6, java.sql.Time.valueOf(LocalTime.now()));
        	saveTransaction.executeUpdate();
        	System.out.println("Transaction Details Successfully saved.\n\n");
    	}
    	catch(SQLException ex)
    	{
    		ex.printStackTrace();
    	}
    }
    
    public void credit(Customer account) throws InvalidAmountException    
    {
    	Scanner scan = new Scanner(System.in);
    	System.out.println("Enter the amount to be credited : ");
    	double amount= scan.nextDouble();
    	if(amount<=0.00)
    		throw new InvalidAmountException();
    	else
    		account.balance = account.balance + amount;
    	
    	UpdateAccountBalance(account.account_no, account.balance);
    	saveTransactionDetails("Credit", amount, account.account_no);
    	System.out.println("Credit Successful\n Updated Balance is : " + account.balance);
    }
    
    
    public void Passbook(String account_no)
    {
    	Connection con = connect();
    	try
    	{
    		
    		String sqlQuery = "SELECT * from transaction_details WHERE account_number = ?";
    		String sqlOrder ="SELECT * FROM bankmanagement.transaction_details ORDER BY date_of_transaction, transaction_time ASC";
    		
        	PreparedStatement getpassbook = con.prepareStatement(sqlOrder);
        	getpassbook.executeQuery();
        	getpassbook = con.prepareStatement(sqlQuery);
        	getpassbook.setString(1, account_no);
    		ResultSet result = getpassbook.executeQuery();
    		System.out.println("Transaction ID\t\tTransaction Date\t\tTransaction Time\t\tTransaction Type\t\tTransaction Amount");
    		while(result.next())
    		{
    			System.out.print(result.getString(1)+"\t\t\t");
    			System.out.print(result.getDate(3)+"\t\t\t");
    			System.out.print(result.getTime(6)+"\t\t\t");
    			System.out.print(result.getString(4)+"\t\t\t\t");
    			System.out.println(result.getDouble(5)+"\t\t\t");
    			
    		}

    	}
    	catch(SQLException ex)
    	{
    		ex.printStackTrace();
    	}
    	
    }
    
    
    public void deleteAccount(String account_no)
    {
    	Connection con = connect();
    	String sqlDelete = "DELETE FROM account_details WHERE account_number = ?";
    	String sqlDel = "DELETE FROM customer_info WHERE account_number = ?";
    	String sqlDelTD = "DELETE FROM transaction_details WHERE account_number = ?";
    	PreparedStatement statement;
		try 
		{
			statement = con.prepareStatement(sqlDelete);
	    	statement.setString(1, account_no);
	    	statement.executeUpdate();
	    	statement = con.prepareStatement(sqlDel);
	    	statement.setString(1, account_no);
	    	statement.executeUpdate();
	    	statement = con.prepareStatement(sqlDelTD);
	    	statement.setString(1, account_no);
	    	statement.executeUpdate();
		} 
		catch (SQLException e)
		{
			e.printStackTrace();
		}
    }
    
    
}
