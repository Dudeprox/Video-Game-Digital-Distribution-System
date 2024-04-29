/**
 * Game class implements and creates a game based on a game's name, price, auction sale percentage and the owner
 * of the game.
 */
public class Game {

    private double price = 0.00;
    private String name;
    private String owner;
    private double auctionSale;

	/**
	 * Game class constructor which constructs a new game based on the game's name, price, owner and the percentage of
	 * the auction sale.
	 * @param name the name of the game
	 * @param price the price of the game
	 * @param owner the owner of the game
	 * @param auctionSale the auction sale percentage of the game
	 */
    public Game(String name, double price, String owner, double auctionSale) {
        this.price = price;
        this.name = name;
        this.owner = owner;
        this.auctionSale = auctionSale;
    }

	/**
	 * Get's the name of the game
	 * @return the name of the game
	 */
	public String getName() { return name; }

	/**
	 * Get's the price of the game
	 * @return the price of the game
	 */
	public double getPrice() {
		return price;
	}

	/**
	 * Get's the owner of the game
	 * @return the owner of the game
	 */
	public String getOwner() {
		return owner;
	}

	/**
	 * Get's the auction sale percentage of the game
	 * @return the auction sale percentage of the game
	 */
	public double getAuctionSale() {
		return auctionSale;
	}
    
}
