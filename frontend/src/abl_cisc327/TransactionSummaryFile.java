package abl_cisc327;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
/*
 * The TransactionSummaryFile class will show the overall results
 * of the session and the user should have read only permission.
 */
public class TransactionSummaryFile {
	private String file;
	private String fileBuffer;
	
	/*
	 * Creates an empty buffer of commands to be written to the file.
	 * The flush method will write to the file whose name is given in String
	 */
	public TransactionSummaryFile(String file) {
		this.fileBuffer = "";
		this.file = file;
	}
	
	/*
	 * Adds a command to the buffer.
	 * Each of the arguments corresponds to a field in the file.
	 */
	public void addCommand(String type, int to, int amount, int from, String accountName) {
		if(accountName == null) {
			accountName = "***";
		}
		fileBuffer += String.format("%s %07d %03d %07d %s\n", type, to, amount, from, accountName);
	}
	
	/*
	 * Writes all of the commands in the buffer to the file and clears the buffer.
	 * Intended for use in logout.
	 */
	public void flush() throws IOException {
		// Open the transaction summary file in append mode
		FileWriter fw = new FileWriter(new File(file), true);
		fw.write(fileBuffer);
		fileBuffer = "";
		fw.close();
	}
}
