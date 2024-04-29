/**
 *  Sell class implements the transaction interface where it sell's a game for any current existing user.
 */
public class Sell implements Transaction{

    /**
     * Checks all the fatal and constraint errors present (or not present) in the given sell transaction
     * @param transaction the sell string transaction in daily.txt
     */
    public static void execute (String transaction) {
        // Checks whether the transaction format is valid
        if(!TransactionsModifier.Checker(transaction,
                "^(03)\\s(.){25}\\s(.){15}\\s\\d{2}(\\.)\\d{2}\\s\\d{3}(\\.)\\d{2}$")) {
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
        double price = Double.parseDouble(transaction.substring(51));
        double saleDiscount = Double.parseDouble(transaction.substring(45, 50));

        // Checks whether logged in user is the selling user
        if(!TransactionsModifier.currentUser.getUserName().equals(sellerName)){
            System.out.println("ERROR: You cannot sell for other Users!");
            return;
        }

        // Checks whether the logged in user is not a buyer
        if (TransactionsModifier.currentUser.getUserType().equals(User.BUYER)) {
            System.out.println("ERROR: A buyer cannot sell games!");
            return;
        }

        // Runs the helper method to sell game
        SellGame(gameName, sellerName, price, saleDiscount);
    }

    /**
     * Helper method that sell's the game that is not owned by the seller and is also not being sold by the user.
     * @param gameName name of the game that user is selling
     * @param sellerName name of the seller
     * @param price the selling game's price
     * @param saleDiscount the discount applied to game when auction sale is on
     */
    public static void SellGame(String gameName, String sellerName, double price, double saleDiscount){

        // Checks whether game is owned (in user's inventory) or whether the game is already being sold by the user
        if(Gift.OwnsGame(TransactionsModifier.currentUser, gameName) ||
                TestSuite.userSellingGame(TransactionsModifier.currentUser.getUserName(), gameName)){
            System.out.println("CONSTRAINT ERROR: User already owns game or is selling the game!");
            return;
        }

        // Puts the game up for sale and adds it to the games.txt file
        Game game = new Game(gameName, price, sellerName, saleDiscount);
        GamesDatabase.sellToday.put(sellerName, game);
        GamesDatabase.addGame(game);
        System.out.println("Game has been put up for sale!");
    }
}
