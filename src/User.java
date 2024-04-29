import java.util.ArrayList;
/**
 * User class implements and creates a user based on the user's name, account balance, type and the inventory
 * of games and price the user owns
 */
public class User {

    public static final String ADMIN = "AA", BUYER = "BS", SELLER = "SS", DUEL = "FS";
    private String userName;
    private double userBalance;
    private String userType;
    private ArrayList<Game> inventory = new ArrayList<Game>();
    private ArrayList<String> userGames;
    public double auctionSale;

    /**
     * Game class constructor which constructs a new user based on the user's name, account balance, type and the
     * inventory of games and price the user owns
     * the auction sale.
     * @param userName the name of the user
     * @param userBalance the account balance of the user
     * @param userType the type of the user account
     * @param userGames the inventory of games and price of the user
     */
    public User(String userName, double userBalance, String userType, ArrayList<String> userGames){
        this.userName = userName;
        this.userBalance = userBalance;
        this.userType = userType;
        this.userGames = userGames;
        this.inventory = new ArrayList<>();
        for (int i = 0; i < this.userGames.size(); i = i + 2) {
            inventory.add(new Game(this.userGames.get(i), Double.parseDouble(this.userGames.get(i + 1)), this.userName,
                    this.auctionSale));
        }
    }

    /**
     * Get's the name of the user
     * @return the name of the user
     */
    public String getUserName() {
        return this.userName;
    }

    /**
     * Get's the balance of the user account
     * @return the balance of the user account
     */
    public double getUserBalance() {
        return this.userBalance;
    }

    /**
     * Get's the type of the user
     * @return the type of the user
     */
    public String getUserType() {
        return this.userType;
    }

    /**
     * Get's the inventory of games and price of the user
     * @return the inventory of games and price of the user
     */
    public ArrayList<String> getUserGames() { return this.userGames; }

    /**
     * Get's the inventory of games(with game objects) of the user
     * @return the inventory of games(with game objects) of the user
     */
    public ArrayList<Game> getInventory(){
        return this.inventory;
    }

    /**
     * Set's the user's games inventory of game name and price based on the new user games
     * @param newUserGames new user games
     */
    public void setUserGames(ArrayList<String> newUserGames){
        this.userGames = newUserGames;
    }

    /**
     * Adds balance to the user's account based on the credit inputted
     * @param credit the credit needed to be added into the user's account balance
     */
    public void addUserBalance(double credit) {
        this.userBalance += credit;
    }

    /**
     * Set's the user's inventory game to be the new inventory of games
     * @param inventory new inventory of games needed to be changed for the user
     */
    public void setInventory(ArrayList<Game> inventory){
        this.inventory = inventory;
    }

    /**
     * Adds game to the user's inventory of games
     * @param game game needed to be added into inventory
     */
    public void addGame(Game game){
        this.inventory.add(game);
    }

    /**
     * Adds game name and game price into user games of the user account
     * @param game game name needed to be added to user games
     * @param price game price needed to be added to user games
     */
    public void addUserGame(String game, String price){
        this.userGames.add(game);
        this.userGames.add(price);
    }

    /**
     * Remove the game from the user's inventory and runs the helper method removeUserGame to remove game and price
     * from "userGames"
     * @param game game needed to be removed from inventory and user games
     * @return true if game is removed from inventory and user games
     */
    public boolean removeGame(String game){
        boolean found = false;
        if(inventory.size() == 0){
            return false;
        }
        for(int i = 0; i < inventory.size(); i++){
            if(game.equals(inventory.get(i).getName())){
                found = true;
                inventory.remove(i);
                removeUserGame(game);
                break;
            }
        }
        return found;
    }

    /**
     * Helper method that removes game_name and it's price from userGames
     * @param game_name the game that need's to be removed from userGames
     */
    public void removeUserGame(String game_name){
        for(int i = 0; i < this.userGames.size(); i = i + 2){
            if(this.userGames.get(i).equals(game_name)){
                this.userGames.remove(i + 1);
                this.userGames.remove(i);
                break;
            }
        }
    }

    /**
     * Gets the game from inventory based on game_name
     * @param game_name the name of the game
     * @return null if game with game_name is not in users inventory
     */
    public Game getGame(String game_name){
        for(Game game: getInventory()){
            if(game.getName().equals(game_name)){
                return game;
            }
        }
        return null;
    }
    
    /**
     * Returns a string of all games and their prices in the user's inventory(userGames) with a dash added in between
     * every object in userGames
     * @return a string of all games and their prices with a '-' in between
     */
    public String toStringUserGames(){
        StringBuilder games = new StringBuilder();
        if(this.userGames.size() == 0){
            return "";
        }
        int counter = 0;
        for (String string: userGames){
            if(counter == 0) {
                games.append(string);
            }else{
                games.append('-').append(string);
            }
            counter += 1;
        }
        return games.toString();
    }
}
