import java.util.HashMap;

/**
 *  Buy class implements the transaction interface where it buy's a game for any existing seller that is selling the
 *  game.
 */
public class Buy {

    public static HashMap<String, Game> purchasedToday = new HashMap<>();
    
    /**
     * Checks all the fatal and constraint errors present (or not present) in the given buy transaction
     * @param transaction the buy string transaction in daily.txt
     */
    public static void execute(String transaction){
        // Checks whether the transaction format is valid
        if(!TransactionsModifier.Checker(transaction, "^(04)\\s(.){25}\\s(.){15}\\s(.){15}$")) {
            System.out.println("FATAL ERROR: Invalid Transaction!");
            return;
        }

        // Checks that a user has logged in!
        if(TransactionsModifier.currentUser == null){
            System.out.println("FATAL ERROR: Please Log in!");
            return;
        }
        
        String gameName = transaction.substring(3, 28).trim();
        String sellerName = transaction.substring(29, 44).trim();
        String buyerName = transaction.substring(45, 60).trim();

        // Checks that the seller exists and is not a buyer type user
        if(!UsersDatabase.Users.containsKey(sellerName) ||
                UsersDatabase.Users.get(sellerName).getUserType().equals(User.BUYER)){
            System.out.println("CONSTRAINT ERROR: User does not exist or is not a seller!");
            return;
        }

        // Checks that the current user is the buyer, the buyer is not a seller and buyer and seller don't equal
        if(!TransactionsModifier.currentUser.getUserName().equals(buyerName) ||
                TransactionsModifier.currentUser.getUserType().equals(User.SELLER) || buyerName.equals(sellerName)) {
            System.out.println("CONSTRAINT ERROR: You cannot buy games for others, cannot buyer your own game or a" +
                    "seller can not buy games!");
            return;
        }

        User seller = UsersDatabase.Users.get(sellerName);
        // Checks that the game being sold is a valid game
        Game game = validGameSelling(gameName, sellerName);

        // Checks that game is valid, the buyer does not own the game and the buyer is not selling the same game
        if(game == null || TransactionsModifier.currentUser.getUserGames().contains(game.getName()) ||
                TestSuite.userSellingGame(buyerName, gameName)){
            System.out.println("CONSTRAINT ERROR: Seller is not selling this game, or you are selling the game" +
                    " or you already own this game!");
            return;
        }
        double price = game.getPrice() - getSale(game);

        // Checks that the buyer has enough credit to purchase the game and seller will not have max account balance
        // after game has been sold
        if(price > TransactionsModifier.currentUser.getUserBalance() || seller.getUserBalance() + price >
                AddCredit.MAXACCOUNTBALANCE){
            System.out.println("CONSTRAINT ERROR: Not enough Balance to purchase Game or Seller is not " +
                    "Selling!");
            return;
        }
        //Runs helper method
        BuyGame(game, price, seller);
    }

    /**
     * Helper method that updates the corresponding file to update user accounts and updates user account balances
     * @param game the game being bought
     * @param price the price of the game being bought
     * @param seller the user that is selling the game
     */
    public static void BuyGame(Game game, double price, User seller){
        // Adds game to the users inventory and userGames
        TransactionsModifier.currentUser.addGame(game);
        TransactionsModifier.currentUser.addUserGame(game.getName(), Double.toString(game.getPrice()));

        // Updates the users.txt file
        UsersDatabase.updateGame(TransactionsModifier.currentUser);
        UsersDatabase.addCredit(TransactionsModifier.currentUser.getUserBalance() - price,
                TransactionsModifier.currentUser);
        UsersDatabase.addCredit(seller.getUserBalance() + price, seller);

        // Updates the buyers and sellers user balance
        TransactionsModifier.currentUser.addUserBalance(-price);
        UsersDatabase.Users.get(seller.getUserName()).addUserBalance(price);
        
        purchasedToday.put(TransactionsModifier.currentUser.getUserName(), game);
        
        System.out.println("Game has been purchased by " + TransactionsModifier.currentUser.getUserName() + "!");
    }


    /**
     * Checks whether the game with the gameName is already being sold by the seller with the sellerName
     * @param gameName the name of the game being checked
     * @param sellerName the name of the seller
     * @return null if game is already being sold by the seller
     */
    public static Game validGameSelling(String gameName, String sellerName){
        GamesDatabase.main(new String[]{""});
        for(Game game: GamesDatabase.sellToday.values()){
            if(game.getName().equals(gameName) && game.getOwner().equals(sellerName)){
                return null;
            }
        }
        for(Game game: GamesDatabase.selling.values()){
            if(game.getName().equals(gameName) && game.getOwner().equals(sellerName)){
                return game;
            }
        }
        return null;
    }

    /**
     * Get's the sale amount added to a game depending on the static variable auctionSale if auction sale is on or not
     * @param game game being bought
     * @return amount of the game's discount
     */
    public static double getSale(Game game){
        AuctionSale.main(new String[]{""});
        double price = 0;
        if(AuctionSale.auctionSale){
            price = game.getPrice() * (game.getAuctionSale() / 100);
        }
        return price;
    }
}
