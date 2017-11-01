package abl_cisc327;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

// The Main class is to parse and execute commands and core functions of the program.
public class Main {
	
	static boolean isUserLoggedin = false;
	static boolean isUserAgent = false;
	static ValidAccountsFile validAccounts;
	static TransactionSummaryFile transactions;
	static HashMap<Integer, Integer> withdrawTotal;
	static Scanner input;
	
	// The purpose of this function is to call on all the 
	// essential methods (withdraw, transfer) and classes (TransactionSummaryFile, ValidAccountsFile). 
	public static void main(String[] args) {
		if(args.length < 2) {
			System.out.println("usage: frontend valid_accounts_file transaction_summary_file");
			System.exit(1);
		}
		
		input = new Scanner(System.in);
		
		try {
			validAccounts = new ValidAccountsFile(args[0]);
		} catch (FileNotFoundException e1) {
			System.out.println("Valid accounts file not found");
			System.exit(1);
		}
		transactions = new TransactionSummaryFile(args[1]);

		while(true) {
			System.out.print("Enter command: ");
			
			if(!input.hasNext()) {
				break;
			}
			
			String command = input.nextLine();
			
			try{
				switch(command) {
				case "login":
					login();
					break;
				case "logout":
					logout();
					break;
				case "deleteacct":
					deleteacct();
					break;
				case "createacct":
					createacct();
					break;
				case "transfer":
					transfer();
					break;
				case "withdraw":
					withdraw();
					break;
				case "deposit":
					deposit();
					break;
				default:
					System.out.println("ERROR: Invalid command");
					break;
				}
			} catch(Exception e) {
				continue;
			}
		}
	}
	
	// Makes sure first thing the user sees on screen is a login page.
	private static void login() {		
		if(isUserLoggedin) {
			// Check no subsequent login should be accepted after a login
			System.out.println("ERROR: You must first logout to login\n");
			return;
		}
		
		System.out.print("Enter login: ");
		String user = input.nextLine().toLowerCase();
		
		if(user.equals("atm")) {
			isUserLoggedin = true;
			isUserAgent = false;
			withdrawTotal = new HashMap<Integer, Integer>();
		}
		else if(user.equals("agent")) {
			isUserLoggedin = true;
			isUserAgent = true;
			withdrawTotal = new HashMap<Integer, Integer>();
		}
		else {
			System.out.println("ERROR: Invalid login");
		}
	}
	
	// User exits the session and gets the transaction summary file.
	private static void logout() {
		if(!isUserLoggedin) {
			// Logout should only be accepted when logged in
			System.out.println("ERROR: You must login before you logout");
			return;
		}
		
		isUserLoggedin = false;
		isUserAgent = false;
		
		// End session and write to the transaction file
		transactions.addCommand("EOS", 0, 0, 0, null);
		try {
			transactions.flush();
		} catch (IOException e) {
			System.out.println("Transaction file could not be written.");
			System.exit(1);
		}
	}
	
	//Allows users to put specified amount of money in their accounts.
	private static void deposit() throws Exception{
		System.out.print("Enter the account to deposit: ");
		String depositAccountStr = input.nextLine();
		System.out.print("Enter the amount to deposit: ");
		String depositAmountStr = input.nextLine();
		
		if(!isUserLoggedin) {
			// No transaction (deposit) other than login should be accepted before login
			System.out.println("ERROR: You need to login first");
			return;
		}
		
		int depositAmount = validateAmount(depositAmountStr);
		int depositAccount = validateAccountNumber(depositAccountStr);
		
		if(!validAccounts.isValid(depositAccount)){
			System.out.println("ERROR: invalid account number");
			return;
		}
		
		transactions.addCommand("DEP", depositAccount, depositAmount, 0, null);
		System.out.printf("You have successfully deposited $%.2f to %d\n", ((float) depositAmount)/100, depositAccount);
	}
	
	// When taking out specified amount of money, 
	// the method ensures the amount is correct and it is not more than the limit. 
	private static void withdraw() throws Exception {
		System.out.print("Enter the account to withdraw: ");
		String accountNumberStr = input.nextLine();
		System.out.print("Enter amount to withdraw: ");
		String amountStr = input.nextLine();
		
		if(!isUserLoggedin) {
			// No transaction (withdraw) other than login should be accepted before login
			System.out.println("ERROR: You need to login first");
			return;
		}
		
		int accountNumber = validateAccountNumber(accountNumberStr);
		int withdrawAmount = validateAmount(amountStr);
		
		int previousWithdrawn = 0;
		if(withdrawTotal.containsKey(accountNumber)) {
			previousWithdrawn = withdrawTotal.get(accountNumber);
		}
		
		if(previousWithdrawn + withdrawAmount > 100000 && !isUserAgent) {
			System.out.println("ERROR: You cannot withdraw more than $1,000.00 from a single account in a single ATM session");
			return;
		}
		withdrawTotal.put(accountNumber, previousWithdrawn + withdrawAmount);
		
		if(!validAccounts.isValid(accountNumber)) {
			System.out.println("ERROR: invalid account number");
			return;
		}
		
		transactions.addCommand("WDR", 0, withdrawAmount, accountNumber, null);
		System.out.printf("You have successfully withdrawn $%.2f from %d\n", ((float) withdrawAmount)/100, accountNumber);
	}
	
