/**
 * RemoveGame class implements the transaction interface where it removes a game that is in the user's inventory
 * or is being sold by the user.
 */
public class RemoveGame {

    /**
     * Checks all the fatal and constraint errors present (or not present) in the given remove game transaction
     * @param transaction the remove game string transaction in daily.txt
     */
    public static void execute(String transaction){
        // Checks whether the transaction format is valid
        if(!TransactionsModifier.Checker(transaction, "^(08)\\s(.){25}\\s(.){15}$")) {
            System.out.println("FATAL ERROR: Invalid Transaction!");
            return;
        }

        // Checks that a user has logged in!
        if(TransactionsModifier.currentUser == null){
            System.out.println("FATAL ERROR: Please Log in!");
            return;
        }
        
        String game = transaction.substring(3, 28).trim();
        String owner = transaction.substring(29).trim();

        // Checks whether the game owner exists in the database
        if(!UsersDatabase.Users.containsKey(owner)){
            System.out.println("CONSTRAINT ERROR: User does not exist!");
            return;
        }

        // Checks to make sure that a non-admin user is the current logged in user
        if(!TransactionsModifier.currentUser.getUserType().equals(User.ADMIN) &&
                !TransactionsModifier.currentUser.getUserName().equals(owner)){
            System.out.println("CONSTRAINT ERROR: You do not have the privilege to remove other user's games!");
            return;
        }
        if(removeGame(game, UsersDatabase.Users.get(owner))) {
            System.out.println("User has removed Game " + game);
        }    
    }

    /**
     * A helper method that checks whether the game is in the inventory of the game owner, where the game is then
     * removed and it also checks whether the game is being sold by the game owner where the game is once again
     * removed
     *
     * @param game the game that needs to be removed
     * @param gameOwner the user who owns the game
     */
    public static boolean removeGame(String game, User gameOwner){
        // Removes and gets boolean whether game is in inventory (after removal)
        if(gameOwner.removeGame(game)){
            UsersDatabase.updateGame(gameOwner);
            UsersDatabase.Users.get(gameOwner.getUserName()).setUserGames(gameOwner.getUserGames());
            return true;
        }
        else{
            // Runs the helper method to check whether the game is being sold by the owner, and if so removes it
            if(checkGameSell(gameOwner, game)){
                GamesDatabase.removeGame(gameOwner, game);
                return true;
            }else{
                System.out.println("CONSTRAINT ERROR: User does not own game!");
            }
        }
        return false;
    }

    /**
     * A helper method that checks whether the game is being sold by the game owner in the database
     * @param gameOwner the user that owns the game
     * @param game the game that is(or not) being sold by the owner
     * @return true if game is being sold by gameOwner
     */
    public static boolean checkGameSell(User gameOwner, String game) {
        boolean checker = false;
        // Checks whether game is being sold by the game owner
        for (Game game1 : GamesDatabase.selling.values()) {
            if (game1.getName().equals(game) && game1.getOwner().equals(gameOwner.getUserName())) {
                GamesDatabase.selling.remove(game1.getName());
                checker = true;
                break;
            }
        }
        return checker;
    }
}