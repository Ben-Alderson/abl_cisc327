package backend;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.TreeMap;

public class MasterAccountsFile {
	TreeMap<Integer, Account> accounts;
	
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
	
	public Account get(int accountNumber) {
		return accounts.get(accountNumber);
	}
	
	public void create(int accountNumber, Account account) {
		accounts.put(accountNumber, account);
	}
	
	public void delete(int accountNumber) {
		accounts.remove(accountNumber);
	}
	
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
