/*
 * This program is the backend of our banking system.
 * It reads in the previous day's master accounts file and then applies all
 * the transactions in the set of daily transaction files to the the accounts to
 * produce today's new master accounts file.
 *
 * (C) 2017 ABLSolutions.Inc
 */

package backend;

import java.io.IOException;
import java.util.Scanner;

/*
 * The main class parses and executes commands and core function of the program
 */
public class Main {
	static MasterAccountsFile accounts;

	/*
	 * The purpose of this function is to call on all essential
	 * methods(deposit,withdraw,transfer,etc.) and classes(e.g MasterAccountsFile)
	 * and throws all input and output exceptions.
	 */
	public static void main(String[] args) throws IOException {
		Scanner transactions = new Scanner(args[0]);
		accounts = new MasterAccountsFile(args[1]);
		
		while(transactions.hasNextLine()) {
			String[] line = transactions.nextLine().split(" ", 5);
			String command = line[0];
			int accountNumberTo = Integer.parseInt(line[1], 10);
			int amount = Integer.parseInt(line[2], 10);
			int accountNumberFrom = Integer.parseInt(line[3], 10);
			String accountName = line[4];
			
			switch(command) {
			case "DEP":
				deposit(accountNumberTo, amount);
				break;
			case "WDR":
				withdraw(accountNumberFrom, amount);
				break;
			case "XFR":
				transfer(accountNumberFrom, accountNumberTo, amount);
				break;
			case "NEW":
				createacct(accountNumberFrom, accountName);
				break;
			case "DEL":
				deleteacct(accountNumberFrom, accountName);
				break;
			case "EOS":
				break;
			}
		}
		
		transactions.close();
		accounts.write(args[2], args[3]);
	}
	
	/*
	 * Adds to the balance.
	 */
	private static void deposit(int accountNumberTo, int amount) {
		Account theAccount = accounts.get(accountNumberTo);
		theAccount.balance += amount;
	}
	
	/*
	 * Checks for negative balance. Subtracting balance as needed.
	 */
	private static void withdraw(int accountNumberFrom, int amount) {
		Account theAccount = accounts.get(accountNumberFrom);
		
		if(theAccount.balance < amount) {
			System.out.println("ERROR: No account should ever have a negative balance");
			return;
		}
		
		theAccount.balance -= amount;
	}

	/*
	 * Checks for negative amount. Adding and subtracting balance as needed.
	 */
	private static void transfer(int accountNumberFrom, int accountNumberTo, int amount) {
		Account theAccountFrom = accounts.get(accountNumberFrom);
		Account theAccountTo = accounts.get(accountNumberTo);
		
		if(theAccountFrom.balance < amount) {
			System.out.println("ERROR: No account should ever have a negative balance");
			return;
		}
		
		theAccountFrom.balance -= amount;
		theAccountTo.balance += amount;
	}
	
	/*
	 * Checks if the account number exists. If there is an existing account,
	 * it fails. If there isn’t any, create a new account. 
	 */
	private static void createacct(int accountNumber, String name) {
		if(accounts.get(accountNumber) == null) {
			System.out.println("ERROR: Created account must have a new unused account number");
			return;
		}
		
		accounts.create(accountNumber, new Account(name, 0));
	}
	
	/*
	 * Makes sure the balance of account is zero. Makes sure the name and
	 * account number match. The purpose of the function is to delete account. 
	 */
	private static void deleteacct(int accountNumber, String name) {
		Account theAccount = accounts.get(accountNumber);
		
		if(theAccount.balance != 0) {
			System.out.println("ERROR: Deleted account must have zero balance");
			return;
		}
		
		if(theAccount.name != name) {
			System.out.println("ERROR: Deleted account transaction names must match.");
			return;
		}
		
		accounts.delete(accountNumber);
	}

}
