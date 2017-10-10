package abl_cisc327;

import java.util.Scanner;

public class Main {
	
	static boolean isUserLoggedin = false;
	static boolean isUserAgent = false;
	static Scanner input;
	
	public static void main(String[] args) {
		input = new Scanner(System.in);
		
		while(true) {
			System.out.print("Enter command: ");
			
			if(!input.hasNext()) {
				break;
			}
			
			String command = input.nextLine();
			
			switch(command) {
			case "login":
				login();
				break;
			case "logout":
				logout();
				break;
			case "createacct":
				createacct();
				break;
			default:
				System.out.println("ERROR: Invalid command");
				break;
			}
		}
	}
	
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
	
	private static void logout() {
		if(!isUserLoggedin) {
			// Logout should only be accepted when logged in
			System.out.println("ERROR: You must login before you logout");
			return;
		}
		
		isUserLoggedin = false;
		isUserAgent = false;
	}
	
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
