/**
 * Delete class implements the transaction interface where it deletes an existing user in the database if and only if
 * the current logged in user is an Admin.
 */
public class Delete implements Transaction{

    /**
     * Deletes an existing user from the database using a valid transaction, with the assumption that the current
     * logged in user is an admin.
     * @param transaction the delete user string transaction in daily.txt
     */
	public static void execute(String transaction){

	    // Checks whether the transaction format is valid
        if(!TransactionsModifier.Checker(transaction, "^(02)\\s(.){15}\\s\\w{2}\\s\\d{6}(\\.)\\d{2}$")){
            System.out.println("FATAL ERROR: Invalid Transaction!");
            return;
        }
        // Updates our database
        UsersDatabase.main(new String[]{""});

        // Checks that a user has logged in!
        if(TransactionsModifier.currentUser == null){
            System.out.println("FATAL ERROR: Please Log in!");
            return;
        }

        // Checks whether logged in user is an admin
        if(!TransactionsModifier.currentUser.getUserType().equals(User.ADMIN)) {
            System.out.println("CONSTRAINT ERROR: Only Admins can delete Users");
            return;
        }

        // Deletes the user from the database if possible.
        String deleteUsername = transaction.substring(3, 18).trim();

        if(UsersDatabase.Users.containsKey(deleteUsername) &&
                !TransactionsModifier.currentUser.getUserName().equals(deleteUsername)) {

            UsersDatabase.removeUser(deleteUsername);
            UsersDatabase.Users.remove(deleteUsername);
            System.out.println("Admin " + TransactionsModifier.currentUser.getUserName() + " deleted User " +
                    deleteUsername);
        }
        else{
            System.out.println("CONSTRAINT ERROR: User does not exist");
        }
    }
}
