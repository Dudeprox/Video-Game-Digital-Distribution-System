/**
 * Logout class implements the interface Transaction to log out a current, valid and existing user out of the backend
 * of this program.
 */
public class Logout implements Transaction {

    /***
     * Logs out the valid user that is currently logged in if it's credentials match the database.
     * @param transaction The logout string transaction in daily.txt
     * @return true if the user was able to logout successfully.
     */
    public static boolean execute(String transaction){
        // Checks whether the transaction format is valid
        if(!TransactionsModifier.Checker(transaction, "^(10)\\s(.){15}\\s\\w{2}\\s\\d{6}(\\.)\\d{2}$")){
            System.out.println("FATAL ERROR: Invalid Transaction!");
            return false;
        }
        
        String userName = transaction.substring(3, 18).trim();
        String userType = transaction.substring(19, 21).trim();
        double userBalance = Double.parseDouble(transaction.substring(22).trim());

        // Checks whether the user that is being logged out is the current user that has logged in
        if(!userName.equals(TransactionsModifier.currentUser.getUserName())){
            System.out.println("CONSTRAINT ERROR: User Name does not match Logged in User!");
            return false;
        }

        // Checks whether the user balance in the transaction string matches the database
        if(userBalance != TransactionsModifier.currentUser.getUserBalance()) {
            System.out.println("CONSTRAINT ERROR: User Balance does not match Database!");
        }

        // Checks whether the user type in the transaction string matches the database
        if(!userType.equals(TransactionsModifier.currentUser.getUserType())){
            System.out.println("CONSTRAINT ERROR: User Type does not match Database!");
        }

        TransactionsModifier.currentUser = null;
        System.out.println("User " + userName + " has Logged out!");
        return true;
    }
}