	// Ensures the accounts information and amount transferred are correct 
	// before proceeding with the process.
	private static void transfer() throws Exception {
		System.out.print("Enter the account number to transfer from: ");
		String fromAccountNumberStr = input.nextLine();
		System.out.print("Enter the account number to transfer to: ");
		String toAccountNumberStr = input.nextLine();
		System.out.print("Enter amount to transfer: ");
		String amount = input.nextLine();
	
		if(!isUserLoggedin) {
			// No transaction (transfer) other than login should be accepted before login
			System.out.println("ERROR: You need to login first");
			return;
		}
		
		int transferAmount = validateAmount(amount);
		int fromAccountNumber = validateAccountNumber(fromAccountNumberStr);
		int toAccountNumber = validateAccountNumber(toAccountNumberStr);
		
		//check if accounts exist 
		if(!validAccounts.isValid(fromAccountNumber) || !validAccounts.isValid(toAccountNumber)){
			System.out.println("ERROR: invalid account number");
			return;
		}
		
		transactions.addCommand("XFR", toAccountNumber, transferAmount, fromAccountNumber, null);
		System.out.printf("You have successfully tranferred $%.2f to %d\n", ((float) transferAmount)/100, toAccountNumber);
	}
	
	// Allows agent to create an account.
	private static void createacct() throws Exception {
		System.out.print("Enter the new account's number: ");
		String accountNumberStr = input.nextLine();
		System.out.print("Enter the new account's name: ");
		String accountNameStr = input.nextLine();
		
		if(!isUserLoggedin) {
			// No transaction (createacct) other than login should be accepted before login
			System.out.println("ERROR: You need to login first");
			return;
		}
		else if(!isUserAgent) {
			// After atm login, only unprivileged transactions are accepted
			System.out.println("ERROR: You cannot create an account");
			return;
		}
		
		int accountNumber = validateAccountNumber(accountNumberStr);
		String accountName = validateAccountName(accountNameStr);
		
		if(!validAccounts.canBeCreated(accountNumber)) {
			System.out.println("ERROR: Your account number must be different from other current account numbers");
			return;
		}
		
		validAccounts.newAccount(accountNumber);
		
		transactions.addCommand("NEW", accountNumber, 0, 0, accountName);
		System.out.printf("You have successfully created a new account\n");
	}
	
	// Allows agent to create an account.
	private static void deleteacct() throws Exception {
		System.out.print("Enter the target account's number: ");
		String accountNumberStr = input.nextLine();
		System.out.print("Enter the target account's name: ");
		String accountNameStr = input.nextLine();
		
		if(!isUserLoggedin) {
			// No transaction (deleteacct) other than login should be accepted before login
			System.out.println("ERROR: You need to login first");
			return;
		}
		else if(!isUserAgent) {
			// After atm login, only unprivileged transactions are accepted
			System.out.println("ERROR: You cannot delete an account");
			return;
		}
		
		int accountNumber = validateAccountNumber(accountNumberStr);
		String accountName = validateAccountName(accountNameStr);
		
		transactions.addCommand("DEL", accountNumber, 0, 0, accountName);
		validAccounts.delete(accountNumber);
		System.out.printf("Account deleted\n");
	}
		
	
	// Verifies the account number. It takes a String as a parameter and returns an integer.
	// If it is not valid an exception is thrown.
	private static int validateAccountNumber(String number) throws Exception {
		int accountNumber;
		
		try {
			accountNumber = Integer.parseInt(number, 10);
		} catch(NumberFormatException e) {
			// New account number must contain all numbers
			System.out.println("ERROR: Account number must be 7 numeric digits");
			throw new Exception();
		}
		
		if(accountNumber > 9999999 || accountNumber < 1000000) {
			// New account number is exactly 7 decimal digits
			System.out.println("ERROR: Account number must be 7 numeric digits");
			throw new Exception();
		}
		
		return accountNumber;
	}
	
	// Verifies the account name. It takes a String as a parameter and returns a String.
	// If it is not valid, an exception is thrown.
	private static String validateAccountName(String name) throws Exception {
		if(name.length() < 3) {
			System.out.println("ERROR: Your account name must be between 3 and 30 alphanumeric characters and not begin or end with a space");
			throw new Exception();
		}
		else if(name.length() > 30) {
			System.out.println("ERROR: Your account name must be between 3 and 30 alphanumeric characters and not begin or end with a space");
			throw new Exception();
		}
		else if(name.startsWith(" ") || name.endsWith(" ")) {
			System.out.println("ERROR: Your account name must not start or end with a space");
			throw new Exception();
		}
		for(int i=0; i<name.length(); i++) {
			char c = name.charAt(i);
			boolean isAlpha = (c >= 'a' && c <= 'z')
					|| (c >= 'A' && c <= 'Z')
					|| (c >= '0' && c <= '9')
					|| c == ' ';
			if(!isAlpha) {
				System.out.println("ERROR: Your account name must be alphanumeric");
				throw new Exception();
			}
		}
		
		return name;
	}
	
	// Verifies that the amount of money being either deposited, withdrawn
	// or transferred for both ATM and agent modes is valid.
	// It takes a String as a parameter and returns an int.
	// If amount is over the allowed limit, an exception is thrown.
	private static int validateAmount(String amountStr) throws Exception {
		int amount;
		try {
			amount = Integer.parseInt(amountStr, 10);
		} catch(NumberFormatException e) {
			// Amount must be a number
			System.out.println("ERROR: Amount must be a number");
			throw new Exception();
		}
		
		//check for atm mode and amount
		if(amount < 0) {
			System.out.println("ERROR: Can't transfer more than $1,000.00");
			throw new Exception();
		}
		else if(amount > 100000 && !isUserAgent){
			System.out.println("ERROR: Can't transfer more than $1,000.00");
			throw new Exception();
		}
		else if(amount > 99999999){
			System.out.println("ERROR: Can't transfer more than $999,999.99");
			throw new Exception();
		}
		
		return amount;
	}
}
