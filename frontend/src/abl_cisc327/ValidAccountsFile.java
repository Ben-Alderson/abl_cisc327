package abl_cisc327;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;

/*
 * The ValidAccountsFile class will store the accounts in a hash set.
 */
public class ValidAccountsFile {
	private HashSet<Integer> validNumbers;
	
	/*
	 * Loads the valid accounts file from the path given in the argument.
	 */
	public ValidAccountsFile(String file) throws FileNotFoundException {
		validNumbers = new HashSet<Integer>();
		
		Scanner reader = new Scanner(new File(file));
		while(reader.hasNextLine()) {
			Integer number = Integer.parseInt(reader.nextLine(), 10);
			if(number == 0) {
				break;
			}
			validNumbers.add(number);
		}
		reader.close();
	}
	
	/*
	 * Returns true if an account is valid. An account is valid if
	 * the account is in the valid accounts file and has not been deleted.
	 */
	public boolean isValid(int accountNumber) {
		return validNumbers.contains(accountNumber);
	}
	
	/*
	 * Marks the given account unusable for the rest of the program’s runtime.
	 * This intended for use from deleteacct.
	 */
	public void delete(int accountNumber) {
		validNumbers.remove(accountNumber);
	}
}
