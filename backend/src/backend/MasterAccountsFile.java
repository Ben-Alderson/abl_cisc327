package backend;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.TreeMap;

/*
 * The MasterAccountsFile class sorts the accounts in ascending order.
 * The class ensures the account numbers, monetary amounts and account
 * names are correctly logged for the transaction summary file above.
 */
public class MasterAccountsFile {
	TreeMap<Integer, Account> accounts;
	
	/*
	 * Loads the master accounts file from the file located at the given path.
	 */
	public MasterAccountsFile(String fileName) {
		accounts = new TreeMap<Integer, Account>();
		
		Scanner file = new Scanner(fileName);
		while(file.hasNextLine()) {
			String[] line = file.nextLine().split(" ", 3);
			int accountNumber = Integer.parseInt(line[0], 10);
			int balance = Integer.parseInt(line[1], 10);
			String accountName = line[2];
			
			accounts.put(accountNumber, new Account(accountName, balance));
		}
		file.close();
	}
	
	/*
	 * Gets an account by the account number.
	 */
	public Account get(int accountNumber) {
		return accounts.get(accountNumber);
	}
	
	/*
	 * Adds the account to the master accounts file with the given account number.
	 */
	public void create(int accountNumber, Account account) {
		accounts.put(accountNumber, account);
	}
	
	/*
	 * Deletes the account with the given account number.
	 */
	public void delete(int accountNumber) {
		accounts.remove(accountNumber);
	}
	
	/*
	 * Writes the valid accounts file and the new master accounts
	 * file to the given paths and throws all input and output exceptions.
	 */
	public void write(String masterFileName, String validFileName) throws IOException {
		FileWriter masterFile = new FileWriter(new File(masterFileName), true);
		FileWriter validFile = new FileWriter(new File(validFileName), true);
		
		for(Integer accountNumber: accounts.keySet()) {
			Account currentAccount = accounts.get(accountNumber);
			masterFile.write(accountNumber.toString());
			masterFile.write(" ");
			masterFile.write(currentAccount.balance);
			masterFile.write(" ");
			masterFile.write(currentAccount.name);
			masterFile.write("\n");
			
			validFile.write(accountNumber.toString());
			validFile.write("\n");
		}
		
		validFile.write("0000000");
		validFile.write("\n");
		
		masterFile.close();
		validFile.close();
	}
}
