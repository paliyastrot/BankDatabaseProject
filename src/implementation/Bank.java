package implementation;

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Random;
import java.util.Scanner;

public class Bank
{
	
	DBService dbservice = new DBService();
	Customer get_account = null;
	public void openAccount()
	{
		Scanner scan = new Scanner(System.in);
		System.out.print("Enter your First Name : \t");
		String first_name = scan.next();
		System.out.print("Enter your Last Name : \t");
		String last_name = scan.next();
		System.out.print("Enter Contact Number : \t");
		String contact_no = scan.next();
		Date date_of_birth = readDate();
		String account_no = getAccountNumber();
		System.out.print("Enter Aadhar ID : \t");
		String aadhar_id = scan.next();
		String customer_id = first_name.substring(0, 3).toUpperCase() + account_no.substring(account_no.length()-4, account_no.length()); 
				
		//creating an Account Type object 
		Customer newAccount = new Customer(account_no, first_name, last_name, contact_no, date_of_birth, aadhar_id, customer_id); 
		//Savings Customer info in Database
		
		dbservice.openAccount(newAccount);
		System.out.println("Your Account Number is : "+ account_no);
		System.out.println("Your customer ID is : "+ customer_id);
	}
	
	//Generating Account Number 
	String getAccountNumber()
	{
		Random rand = new Random();
		int acc = rand.nextInt(900000000)+100000000;
		String account_no = Integer.toString(acc);
		return account_no;
	}
	
	//Reading Date
	public Date readDate()
	{
		System.out.print("Enter Date of Birth(format - \"yyyy-mm-dd\") : \t");
		Scanner scan = new Scanner(System.in);
		String dateString = scan.next();
		return java.sql.Date.valueOf(dateString);
	}
	
	
	public void login()
	{
		
		Scanner scan = new Scanner(System.in);
		System.out.println("Enter your customer ID: ");
		String customer_id = scan.next();
		get_account = dbservice.getAccount(customer_id);
		openMenu();
		return;
	}
	
	
	public void openMenu()
	{
		Scanner scan = new Scanner(System.in);
		while(true)
		{
			
			System.out.println("1.Get Account Details\n2.Credit Amount\n3.Debit Amount\n4.Print Passbook\n5.Delete Account\n6.Exit\n");
			System.out.println("Enter the choice :");
			int choice = scan.nextInt();
			switch(choice)
			{
			case 1: 
				getDetails();
				break;
			case 2:
				try 
				{
					dbservice.credit(get_account);
					break;
				} 
				catch (InvalidAmountException e) 
				{
					
					e.printStackTrace();
				}
			case 3:
				dbservice.debit(get_account);
				break;
			case 4:
				getPassbook();
				break;
			case 5: 
				delete();
				return ;
				
			case 6:
				get_account = null;
				System.exit(0);
			}
		}
	}
	
	public void getDetails()
	{
		System.out.println("Account Number : "+get_account.account_no);
		System.out.println("Customer _ID : "+ get_account.customer_id);
		System.out.println("Account Holder Name : "+ get_account.first_name + " " + get_account.last_name);
		System.out.println("Contact Number : "+ get_account.contact_no);
		System.out.println("Date of Birth : "+ get_account.date_of_birth);
		System.out.println("Aadhar Number : "+get_account.aadhar_id);
		System.out.println("Account Balance : "+ get_account.balance);
		System.out.println("\n\n");
		
	}
	
	public void getPassbook()
	{
		System.out.println("Account Number : "+ get_account.account_no+"\t\t\t\t\t"+ "Account Holder Name : "+ get_account.first_name+" "+get_account.last_name);
		System.out.println("The details of the transaction is Shown below\n");
		dbservice.Passbook(get_account.account_no);
		System.out.println("\n\n");
	}
	
	
	public void delete()
	{
		dbservice.deleteAccount(get_account.account_no);
		System.out.println("Account Successfully Deleted\n\n");
	}
}
