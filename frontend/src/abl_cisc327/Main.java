package abl_cisc327;

import java.util.Scanner;

//The Main class is to parse and execute commands and core functions of the program.
public class Main {
	
	static boolean isUserLoggedin = false;
	static boolean isUserAgent = false;
	static ValidAccountsFile validAccounts;
	static TransactionSummaryFile transactions;
	static Scanner input;
	
	//The purpose of this function is to call on all the 
	//essential methods (withdraw, transfer) and classes (TransactionSummaryFile, ValidAccountsFile). 
	public static void main(String[] args) {
		input = new Scanner(System.in);
		
		validAccounts = new ValidAccountsFile(args[1]);
		transactions = new TransactionsSummaryFile(args[2]);

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
			}catch(Exception e){
				continue;
			}
		}
	}
	
	//Makes sure first thing the user sees on screen is a login page.
	private static void login() {
		System.out.print("Enter login: ");
		String user = input.nextLine().toLowerCase();
		
		if(isUserLoggedin) {
			// Check no subsequent login should be accepted after a login
			System.out.println("ERROR: You must first logout to login\n");
			return;
		}
		
		if(user.equals("atm")) {
			isUserLoggedin = true;
			isUserAgent = false;
		}
		else if(user.equals("agent")) {
			isUserLoggedin = true;
			isUserAgent = true;
		}
		else {
			System.out.println("ERROR: Invalid login");
		}
	}
	
	//User exits the session and gets the transaction summary file.
	private static void logout() {
		if(!isUserLoggedin) {
			// Logout should only be accepted when logged in
			System.out.println("ERROR: You must login before you logout");
			return;
		}
		
		isUserLoggedin = false;
		isUserAgent = false;
	}
	
	//When taking out specified amount of money, 
	//the method ensures the amount is correct and it is not more than the limit. 
	private static void withdraw(){
		System.out.print("Enter amount to withdraw: ");
		String amount = input.nextLine();
		
		
		if(!isUserLoggedin) {
			// No transaction (withdraw) other than login should be accepted before login
			System.out.println("ERROR: You need to login first");
			return;
		}
		
		int withdrawAmount = Integer.parseInt(amount);
		if(withdrawAmount < 0 || withdrawAmount > 1000){
			//specification of limit
			System.out.println("Can't withdraw money below 0 or over 1000 in ATM mode");
		}
		else{
			transactions.addCommand("WDR",accountNumber,withdrawAmount,0,null);
			System.out.println(""You have successfully tranferred $%.2f to %d", ((float) transferAmount)/100, fromAccountNumber");
		}
	}
	
	//Ensures the accounts information and amount transferred are correct 
	//before proceeding with the process.
	private static void transfer() throws Exception{
		System.out.println("Enter the account number to transfer from");
		String fromAccountNumberStr = input.nextLine();
		System.out.println("Enter the account number to transfer to");
		String toAccountNumberStr = input.nextLine();
		System.out.println("Enter amount to transfer");
		String amount = input.nextLine();
	
		
		if(!isUserLoggedin) {
			// No transaction (transfer) other than login should be accepted before login
			System.out.println("ERROR: You need to login first");
			return;
		}
		
		int transferAmount = Integer.parseInt(amount);
		int fromAccountNumber = validateAccountNumber(fromAccountNumberStr);
		int toAccountNumber = validateAccountNumber(toAccountNumberStr);
		
		//check for atm mode and amount
		if((transferAmount<0 || transferAmount > 100000)){
			System.out.println("ERROR: Can't transfer more than $1,000.00");
			return;
		}
		else if((transferAmount<0 || transferAmount > 99999999) && !isUserAgent){
			System.out.println("ERROR: Can't transfer more than $999,999.99");
			return;
		}
	
		
		//check if accounts exist 
		if(!validAccounts.isValid(fromAccountNumber) || !validAccounts.isValid(toAcountNumber)){
			System.out.println("ERROR: invalid account number");
			return;
		}
		
		transactions.addCommand ("XFR",toAccountNumber, transferAmount, fromAccountNumber,null);
		System.out.printf("You have successfully tranferred $%.2f to %d", ((float) transferAmount)/100, toAccountNumber);
		
		
		//working case
		
		
		
	}
	
	//Allows agent to create an account.
	private static void createacct() {
		String inputNumber = input.nextLine();
		String inputName = input.nextLine();
		
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
		
		int number;
		String name;
		try {
			number = validateAccountNumber(inputNumber);
			name = validateAccountName(inputName);
		} catch (Exception e) {
			return;
		}
		
		// TODO: Process transaction
	}
		
	
	//Allows agents to delete an account
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
	
	private static String validateAccountName(String name) {
		// TODO: Validate the account name
		return name;
	}
}
