/**
 *  Gift class implements the transaction interface where it gifts a game for any current existing user to an existing
 *  user.
 */
public class Gift implements Transaction{
    
    /**
     * Checks all the fatal and constraint errors present (or not present) in the given gift transaction
     * @param transaction the gift string transaction in daily.txt
     */
    public static void execute(String transaction) {
        // Checks whether the transaction format is valid
        if (!TransactionsModifier.Checker(transaction, "^(09)\\s(.){25}\\s(.){15}\\s(.){15}$")) {
            System.out.println("FATAL ERROR: Invalid Transaction!");
            return;
        }

        // Checks that a user has logged in!
        if(TransactionsModifier.currentUser == null){
            System.out.println("FATAL ERROR: Please Log in!");
            return;
        }
        
        String game_name = transaction.substring(3, 28).trim();
        String gifter_name = transaction.substring(29, 44).trim();
        String receiver_name = transaction.substring(45).trim();
        
        // Checks that both the gifter and receiver exist in the database
        if (!(UsersDatabase.Users.containsKey(gifter_name) && UsersDatabase.Users.containsKey(receiver_name))) {
            System.out.println("CONSTRAINT ERROR: Either Gifter or Receiver does not exist!");
            return;
        }
        
        // Check that the gifter is also not the receiver
        if(gifter_name.equals(receiver_name)){
            System.out.println("CONSTRAINT ERROR: You cannot gift games to your self");
            return;
        }
        
        User gifter = UsersDatabase.Users.get(gifter_name);
        User receiver = UsersDatabase.Users.get(receiver_name);

        // Checks that the game being gifted is not already owned by the receiver
        if(OwnsGame(receiver, game_name) || TestSuite.userSellingGame(receiver_name, game_name)){
            System.out.println("CONSTRAINT ERROR: User already owns the game!");
            return;
        }
        
        Game game = null;
        
        // Checks that if the logged in user is an admin, if he is then it checks whether the gifter has the game, if it
        // does then after gifting the game is removed from his inventory if not the admin creates a new game and gifts
        // that for the gifter to the receiver
        if(TransactionsModifier.currentUser.getUserType().equals(User.ADMIN)){
            if(OwnsGame(gifter, game_name) || selling_or_buying(gifter, game_name)){
                game = Gift.getGame(gifter, game_name);
            }else {
                game = new Game(game_name, 0.0, gifter_name, 0.0);
            }    
        }
        
        // Checks that if the current user is not an admin, and the game is not owned by the player and the game 
        // is not being sold by the player and it's also not purchased today
        if(!TransactionsModifier.currentUser.getUserType().equals(User.ADMIN) && !OwnsGame(gifter, game_name) &&
            !selling_or_buying(gifter, game_name)){
            System.out.println("CONSTRAINT ERROR: Non-Admin users cannot gift games they do not own!");
            return;
        }
        
        if(game == null){
            game = gifter.getGame(game_name);
        }
        
        // Runs helper method "gift"
        gift(gifter, receiver,  game);
    }

    /**
     * Helper method that gift's the game that is owned by the gifter and is also not owned by the receiver
     * @param Gifter the user that is gifting the game
     * @param Receiver the user that is receiving the game
     * @param game the game being gifted
     */
    private static void gift(User Gifter, User Receiver, Game game) {
        if(OwnsGame(Gifter, game.getName()) || selling_or_buying(Gifter, game.getName())){
            removeGiftingGame(Gifter, game.getName());
        }
        System.out.println("User " + Gifter.getUserName() + " has gifted Game " + game.getName());
        UsersDatabase.Users.get(Receiver.getUserName()).addGame(game);
        UsersDatabase.Users.get(Receiver.getUserName()).addUserGame(game.getName(), Double.toString(game.getPrice()));
        UsersDatabase.updateGame(Receiver);
    }

    /**
     * Helper method that checks if the game is being sold by the gifter and if the game is not purchased on the same
     * day as the gifting transaction
     * @param gifter the user that is gifting the game
     * @param game_name the name of the game being gifted
     * @return false if the game is not being sold by the gifter and if the game is purchased on the same day as the 
     * gifting
     */
    public static boolean selling_or_buying(User gifter, String game_name){
        boolean notSellingToday = false;
        boolean notBoughtToday = true;
        for(Game game: GamesDatabase.selling.values()){
            if(game.getName().equals(game_name) && gifter.getUserName().equals(game.getOwner())){
                notSellingToday = true;
                break;
            }
        }
        for(Game game: Buy.purchasedToday.values()){
            if(game.getName().equals(game_name) && game.getOwner().equals(gifter.getUserName())){
                notBoughtToday = false;
                break;
            }
        }
        return notBoughtToday && notSellingToday;
    }

    /**
     * Helper method that checks whether the game is owned by the user.
     * @param user the user who's inventory is being checked for the game with the game_name
     * @param game_name the name of the game being searched for
     * @return true if the game is owned by the user
     */
    public static boolean OwnsGame(User user, String game_name) {
        for (Game curr_game: user.getInventory()) {
            if (curr_game.getName().equals(game_name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Helper method that get's the game if the game is being sold by the gifter or if the game is in the user's 
     * inventory.
     * @param gifter the user who is quite possibly selling the game
     * @param game_name the name of the game being searched for
     * @return null if the game is not being sold and the game is not in the gifters inventory.
     */
    public static Game getGame(User gifter, String game_name){
        for(Game game1: GamesDatabase.selling.values()){
            if(game1.getOwner().equals(gifter.getUserName()) && game1.getName().equals(game_name)){
                return game1;
            }
        }
        return gifter.getGame(game_name);
    }

    /**
     * Helper method that removes game from the inventory of the game owner
     * @param gameOwner the owner of the game being removed
     * @param game the game being removed from the owners inventory
     */
    public static void removeGiftingGame(User gameOwner, String game){
        if(gameOwner.removeGame(game)){
            UsersDatabase.updateGame(gameOwner);
            UsersDatabase.Users.get(gameOwner.getUserName()).setUserGames(gameOwner.getUserGames());
        }
    }
    

}
