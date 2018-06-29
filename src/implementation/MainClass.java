package implementation;

import java.util.Scanner;

public class MainClass 
{
	public static void main(String args[])
	{
		System.out.println("\t\t\t\tWelcome to Banking System");
		Scanner scan = new Scanner(System.in);
		while(true)
		{
			Bank b =new Bank();
			System.out.println("Enter your choice\n1.Create New Account\n2.Existing User\n3.Exit");
			int choice = scan.nextInt();
			switch(choice)
			{
			case 1:
				b.openAccount();
				System.out.println("Exiting...!!!\nPlease login again using Customer ID to continue using your account.");
				break;
			case 2:
				b.login();
				break;
			case 3:
				System.exit(0);
				break;
			}		
		}
	}
}
