/**
 * Interface for each transaction. Each specific transaction will implement this, the only inherited functionality in
 * each transaction is the execute method.
 */

public interface Transaction {

    /**
     * The method that will carry out and preform the necessary actions for each transaction. This method will handle
     * carrying out the transaction itself and all the consequences (file changes, database changes, etc.) of each
     * transaction.
     * @param user The user preforming the transaction
     */
    public static void execute(User user) {}
}