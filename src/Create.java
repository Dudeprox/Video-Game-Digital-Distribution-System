import java.util.ArrayList;

/**
 * Create class implements the transaction interface where it creates a new user depending on a current logged in admin.
 */
public class Create implements Transaction{

    /**
     * Creates a new user from a valid transaction, where the user does not exist in the system's database.
     * @param transaction the create new user string transaction in daily.txt
     */
    public static void execute(String transaction){
        // Checks whether the transaction format is valid
        if(!TransactionsModifier.Checker(transaction, "^(01)\\s(.){15}\\s\\w{2}\\s\\d{6}(\\.)\\d{2}$")) {
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
        if (!TransactionsModifier.currentUser.getUserType().equals(User.ADMIN)) {
            System.out.println("CONSTRAINT ERROR: Only Admins can create new Users");
            return;
        }

        String newUsername = transaction.substring(3, 18).trim();
        String newUserType = transaction.substring(19, 21).trim();
        double newUserCredit = Double.parseDouble(transaction.substring(22).trim());

        // Checks whether the user balance is valid
        if(newUserCredit < 0){
            System.out.println("CONSTRAINT ERROR: Invalid Credit!");
            return;
        }

        // Creates the new user if possible.
        User newUser = new User(newUsername, newUserCredit, newUserType, new ArrayList<>());

        if (!UsersDatabase.Users.containsKey(newUser.getUserName())) {
            UsersDatabase.addUser(newUser.getUserName(), newUser.getUserBalance(), newUser.getUserType(),
                    newUser.toStringUserGames());
            System.out.println("Admin " + TransactionsModifier.currentUser.getUserName() +
                    " created User " + newUser.getUserName());
        } else {
            System.out.println("CONSTRAINT ERROR: User Already Exists");
        }

    }
}
