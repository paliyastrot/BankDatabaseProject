package implementation;

import java.sql.Date;

public class Customer 
{
	String account_no;
	String customer_id;
	String first_name;
	String last_name;
	String aadhar_id;
	String contact_no;
	Date date_of_birth;
	double balance;
	
	
	public Customer()
	{
		this.account_no=null;
		this.aadhar_id=null;
		this.first_name=null;
		this.last_name=null;
		this.contact_no=null;
		this.balance=0.00;
		this.date_of_birth=null;
	}
	public Customer(String account_no , String first_name, String last_name, String contact_no, Date date_of_birth, String aadhar_id, String customer_id)
	{
		this.account_no = account_no;
		this.first_name = first_name;
		this.last_name = last_name;
		this.aadhar_id = aadhar_id;
		this.contact_no = contact_no;
		this.customer_id = customer_id;
		this.date_of_birth = date_of_birth;
		this.balance = 0;
	}
	
	public String getAcocunt()
	{
		return account_no;
	}
	
	public double getBalance()
	{
		return balance;
	}

}
