/**
 * AddCredit class implements the transaction interface where it adds credit to a logged in user if the user is not an
 * admin otherwise it adds credit to an existing user.
 */
public class AddCredit implements Transaction{

    public static double MAXACCOUNTBALANCE = 999999.99;

    /**
     * Checks all the fatal and constraint errors present (or not present) in the given add credit transaction
     * @param transaction the add credit string transaction in daily.txt
     */
    public static void execute(String transaction){
        // Checks whether the transaction format is valid
        if(!TransactionsModifier.Checker(transaction, "^(06)\\s(.){15}\\s\\w{2}\\s\\d{6}(\\.)\\d{2}$")) {
            System.out.println("FATAL ERROR: Invalid Transaction!");
            return;
        }

        // Checks that a user has logged in!
        if(TransactionsModifier.currentUser == null){
            System.out.println("FATAL ERROR: Please Log in!");
            return;
        }
        
        String username = transaction.substring(3, 18).trim();
        double credit = Double.parseDouble(transaction.substring(22));

        // Checks whether the user to add credit to is in the database
        if (!UsersDatabase.Users.containsKey(username)) {
            System.out.println("CONSTRAINT ERROR: User does not exist");
            return;
        }
        User user = UsersDatabase.Users.get(username);
        double maxDailyAmount = 1000.00;

        //Checks whether it's possible to add valid credit on the present day
        if (!(credit >= 0 && TransactionsModifier.dailyAdd.get(username) + credit <= maxDailyAmount)) {
            System.out.println("CONSTRAINT ERROR: User " + username + " has exceeded daily added amount");
            return;
        }

        // Runs method addCredit if the account is not maxed out after adding the credit
        if (TransactionsModifier.dailyAdd.get(username) + credit + user.getUserBalance() <= MAXACCOUNTBALANCE) {
            addCredit(user, credit);
        } else {
            System.out.println("CONSTRAINT ERROR: User " + username + " has exceeded balance amount");
        }
    }

    /**
     * A helper method used to update the database depending on the user's type and the amount that needs to be added
     * to his account balance. It asserts that if the current user is not an admin, then the user to add the credit
     * to must be the logged in user.
     *
     * @param user the user who needs credit added to his account
     * @param credit the amount to be added in the users account balance
     */
    public static void addCredit(User user, double credit){
        // Makes sure that the user is the same as the current logged in user if user is not an admin
        if(!TransactionsModifier.currentUser.getUserType().equals(User.ADMIN) &&
                !user.getUserName().equals(TransactionsModifier.currentUser.getUserName())) {
            System.out.println("CONSTRAINT ERROR: Non-Admins cannot add credit to other Users!");
            return;
        }

        // Changes the file and updates our database on the users new account balance
        UsersDatabase.addCredit(credit + user.getUserBalance(), user);
        TransactionsModifier.dailyAdd.put(user.getUserName(),
                TransactionsModifier.dailyAdd.get(user.getUserName()) + credit);
        UsersDatabase.Users.get(user.getUserName()).addUserBalance(credit);
        System.out.println(credit + " has been added to the balance of user " + user.getUserName());
    }
}
