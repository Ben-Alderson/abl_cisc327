package backend;

import java.io.IOException;
import java.util.Scanner;

public class Main {
	static MasterAccountsFile accounts;

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
	
	private static void deposit(int accountNumberTo, int amount) {
		Account theAccount = accounts.get(accountNumberTo);
		theAccount.balance += amount;
	}
	
	private static void withdraw(int accountNumberFrom, int amount) {
		Account theAccount = accounts.get(accountNumberFrom);
		
		if(theAccount.balance < amount) {
			System.out.println("ERROR: No account should ever have a negative balance");
			return;
		}
		
		theAccount.balance -= amount;
	}

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
	
	private static void createacct(int accountNumber, String name) {
		if(accounts.get(accountNumber) == null) {
			System.out.println("ERROR: Created account must have a new unused account number");
			return;
		}
		
		accounts.create(accountNumber, new Account(name, 0));
	}
	
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
