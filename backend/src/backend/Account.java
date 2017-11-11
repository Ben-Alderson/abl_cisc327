package backend;

/*
 * The Account class holds the account name and account balance
 */
public class Account {
	public String name;
	public int balance;

	/*
	 * Creates a new account with a name and balance.
	 */
	public Account(String accountName, int balance) {
		this.name = accountName;
		this.balance = balance;
	}
}
