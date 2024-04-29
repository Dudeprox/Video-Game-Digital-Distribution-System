/**
 * Login class implements the interface Transaction to log in a valid and existing user in to the backend
 * of this program.
 */
public class Login implements Transaction{

    /***
     * Logs in a valid user that exists in the database with it's appropriate credentials.
     * @param transaction The login string transaction in daily.txt
     * @return true if the user was able to log in successfully.
     */
    public static boolean execute(String transaction){
        // Checks whether the transaction format is valid
        if(!TransactionsModifier.Checker(transaction, "^(00)\\s(.){15}\\s\\w{2}\\s\\d{6}(\\.)\\d{2}$")){
            System.out.println("FATAL ERROR: Invalid Transaction!");
            return false;
        }

        String userName = transaction.substring(3, 18).trim();
        String userType = transaction.substring(19, 21).trim();
        double userBalance = Double.parseDouble(transaction.substring(22).trim());

        // Checks whether the user exists in the database
        if(!UsersDatabase.Users.containsKey(userName)) {
            System.out.println("FATAL ERROR: No such user exists!");
            return false;
        }

        TransactionsModifier.currentUser = UsersDatabase.Users.get(userName);

        // Checks whether the user balance in the transaction string matches the database
        if(userBalance != TransactionsModifier.currentUser.getUserBalance()) {
            System.out.println("CONSTRAINT ERROR: User Balance does not match Database!");
        }

        // Checks whether the user type in the transaction string matches the database
        if(!userType.equals(TransactionsModifier.currentUser.getUserType())){
            System.out.println("CONSTRAINT ERROR: User Type does not match Database!");
        }

        System.out.println("User " + userName + " has Logged in!");
        return true;
    }
}
