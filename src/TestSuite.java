import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.lang.model.element.ModuleElement;
import javax.swing.text.TabableView;
import java.lang.reflect.Array;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class TestSuite {


    /*
     * ==== Test Helpers ====
     * This is a place to add helper functions for your test cases
     */
    // User File Must be
//    Michael,999994.0,SS,
//    NickCsgo,999999.99,AA,
//    Talha,68220.0,AA,Csgo-10.0
//    Nick,3506.16,FS,TFT2-90.0-GTA5-100.0
//    Haris,6307.72,AA,
    public boolean userOwnsGame(String userName, String gameName) {
        for (String game : UsersDatabase.Users.get(userName).getUserGames()) {
            if (game.equals(gameName)) {
                return true;
            }
        }
        return false;
    }

    public static boolean userSellingGame(String userName, String gameName) {

        boolean selling = false;
        boolean sellingToday = false;

        for (String sellingUserName : GamesDatabase.sellToday.keySet()) {
            if (sellingUserName.equals(userName)) {
                sellingToday = GamesDatabase.sellToday.get(sellingUserName).getName().equals(gameName);
            }
        }
        for (String game_name : GamesDatabase.selling.keySet()) {
            if (game_name.equals(gameName)) {
                selling = GamesDatabase.selling.get(gameName).getOwner().equals(userName);
            }
        }
        return selling || sellingToday;
//        if (selling && !sellingToday){
//            return true;
//        }
//        else if (!selling && sellingToday){
//            return true;
//        }else {
//            return selling && sellingToday;
//        }
    }

    /*
     * ==== Test Cases ====
     * This is where you should add your test cases!
     */
    User talha;
    User nick;
    User haris;
    User michael;
    User buyer;

    @BeforeEach
    public void setUp() {
        // This runs before each test method, so we have a fresh OnitamaBoard for each
        // test.
        // Talha must be in the file and database.
        UsersDatabase.main(new String[]{""});

        talha = UsersDatabase.Users.get("Talha");

//        Create.execute(TransactionsModifier.currentUser, "01 Talha           AA 068220.00");
//        UsersDatabase.main(new String[]{""});
//        Game game = new Game("Csgo", 10.0, talha.getUserName(), 0.0);
//        UsersDatabase.Users.get("Talha").userGames.add(game.getName());
//        UsersDatabase.Users.get("Talha").userGames.add(Double.toString(game.getPrice()));


        ArrayList<String> nickGames = new ArrayList<>();
        nickGames.add("TFT2");
        nickGames.add("90.0");
        nickGames.add("GTA5");
        nickGames.add("100.0");
        nick = new User("Tes1", 3506.16, User.DUEL, nickGames);
        UsersDatabase.Users.put(nick.getUserName(), nick);
        TransactionsModifier.dailyAdd.put(nick.getUserName(), 0.0);


        ArrayList<String> harisGames = new ArrayList<>();
        haris = new User("Test2", 6307.72, User.ADMIN, harisGames);
        UsersDatabase.Users.put(haris.getUserName(), haris);
        TransactionsModifier.dailyAdd.put(haris.getUserName(), 0.0);

        ArrayList<String> michaelGames = new ArrayList<>();
        michael = new User("Test3", 999994.0, User.SELLER, michaelGames);
        UsersDatabase.Users.put(michael.getUserName(), michael);
        TransactionsModifier.dailyAdd.put(michael.getUserName(), 0.0);

        ArrayList<String> buyerGames = new ArrayList<>();
        buyer = new User("Test4", 25.0, User.BUYER, buyerGames);
        UsersDatabase.Users.put(buyer.getUserName(), buyer);
        TransactionsModifier.dailyAdd.put(buyer.getUserName(), 0.0);
    }

    @Test
    public void testSell(){

//        XX IIIIIIIIIIIIIIIIIII SSSSSSSSSSSSS DDDDD PPPPPP 04


        // As a (Non-Seller) Buyer I want to sell a Game (Rocket League for 25.0 with 10 percent discount)
        // (Shouldn't Do anything)

        // Login as a Buyer
        TransactionsModifier.currentUser = buyer;
        System.out.println("User: " + buyer.getUserName() + " has logged in!" + "\n");


        String transactionSell = "03 Rocket League             Test4           10.00 025.00";

        String gameName = transactionSell.substring(3, 28).trim();
        String sellerName = transactionSell.substring(29, 44).trim();
//        double price = Double.parseDouble(transactionSell.substring(51).trim());
//        double saleDiscount = Double.parseDouble(transactionSell.substring(45, 50).trim());

        System.out.println("____________BEFORE_________________");
        System.out.println("Game Selling Expected: " + false + "\n");
        System.out.println("Game Selling by User Exists: " + userSellingGame(sellerName, gameName) + "\n");
        assertFalse(userSellingGame(sellerName, gameName));
        System.out.println(transactionSell);
        System.out.println("____________AFTER_________________");
        Sell.execute(transactionSell);
        System.out.println("Game Selling Expected: " + false + "\n");
        System.out.println("Game Selling by User Exists: " + userSellingGame(sellerName, gameName) + "\n");
        assertFalse(userSellingGame(sellerName, gameName));



        // As a seller I want to sell a Game (Rocket League for 999.99 with 90 percent discount)
        // (Puts a Game up for Sale)

        // If game was already up for sale it should be in selling and not sellingToday

        // Login in as a seller
        TransactionsModifier.currentUser = michael;
        System.out.println("User: " + michael.getUserName() + " has logged in!" + "\n");

        String transactionSell1 = "03 Rocket League             Test3           90.00 999.99";

        String gameName1 = transactionSell1.substring(3, 28).trim();
        String sellerName1 = transactionSell1.substring(29, 44).trim();
        double price1 = Double.parseDouble(transactionSell1.substring(51).trim());
        double saleDiscount1 = Double.parseDouble(transactionSell1.substring(45, 50).trim());

        System.out.println("____________BEFORE_________________");
        System.out.println("Game Selling Expected: " + false + "\n");
        System.out.println("Game Selling by User Exists: " + userSellingGame(sellerName1, gameName1) + "\n");
        assertFalse(userSellingGame(sellerName1, gameName1));


        System.out.println(transactionSell1);
        System.out.println("____________AFTER_________________");
        Sell.execute(transactionSell1);
        System.out.println("Game Selling Expected: " + true + "\n");
        System.out.println("Game Selling by User Exists: " + userSellingGame(sellerName1, gameName1) + "\n");
        assertTrue(userSellingGame(sellerName1, gameName1));

        Game sellingGame = null;
        if(GamesDatabase.selling.containsKey(sellerName1)
                && GamesDatabase.selling.get(sellerName1).getName().equals(gameName1)
                && !GamesDatabase.sellToday.containsKey(sellerName1) ){
            sellingGame = GamesDatabase.selling.get(sellerName1);
        }
        else if (!GamesDatabase.selling.containsKey(sellerName1) && GamesDatabase.sellToday.containsKey(sellerName1)
                && GamesDatabase.sellToday.get(sellerName1).getName().equals(gameName1)){
            sellingGame = GamesDatabase.sellToday.get(sellerName1);
        }
        assertNotNull(sellingGame);
        assertEquals(price1, sellingGame.getPrice());
        assertEquals(saleDiscount1, sellingGame.getAuctionSale());


        // As a seller I want to sell a Game (Rocket League for 999.99 with 90 percent discount) which is
        //already up for sale. (Shouldn't Do anything)
        System.out.println("____________BEFORE_________________");
        System.out.println("Game Selling Expected: " + true + "\n");
        System.out.println("Game Selling by User Exists: " + userSellingGame(sellerName1, gameName1) + "\n");
        assertTrue(userSellingGame(sellerName1, gameName1));

        System.out.println(transactionSell1);
        System.out.println("____________AFTER_________________");
        Sell.execute(transactionSell1);
        System.out.println("Game Selling Expected: " + true + "\n");
        System.out.println("Game Selling by User Exists: " + userSellingGame(sellerName1, gameName1) + "\n");
        assertTrue(userSellingGame(sellerName1, gameName1));

        if(GamesDatabase.selling.containsKey(sellerName1)
                && GamesDatabase.selling.get(sellerName1).getName().equals(gameName1)
                && !GamesDatabase.sellToday.containsKey(sellerName1) ){
            sellingGame = GamesDatabase.selling.get(sellerName1);
        }
        else if (!GamesDatabase.selling.containsKey(sellerName1) && GamesDatabase.sellToday.containsKey(sellerName1)
                && GamesDatabase.sellToday.get(sellerName1).getName().equals(gameName1)){
            sellingGame = GamesDatabase.sellToday.get(sellerName1);
        }
        assertNotNull(sellingGame);
        assertEquals(price1, sellingGame.getPrice());
        assertEquals(saleDiscount1, sellingGame.getAuctionSale());

        // As a seller I want to sell a Game (Expensive One for 1000.0 with 10 percent discount)
        // (Shouldn't Do anything)
        String transactionSell2 = "03 Expensive One             Test3           10.00 1000.00";

        String gameName2 = transactionSell2.substring(3, 28).trim();
        String sellerName2 = transactionSell2.substring(29, 44).trim();

        System.out.println("____________BEFORE_________________");
        System.out.println("Game Selling Expected: " + false + "\n");
        System.out.println("Game Selling by User Exists: " + userSellingGame(sellerName2, gameName2) + "\n");
        assertFalse(userSellingGame(sellerName2, gameName2));

        System.out.println(transactionSell2);
        System.out.println("____________AFTER_________________");
        Sell.execute(transactionSell2);
        System.out.println("Game Selling Expected: " + false + "\n");
        System.out.println("Game Selling by User Exists: " + userSellingGame(sellerName2, gameName2) + "\n");
        assertFalse(userSellingGame(sellerName2, gameName2));

        Game sellingGame1 = null;

        if(GamesDatabase.selling.containsKey(sellerName2)
                && GamesDatabase.selling.get(sellerName2).getName().equals(gameName2)
                && !GamesDatabase.sellToday.containsKey(sellerName2) ){
            sellingGame1 = GamesDatabase.selling.get(sellerName2);
        }
        else if (!GamesDatabase.selling.containsKey(sellerName2) && GamesDatabase.sellToday.containsKey(sellerName2)
                && GamesDatabase.sellToday.get(sellerName2).getName().equals(gameName2)){
            sellingGame1 = GamesDatabase.sellToday.get(sellerName2);
        }
        assertNull(sellingGame1);

        //Remove game otherwise the test when Re-Run will not work
        GamesDatabase.removeGame(michael, "Rocket League");

        // As an Admin I can put up RedDead for 500.0 dollars with 50 percent discount. (Should put the game up for
        // sale)
        TransactionsModifier.currentUser = talha;
        System.out.println("User: " + talha.getUserName() + " has logged in!" + "\n");

        String transactionSell3 = "03 RedDead                   Talha           50.00 500.00";

        String gameName3 = transactionSell3.substring(3, 28).trim();
        String sellerName3 = transactionSell3.substring(29, 44).trim();
        double price3 = Double.parseDouble(transactionSell3.substring(51).trim());
        double saleDiscount3 = Double.parseDouble(transactionSell3.substring(45, 50).trim());

        System.out.println("____________BEFORE_________________");
        System.out.println("Game Selling Expected: " + false + "\n");
        System.out.println("Game Selling by User Exists: " + userSellingGame(sellerName3, gameName3) + "\n");
        assertFalse(userSellingGame(sellerName3, gameName3));


        System.out.println(transactionSell3);
        System.out.println("____________AFTER_________________");
        Sell.execute(transactionSell3);
        System.out.println("Game Selling Expected: " + true + "\n");
        System.out.println("Game Selling by User Exists: " + userSellingGame(sellerName3, gameName3) + "\n");
        assertTrue(userSellingGame(sellerName3, gameName3));

        Game sellingGame3 = null;

        if(GamesDatabase.selling.containsKey(sellerName3)
                && GamesDatabase.selling.get(sellerName3).getName().equals(gameName3)
                && !GamesDatabase.sellToday.containsKey(sellerName3) ){
            sellingGame3 = GamesDatabase.selling.get(sellerName3);
        }
        else if (!GamesDatabase.selling.containsKey(sellerName3) && GamesDatabase.sellToday.containsKey(sellerName3)
                && GamesDatabase.sellToday.get(sellerName3).getName().equals(gameName3)){
            sellingGame3 = GamesDatabase.sellToday.get(sellerName3);
        }
        assertNotNull(sellingGame3);
        assertEquals(price3, sellingGame3.getPrice());
        assertEquals(saleDiscount3, sellingGame3.getAuctionSale());

        //Remove game otherwise the test when Re-Run will not work
        GamesDatabase.removeGame(talha, "RedDead");

        // As an FS User I wan to put up Pac-Man for 20.0 dollars with 50 percent discount. (Should put the game up for
        // sale)
        TransactionsModifier.currentUser = nick;
        System.out.println("User: " + nick.getUserName() + " has logged in!" + "\n");

        String transactionSell4 = "03 Pac-man                   Tes1            50.00 020.00";

        String gameName4 = transactionSell4.substring(3, 28).trim();
        String sellerName4 = transactionSell4.substring(29, 44).trim();
        double price4 = Double.parseDouble(transactionSell4.substring(51).trim());
        double saleDiscount4 = Double.parseDouble(transactionSell4.substring(45, 50).trim());

        System.out.println("____________BEFORE_________________");
        System.out.println("Game Selling Expected: " + false + "\n");
        System.out.println("Game Selling by User Exists: " + userSellingGame(sellerName4, gameName4) + "\n");
        assertFalse(userSellingGame(sellerName4, gameName4));


        System.out.println(transactionSell4);
        System.out.println("____________AFTER_________________");
        Sell.execute(transactionSell4);
        System.out.println("Game Selling Expected: " + true + "\n");
        System.out.println("Game Selling by User Exists: " + userSellingGame(sellerName4, gameName4) + "\n");
        assertTrue(userSellingGame(sellerName4, gameName4));

        Game sellingGame4 = null;

        if(GamesDatabase.selling.containsKey(sellerName4)
                && GamesDatabase.selling.get(sellerName4).getName().equals(gameName4)
                && !GamesDatabase.sellToday.containsKey(sellerName4) ){
            sellingGame4 = GamesDatabase.selling.get(sellerName4);
        }
        else if (!GamesDatabase.selling.containsKey(sellerName4) && GamesDatabase.sellToday.containsKey(sellerName4)
                && GamesDatabase.sellToday.get(sellerName4).getName().equals(gameName4)){
            sellingGame4 = GamesDatabase.sellToday.get(sellerName4);
        }
        assertNotNull(sellingGame4);
        assertEquals(price4, sellingGame4.getPrice());
        assertEquals(saleDiscount4, sellingGame4.getAuctionSale());

        //Remove game otherwise the test when Re-Run will not work
        GamesDatabase.removeGame(nick, "Pac-Man");

        System.out.println("User: " + nick.getUserName() + " has logged off!" + "\n");
        TransactionsModifier.currentUser = null;
    }

    @Test
    public void testRefund() {
        //admin making refund, everything here is fine
        System.out.println("Admin Test2 refunding 500 credits from user Test3 to user Tes1");
        TransactionsModifier.currentUser = haris;

        String transactionrefund = "05 Tes1            Test3           000500.00";
        double expectedbuyercredit = nick.getUserBalance() + 500;
        double expectedsellercredit = michael.getUserBalance() - 500;

        System.out.println("Tes1 balance before execution: " + nick.getUserBalance());
        System.out.println("Test3 balance before execution: " + michael.getUserBalance());

        Refund.execute(transactionrefund);
        System.out.println("Tes1 balance after execution: " + nick.getUserBalance());
        System.out.println("Test3 balance after execution: " + michael.getUserBalance());
        assertEquals(expectedbuyercredit, nick.getUserBalance());
        assertEquals(expectedsellercredit, michael.getUserBalance());
        //transaction goes through

        System.out.println("-----------------------------");
        //non-admin trying to make transaction, should NOT go through as refund is privileged transaction
        System.out.println("Admin Test2 refunding 500 credits from user Test3 to user Tes1");
        TransactionsModifier.currentUser = nick;
        String transactionrefund2 = "05 Tes1            Test3           000500.00";
        double expectedbuyercredit2 = nick.getUserBalance();
        double expectedsellercredit2 = michael.getUserBalance();
        System.out.println("Tes1 balance before execution: " + nick.getUserBalance());
        System.out.println("Test3 balance before execution: " + michael.getUserBalance());
        Refund.execute(transactionrefund2);
        System.out.println("Tes1 balance after execution: " + nick.getUserBalance());
        System.out.println("Test3 balance after execution: " + michael.getUserBalance());
        assertEquals(expectedbuyercredit2, nick.getUserBalance());
        assertEquals(expectedsellercredit2, michael.getUserBalance());
        //neither buyer nor seller's balance should change as the transaction yielded an error, thus not completing
        System.out.println("-----------------------------");
        TransactionsModifier.currentUser = talha;
        String transactionrefund3 = "05 Talha           Test3           000500.00";
        double talhaBalance = talha.getUserBalance();
        double expectedbuyercredit3 = talhaBalance + 500;
        double expectedsellercredit3 = michael.getUserBalance() - 500;
        System.out.println("Tes1 balance before execution: " + talha.getUserBalance());
        System.out.println("Test3 balance before execution: " + michael.getUserBalance());
        Refund.execute(transactionrefund3);
        System.out.println("Tes1 balance after execution: " + talha.getUserBalance());
        System.out.println("Test3 balance after execution: " + michael.getUserBalance());
        assertEquals(expectedbuyercredit3, talha.getUserBalance());
        assertEquals(expectedsellercredit3, michael.getUserBalance());

        UsersDatabase.addCredit(talhaBalance, talha);
        Delete.execute("02 Tes1            FS 004006.16" );
        Delete.execute("02 Test3           SS 999494.00" );
        TransactionsModifier.currentUser = null;
        System.out.println("User: " + talha.getUserName() + " has logged off!" + "\n");

    }

    @Test
    public void testRemoveGame(){

//        Michael,999994.0,SS,
//        NickCsgo,999999.99,AA,
//        Talha,68220.0,AA,Csgo-10.0
//        Nick,3506.16,FS,TFT2-90.0-GTA5-100.0
//        Haris,6307.72,AA,

        // This method must only be ran once on this file.

        // Nick (Non-Admin User) Login
        TransactionsModifier.currentUser = nick;
        System.out.println("User: " + nick.getUserName() + " has logged in!" + "\n");


        // As a Non Admin, I remove a game from a different user other than my own.
        String transactionRemoveGame = "08 Csgo                      Talha          ";
        String game = transactionRemoveGame.substring(3, 28).trim();
        String owner = transactionRemoveGame.substring(29).trim();


        System.out.println("____________BEFORE_________________");
        System.out.println("Game Owning Expected: " + true + "\n");
        System.out.println("Game Owned by User Exists: " + userOwnsGame(owner,game) + "\n");
        assertTrue(userOwnsGame(owner,game));
        System.out.println(transactionRemoveGame);
        System.out.println("____________AFTER_________________");
        RemoveGame.execute(transactionRemoveGame);
        System.out.println("Game Owning Expected: " + true + "\n");
        System.out.println("Game Owned by User Exists: " + userOwnsGame(owner,game) + "\n");
        assertTrue(userOwnsGame(owner,game));


        // As a Non Admin, I remove a game from my own inventory.
        String transactionRemoveGame2 = "08 TFT2                      Tes1           ";

        String game1 = transactionRemoveGame2.substring(3, 28).trim();
        String owner1 = transactionRemoveGame2.substring(29).trim();


        System.out.println("____________BEFORE_________________");
        System.out.println("Game Owning Expected: " + true + "\n");
        System.out.println("Game Owned by User Exists: " + userOwnsGame(owner1,game1) + "\n");
        assertTrue(userOwnsGame(owner1,game1));
        System.out.println(transactionRemoveGame2);
        System.out.println("____________AFTER_________________");
        RemoveGame.execute(transactionRemoveGame2);
        System.out.println("Game Owning Expected: " + false + "\n");
        System.out.println("Game Owned by User Exists: " + userOwnsGame(owner1,game1) + "\n");
        assertFalse(userOwnsGame(owner1,game1));


        // As a Non Admin, I remove a game from my own inventory that I dont own.
        String transactionRemoveGame3 = "08 Csgo                      Tes1           ";


        String game2 = transactionRemoveGame3.substring(3, 28).trim();
        String owner2 = transactionRemoveGame3.substring(29).trim();


        System.out.println("____________BEFORE_________________");
        System.out.println("Game Owning Expected: " + false + "\n");
        System.out.println("Game Owned by User Exists: " + userOwnsGame(owner2,game2) + "\n");
        assertFalse(userOwnsGame(owner2,game2));
        System.out.println(transactionRemoveGame3);
        System.out.println("____________AFTER_________________");
        RemoveGame.execute(transactionRemoveGame3);
        System.out.println("Game Owning Expected: " + false + "\n");
        System.out.println("Game Owned by User Exists: " + userOwnsGame(owner2,game2) + "\n");
        assertFalse(userOwnsGame(owner2,game2));


        // Talha (Admin User) Login
        TransactionsModifier.currentUser = null;
        System.out.println("User: " + nick.getUserName() + " has logged off!" + "\n");

        TransactionsModifier.currentUser = talha;
        System.out.println("User: " + nick.getUserName() + " has logged in!" + "\n");

        // As an Admin, Talha I remove Nick's (Another User) game
        String transactionRemoveGame4 = "08 GTA5                      Tes1           ";

        String game3 = transactionRemoveGame4.substring(3, 28).trim();
        String owner3 = transactionRemoveGame4.substring(29).trim();


        System.out.println("____________BEFORE_________________");
        System.out.println("Game Owning Expected: " + true + "\n");
        System.out.println("Game Owned by User Exists: " + userOwnsGame(owner3,game3) + "\n");
        assertTrue(userOwnsGame(owner3,game3));
        System.out.println(transactionRemoveGame4);
        System.out.println("____________AFTER_________________");
        RemoveGame.execute(transactionRemoveGame4);
        System.out.println("Game Owning Expected: " + false + "\n");
        System.out.println("Game Owned by User Exists: " + userOwnsGame(owner3,game3) + "\n");
        assertFalse(userOwnsGame(owner3,game3));



        // As an Admin, Talha I remove Nick's (Another User) game, which he doesn't own
        String transactionRemoveGame5 = "08 TFT2                      Tes1           ";

        String game4 = transactionRemoveGame5.substring(3, 28).trim();
        String owner4 = transactionRemoveGame5.substring(29).trim();


        System.out.println("____________BEFORE_________________");
        System.out.println("Game Owning Expected: " + false + "\n");
        System.out.println("Game Owned by User Exists: " + userOwnsGame(owner4,game4) + "\n");
        assertFalse(userOwnsGame(owner4,game4));
        System.out.println(transactionRemoveGame5);
        System.out.println("____________AFTER_________________");
        RemoveGame.execute(transactionRemoveGame5);
        System.out.println("Game Owning Expected: " + false + "\n");
        System.out.println("Game Owned by User Exists: " + userOwnsGame(owner4,game4) + "\n");
        assertFalse(userOwnsGame(owner4,game4));


        // As an Admin, Talha I remove my own game csgo
        String transactionRemoveGame6 = "08 Csgo                      Talha          ";

        String game5 = transactionRemoveGame6.substring(3, 28).trim();
        String owner5 = transactionRemoveGame6.substring(29).trim();


        System.out.println("____________BEFORE_________________");
        System.out.println("Game Owning Expected: " + true + "\n");
        System.out.println("Game Owned by User Exists: " + userOwnsGame(owner5,game5) + "\n");
        assertTrue(userOwnsGame(owner5,game5));
        System.out.println(transactionRemoveGame6);
        System.out.println("____________AFTER_________________");
        RemoveGame.execute(transactionRemoveGame6);
        System.out.println("Game Owning Expected: " + false + "\n");
        System.out.println("Game Owned by User Exists: " + userOwnsGame(owner5,game5) + "\n");
        assertFalse(userOwnsGame(owner5,game5));

        // As a seller I want to remove a game I have put up for sale.  DO THISS

        Delete.execute("02 Tes1            FS 003506.16" );
        talha.addUserGame("Csgo", "10.0");
        UsersDatabase.updateGame(talha);
        TransactionsModifier.currentUser = null;
        System.out.println("User: " + talha.getUserName() + " has logged off!" + "\n");


    }

    @Test
    public void testLog_in_out(){
//        Have users.txt in this format:

//        Michael,999994.0,SS,
//        NickCsgo,999999.99,AA,
//        Talha,68220.0,AA,Csgo-10.0
//        Nick,3506.16,FS,TFT2-90.0-GTA5-100.0
//        Haris,6307.72,AA,

        // Try logging out before logging in!
        String transaction1 = "10 Talha           AA 068220.00";
        TransactionsReader.transactions.add(transaction1);
        TransactionsModifier.main(new String[]{});
        TransactionsReader.transactions.remove(transaction1);
        assertNull(TransactionsModifier.currentUser);

        // Log in user Talha
        String transaction4 = "00 Talha           AA 068220.00";
        TransactionsReader.transactions.add(transaction4);
        TransactionsModifier.main(new String[]{});
        TransactionsReader.transactions.remove(transaction4);
        assertEquals(talha.getUserName(), TransactionsModifier.currentUser.getUserName());

        // Try logging in different user when already logged in
        String transaction5 = "00 Haris           AA 006307.72";
        TransactionsReader.transactions.add(transaction5);
        TransactionsModifier.main(new String[]{});
        TransactionsReader.transactions.remove(transaction5);
        assertEquals(talha.getUserName(), TransactionsModifier.currentUser.getUserName());

        // Try Logging out with a different user than the user that is logged in
        String transaction6 = "10 Haris           AA 006307.72";
        TransactionsReader.transactions.add(transaction6);
        TransactionsModifier.main(new String[]{});
        TransactionsReader.transactions.remove(transaction6);
        assertEquals(talha.getUserName(), TransactionsModifier.currentUser.getUserName());


        // Log out of user talha
        String transaction9 = "10 Talha           AA 068220.00";
        TransactionsReader.transactions.add(transaction9);
        TransactionsModifier.main(new String[]{});
        TransactionsReader.transactions.remove(transaction9);
        assertNull(TransactionsModifier.currentUser);
    }

    @Test
    public void testDelete(){

//        Michael,999994.0,SS,
//        NickCsgo,999999.99,AA,
//        Talha,68220.0,AA,Csgo-10.0
//        Haris,7307.72,AA,
//        Nick,3706.16,FS,

        String transactionDelete = "02 Haroon          SS 000420.00";

        TransactionsModifier.currentUser = nick;
        System.out.println("User: " + nick.getUserName() + " has logged in!" + "\n");

        // Nick as an FS User shouldn't be able to delete a user.
        String transactionCreate1 = "01 Haroon          SS 000420.00";

        String newUsername = transactionCreate1.substring(3, 18).trim();
        String newUserType = transactionCreate1.substring(19, 21).trim();
        double newUserCredit = Double.parseDouble(transactionCreate1.substring(22).trim());

        if (!UsersDatabase.Users.containsKey(newUsername)){
            TransactionsModifier.currentUser = talha;
            Create.execute(transactionCreate1);
            TransactionsModifier.currentUser = nick;
        }
        UsersDatabase.main(new String[]{});

        System.out.println("____________BEFORE_________________");
        System.out.println("Expected: " + true + "\n");
        System.out.println("User Exists: " + UsersDatabase.Users.containsKey(newUsername) + "\n");
        assertTrue(UsersDatabase.Users.containsKey(newUsername));

        int expectednumOfUsers = 0;
        for (String userNames: UsersDatabase.Users.keySet()){
            expectednumOfUsers++;
        }

        System.out.println("____________AFTER_________________");
        Delete.execute(transactionDelete);
        System.out.println("Expected: " + true + "\n");
        System.out.println("User Exists: " + UsersDatabase.Users.containsKey(newUsername) + "\n");
        assertTrue(UsersDatabase.Users.containsKey(newUsername));

        int actualNumOfUsers = 0;
        for (String userNames: UsersDatabase.Users.keySet()){
            actualNumOfUsers++;
        }

        System.out.println("Expected Users: " + expectednumOfUsers + "\n");
        System.out.println("User Exists: " + actualNumOfUsers + "\n");
        assertEquals(expectednumOfUsers, actualNumOfUsers);
        System.out.println("-----------------------------------");


        // As an Admin I should be able to delete a user.
        // Occurrences of User
        System.out.println("User: " + nick.getUserName() + " has logged off!" + "\n");

        TransactionsModifier.currentUser = talha;
        System.out.println("User: " + talha.getUserName() + " has logged in!" + "\n");


        System.out.println("____________BEFORE_________________");
        System.out.println("Expected: " + true + "\n");
        System.out.println("User Exists: " + UsersDatabase.Users.containsKey(newUsername) + "\n");
        assertTrue(UsersDatabase.Users.containsKey(newUsername));

        System.out.println("____________AFTER_________________");
        Delete.execute(transactionDelete);
        System.out.println("Expected: " + false + "\n");
        System.out.println("User Exists: " + UsersDatabase.Users.containsKey(newUsername) + "\n");
        assertFalse(UsersDatabase.Users.containsKey(newUsername));

        int occurrences = 0;
        for (String userNames: UsersDatabase.Users.keySet()){
            if(userNames.equals(newUsername)){
                occurrences++;
            }
        }
        System.out.println("Expected occurrences: " + 0 + "\n");
        System.out.println("User occurrences: " + occurrences + "\n");
        assertEquals(0, occurrences);
        // As an admin I cannot delete a user doesn't exist.

        int beforeActualOccurrences = 0;
        for (String userNames: UsersDatabase.Users.keySet()){
            if(userNames.equals(newUsername)){
                beforeActualOccurrences++;
            }
        }

        System.out.println("____________BEFORE_________________");
        System.out.println("Expected: " + false + "\n");
        System.out.println("User Exists: " + UsersDatabase.Users.containsKey(newUsername) + "\n");
        assertFalse(UsersDatabase.Users.containsKey(newUsername));
        System.out.println("Expected Occurrences: " + 0 + "\n");
        System.out.println("User Occurrences: " + beforeActualOccurrences + "\n");
        assertEquals(0, beforeActualOccurrences);

        System.out.println("____________AFTER_________________");
        Delete.execute(transactionDelete);
        System.out.println("Expected: " + false + "\n");
        System.out.println("User Exists: " + UsersDatabase.Users.containsKey(newUsername) + "\n");
        assertFalse(UsersDatabase.Users.containsKey(newUsername));

        int afterActualOccurrences = 0;
        for (String userNames: UsersDatabase.Users.keySet()){
            if(userNames.equals(newUsername)){
                afterActualOccurrences++;
            }
        }

        System.out.println("Expected Occurrences: " + 0 + "\n");
        System.out.println("User Occurrences: " + afterActualOccurrences + "\n");
        assertEquals(0, afterActualOccurrences);

        TransactionsModifier.currentUser = null;
        System.out.println("User: " + talha.getUserName() + " has logged off!" + "\n");

    }

    @Test
    public void testCreate(){

//        Michael,999994.0,SS,[]
//        NickCsgo,999999.99,AA,[]
//        Talha,68220.0,AA,[Csgo-10.0]
//        Haris,7307.72,AA,[]
//        Nick,3706.16,FS,[]

        String transactionDelete = "02 Haroon          SS 000420.00";
        TransactionsModifier.currentUser = nick;
        System.out.println("User: " + nick.getUserName() + " has logged in!" + "\n");

        // Nick as an FS User shouldn't be able to create a new user.
        String transactionCreate1 = "01 Haroon          SS 000420.00";
        String newUsername = transactionCreate1.substring(3, 18).trim();
        String newUserType = transactionCreate1.substring(19, 21).trim();
        double newUserCredit = Double.parseDouble(transactionCreate1.substring(22).trim());

        if (UsersDatabase.Users.containsKey(newUsername)){
            TransactionsModifier.currentUser = talha;
            Delete.execute(transactionDelete);
            TransactionsModifier.currentUser = nick;
        }

        System.out.println("____________BEFORE_________________");
        System.out.println("Expected: " + false + "\n");
        System.out.println("User Exists: " + UsersDatabase.Users.containsKey(newUsername) + "\n");
        assertFalse(UsersDatabase.Users.containsKey(newUsername));


        System.out.println("____________AFTER_________________");
        Create.execute(transactionCreate1);
        System.out.println("Expected: " + false + "\n");
        System.out.println("User Exists: " + UsersDatabase.Users.containsKey(newUsername) + "\n");
        assertFalse(UsersDatabase.Users.containsKey(newUsername));
        System.out.println("-----------------------------------");


        // As an Admin I should be able to create a new user.
            // Check balance is <= 999,999.99
            // Check Balance matches amount given, Check Name matches given, and type matches given

        TransactionsModifier.currentUser = null;
        System.out.println("User: " + nick.getUserName() + " has logged off!" + "\n");

        TransactionsModifier.currentUser = talha;
        System.out.println("User: " + talha.getUserName() + " has logged in!" + "\n");


        System.out.println("____________BEFORE_________________");
        System.out.println("Expected: " + false + "\n");
        System.out.println("User Exists: " + UsersDatabase.Users.containsKey(newUsername) + "\n");
        assertFalse(UsersDatabase.Users.containsKey(newUsername));

        System.out.println("____________AFTER_________________");
        Create.execute(transactionCreate1);
        System.out.println("Expected: " + true + "\n");
        System.out.println("User Exists: " + UsersDatabase.Users.containsKey(newUsername) + "\n");
        UsersDatabase.main(new String[]{});
        assertTrue(UsersDatabase.Users.containsKey(newUsername));
        boolean checkBalance = UsersDatabase.Users.get(newUsername).getUserBalance() == newUserCredit;
        assertTrue(checkBalance);
        boolean checkUserType = UsersDatabase.Users.get(newUsername).getUserType().equals(newUserType);
        assertTrue(checkUserType);

        // As an admin I cannot create a user that already exists.

        int occurrences = 0;
        for (String userNames: UsersDatabase.Users.keySet()){
            if(userNames.equals(newUsername)){
                occurrences++;
            }
        }
        System.out.println("____________BEFORE_________________");
        System.out.println("Expected: " + true + "\n");
        System.out.println("User Exists: " + UsersDatabase.Users.containsKey(newUsername) + "\n");
        System.out.println("Expected Occurrences: " + 1 + "\n");
        System.out.println("User Occurrences: " + occurrences + "\n");
        assertEquals(1, occurrences);

        System.out.println("____________AFTER_________________");
        Create.execute(transactionCreate1);
        System.out.println("Expected: " + true + "\n");
        System.out.println("User Exists: " + UsersDatabase.Users.containsKey(newUsername) + "\n");
        System.out.println("Expected Occurrences: " + 1 + "\n");
        System.out.println("User Occurrences: " + occurrences + "\n");

        boolean checkBalance1 = UsersDatabase.Users.get(newUsername).getUserBalance() == newUserCredit;
        assertTrue(checkBalance1);
        boolean checkUserType1 = UsersDatabase.Users.get(newUsername).getUserType().equals(newUserType);
        assertTrue(checkUserType1);

        Delete.execute("02 Haroon          SS 000420.00" );

        TransactionsModifier.currentUser = null;
        System.out.println("User: " + talha.getUserName() + " has logged off!" + "\n");

    }


    @Test
    public void testAddCredit(){


//        Michael,999994.0,SS,
//                NickCsgo,999999.99,AA,
//                Talha,68220.0,AA,TFT2-90.0
//        Haris,2500.0,AA,
//                Nick,4706.16,BS,


        TransactionsModifier.currentUser = nick;
        String transactionaddcredit = "06 Tes1            AA 000100.00";
        double expected = nick.getUserBalance() + 100;
        System.out.println("Expected: " + expected);
        System.out.println("Balance before execution: " + nick.getUserBalance());
        AddCredit.execute(transactionaddcredit);
        System.out.println("Balance after execution: " + nick.getUserBalance());
        assertEquals(expected, nick.getUserBalance());
        System.out.println("-----------------------------");

        TransactionsModifier.currentUser = haris;
        String transactionaddcredit2 = "06 Test2           BS 001000.00";
        double expected2  = haris.getUserBalance() + 1000;
        System.out.println("Expected: " + expected2);
        System.out.println("Balance before execution: " + haris.getUserBalance());
        AddCredit.execute(transactionaddcredit2);
        System.out.println("Balance after execution: " + haris.getUserBalance());
        assertEquals(expected2, haris.getUserBalance());
        System.out.println("-----------------------------");
        String transactionaddcredit3 = "06 Tes1            AA 000100.00";
        double expected3  = nick.getUserBalance();
        System.out.println("Expected: " + expected3);
        System.out.println("Balance before execution: " + nick.getUserBalance());
        AddCredit.execute(transactionaddcredit3);
        System.out.println("Balance after execution: " + nick.getUserBalance());
        System.out.println("-----------------------------");
        TransactionsModifier.currentUser = nick;
        String transactionaddcredit4 = "06 Tes1            BS 000500.00";
        double expected4  = nick.getUserBalance() + 500;
        System.out.println("Expected: " + expected4);
        System.out.println("Balance before execution: " + nick.getUserBalance());
        AddCredit.execute(transactionaddcredit4);
        System.out.println("Balance after execution: " + nick.getUserBalance());
        assertEquals(expected4, nick.getUserBalance());
        TransactionsModifier.currentUser = null;
        System.out.println("-----------------------------");
        TransactionsModifier.currentUser = haris;
        String transactionaddcredit5 = "06 Test2           BS 000500.00";
        double expected5  = haris.getUserBalance();
        System.out.println("Expected: " + expected5);
        System.out.println("Balance before execution: " + haris.getUserBalance());
        AddCredit.execute(transactionaddcredit5);
        System.out.println("Balance after execution: " + haris.getUserBalance());
        assertEquals(expected5, haris.getUserBalance());
        TransactionsModifier.currentUser = talha;
        String transactionaddcredit6 = "06 Tes1            BS 000500.00";
        double expected6  = nick.getUserBalance();
        System.out.println("Expected: " + expected6);
        System.out.println("Balance before execution: " + nick.getUserBalance());
        AddCredit.execute(transactionaddcredit6);
        System.out.println("Balance after execution: " + nick.getUserBalance());
        assertEquals(expected6, nick.getUserBalance());


        Delete.execute("02 Tes1            FS 004106.16" );
        Delete.execute("02 Test2           AA 007307.72" );

        TransactionsModifier.currentUser = null;
        System.out.println("User: " + haris.getUserName() + " has logged off!" + "\n");
    }

    @Test
    public void testGift() {

        // Let an Admin be Logged IN!
        // This test must only be run once.
        System.out.println("\n");
        TransactionsModifier.currentUser = talha;
        System.out.println("Admin: " + talha.getUserName() + " has logged in!" + "\n");

        //Nick,3506.16,FS,TFT2-90.0-GTA5-100.0
        //Haris,6307.72,AA,
        //Talha,68220.0,AA,Csgo-10.0

        // Admin Gives GTA5 from Nick to Haris

        String transaction2 = "09 GTA5                      Tes1            Test2          ";

        System.out.println("____________BEFORE_________________");
        ArrayList<String> nickExpected = new ArrayList<>();
        ArrayList<String> harisExpected = new ArrayList<>();
        ArrayList<String> talhaExpected = new ArrayList<>();
        nickExpected.add("TFT2");
        nickExpected.add("90.0");
        nickExpected.add("GTA5");
        nickExpected.add("100.0");

        talhaExpected.add("Csgo");
        talhaExpected.add("10.0");

        System.out.println("Expected Tes1: " + nickExpected);
        System.out.println("Actual Tes1: " + nick.getUserGames());

        System.out.println("Expected Test2: "+ harisExpected);
        System.out.println("Actual Test2: " + haris.getUserGames());

        System.out.println("Expected Talha: "+ talhaExpected);
        System.out.println("Actual Talha: " + talha.getUserGames());

        assertEquals(nickExpected, nick.getUserGames());
        assertEquals(0, haris.getUserGames().size());
        assertEquals(talhaExpected, talha.getUserGames());


        System.out.println("\n");
        System.out.println(transaction2);
        Gift.execute(transaction2); // Nick Gifts GTA5 to Haris
        System.out.println("____________GIFTED_________________");
        nickExpected.remove("GTA5");
        nickExpected.remove("100.0");
        harisExpected.add("GTA5");
        harisExpected.add("100.0");

        System.out.println("Expected Tes1: "+ nickExpected);
        System.out.println("Actual Tes1: " + nick.getUserGames());

        System.out.println("Expected Test2: "+ harisExpected);
        System.out.println("Actual Test2: " + haris.getUserGames());

        System.out.println("Expected Talha: "+ talhaExpected);
        System.out.println("Actual Talha: " + talha.getUserGames());
        assertEquals(nickExpected, nick.getUserGames());
        assertEquals(harisExpected, haris.getUserGames());
        assertEquals(talhaExpected, talha.getUserGames());

        System.out.println("\n");

        // Admin gives TFT2 from Nick to Haris

        String transaction3 = "09 TFT2                      Tes1            Test2          ";
        System.out.println(transaction3);
        Gift.execute(transaction3); // Nick Gifts TFT2 to Haris
        System.out.println("____________GIFTED_________________");

        nickExpected.remove("TFT2");
        nickExpected.remove("90.0");
        harisExpected.add("TFT2");
        harisExpected.add("90.0");

        System.out.println("Expected Tes1: "+ nickExpected);
        System.out.println("Actual Tes1: " + nick.getUserGames());

        System.out.println("Expected Test2: "+ harisExpected);
        System.out.println("Actual Test2: " + haris.getUserGames());

        System.out.println("Expected Talha: "+ talhaExpected);
        System.out.println("Actual Talha: " + talha.getUserGames());
        assertEquals(0, nick.getUserGames().size());
        assertEquals(harisExpected, haris.getUserGames());
        assertEquals(talhaExpected, talha.getUserGames());

        System.out.println("\n");

        // Admin Takes tries to Take TFT2 from Nick, when he doesn't have it anymore

        String transaction4 = "09 TFT2                      Tes1            Talha          ";
        System.out.println(transaction4);
        
        Gift.execute(transaction4); // Nick Gifts TFT2 to Talha, without having TFT2
        talhaExpected.add("TFT2");
        talhaExpected.add("0.0");
        System.out.println("____________GIFTED_________________");

        System.out.println("Expected Tes1: "+ nickExpected);
        System.out.println("Actual Tes1: " + nick.getUserGames());

        System.out.println("Expected Test2: "+ harisExpected);
        System.out.println("Actual Test2: " + haris.getUserGames());

        System.out.println("Expected Talha: "+ talhaExpected);
        System.out.println("Actual Talha: " + talha.getUserGames());
        assertEquals(0, nick.getUserGames().size());
        assertEquals(harisExpected, haris.getUserGames());
        assertEquals(talhaExpected, talha.getUserGames());

        // Admin Gifts Csgo which he owns to himself.

        System.out.println("\n");

        String transaction5 = "09 Csgo                      Talha           Talha          ";
        System.out.println(transaction5);

//        Gift.execute(transaction5); // Nick Gifts TFT2 to Talha, without having TFT2
        System.out.println("____________GIFTED_________________");

        System.out.println("Expected Tes1: "+ nickExpected);
        System.out.println("Actual Tes1: " + nick.getUserGames());

        System.out.println("Expected Test2: "+ harisExpected);
        System.out.println("Actual Test2: " + haris.getUserGames());

        System.out.println("Expected Talha: "+ talhaExpected);
        System.out.println("Actual Talha: " + talha.getUserGames());
        assertEquals(0, nick.getUserGames().size());
        assertEquals(harisExpected, haris.getUserGames());
        assertEquals(talhaExpected, talha.getUserGames());

        // Admin Gifts Csgo away to nick

        System.out.println("\n");

        String transaction6 = "09 Csgo                      Talha           Tes1           ";
        System.out.println(transaction6);

        nickExpected.add("Csgo");
        nickExpected.add("10.0");
        talhaExpected.remove("Csgo");
        talhaExpected.remove("10.0");
        Gift.execute(transaction6);
        System.out.println("____________GIFTED_________________");

        System.out.println("Expected Tes1: "+ nickExpected);
        System.out.println("Actual Tes1: " + nick.getUserGames());

        System.out.println("Expected Test2: "+ harisExpected);
        System.out.println("Actual Test2: " + haris.getUserGames());

        System.out.println("Expected Talha: "+ talhaExpected);
        System.out.println("Actual Talha: " + talha.getUserGames());
        assertEquals(nickExpected, nick.getUserGames());
        assertEquals(harisExpected, haris.getUserGames());
        assertEquals(2, talha.getUserGames().size());

        System.out.println("\n");

        Delete.execute("02 Tes1            FS 003506.16" );
        Delete.execute("02 Test2           AA 006307.72" );

        talha.addUserGame("Csgo", "10.0");
        talha.removeUserGame("TFT2");
        UsersDatabase.updateGame(talha);
        TransactionsModifier.currentUser = null;

        System.out.println("User: " + talha.getUserName() + " has logged off!" + "\n");

    }

    @Test
    public void giftSellTest() {
        // Now let a buy/seller Nick to Log IN!
        // This test must only be run twice.
        TransactionsModifier.currentUser = nick;
        System.out.println("FS User: " + nick.getUserName() + " has logged in!" + "\n");
        GamesDatabase.main(new String[]{});

//        Talha,68220.0,AA,Csgo-10.0
//        Nick,3506.16,FS,TFT2-90.0-GTA5-100.0
//        Haris,6307.72,AA,
//        games.txt should be empty

        ArrayList<String> nickExpected = new ArrayList<>();
        ArrayList<String> harisExpected = new ArrayList<>();
        ArrayList<String> talhaExpected = new ArrayList<>();

        nickExpected.add("TFT2");
        nickExpected.add("90.0");
        nickExpected.add("GTA5");
        nickExpected.add("100.0");

        talhaExpected.add("Csgo");
        talhaExpected.add("10.0");

        System.out.println("Expected Tes1: " + nickExpected);
        System.out.println("Actual Tes1: " + nick.getUserGames());

        System.out.println("Expected Test2: "+ harisExpected);
        System.out.println("Actual Test2: " + haris.getUserGames());

        System.out.println("Expected Talha: "+ talhaExpected);
        System.out.println("Actual Talha: " + talha.getUserGames());
        assertEquals(nickExpected, nick.getUserGames());
        assertEquals(0, haris.getUserGames().size());
        assertEquals(talhaExpected, talha.getUserGames());

        System.out.println("\n");

        // Nick Puts a game "Baby" up for Sale, but doesnt own the game, and game is gifted.

        String transaction7 = "03 Baby                      Tes1            10.00 010.00";
        String transaction8 = "09 Baby                      Tes1            Test2          ";
        Sell.execute(transaction7);

        // Selling or Selling Today contains "Baby"
        boolean flag = userSellingGame(nick.getUserName(), "Baby");

        assertTrue(flag);


        if (GamesDatabase.sellToday.containsKey("Baby") && !GamesDatabase.selling.containsKey("Baby")) {
            Gift.execute(transaction8);
            System.out.println("____________GIFTED_________________");

            System.out.println("Expected Tes1: " + nickExpected);
            System.out.println("Actual Tes1: " + nick.getUserGames());

            System.out.println("Expected Test2: " + harisExpected);
            System.out.println("Actual Test2: " + haris.getUserGames());

            assertEquals(nickExpected, nick.getUserGames());
            assertEquals(harisExpected, haris.getUserGames());
        }
        else if (!GamesDatabase.sellToday.containsKey("Baby") && GamesDatabase.selling.containsKey("Baby")){
            Gift.execute(transaction8);

            harisExpected.add("Baby");
            harisExpected.add("10.0");

            boolean flag2 = GamesDatabase.selling.containsKey("Baby");
            assertTrue(flag2);

            System.out.println("____________GIFTED_________________");

            System.out.println("Expected Tes1: " + nickExpected);
            System.out.println("Actual Tes1: " + nick.getUserGames());

            System.out.println("Expected Test2: " + harisExpected);
            System.out.println("Actual Test2: " + haris.getUserGames());

            assertEquals(nickExpected, nick.getUserGames());
            assertEquals(harisExpected, haris.getUserGames());
        }
        GamesDatabase.removeGame(nick, "Baby");
        TransactionsModifier.currentUser = talha;
        if (UsersDatabase.Users.containsKey(haris.getUserName())){
            Delete.execute("02 Test2           AA 006307.72" );
        }
        TransactionsModifier.currentUser = null;
        System.out.println("\n");

    }

    @Test
    public void testAuctionSale(){
        TransactionsModifier.currentUser = talha;
        String transaction1 = "07 Talha           AA 068220.00";
        AuctionSale.execute(transaction1);
        System.out.println("Is Auction Sale on?");
        System.out.println("Expected: true");
        System.out.println("Actual: " + AuctionSale.auctionSale);
        assertTrue(AuctionSale.auctionSale);

        AuctionSale.execute(transaction1);
        System.out.println("Is Auction Sale on?");
        System.out.println("Expected: false");
        System.out.println("Actual: " + AuctionSale.auctionSale);
        assertFalse(AuctionSale.auctionSale);

        TransactionsModifier.currentUser = nick;
        String transaction2 = "07 Tes1            FS 003516.16";
        AuctionSale.execute(transaction2);
        System.out.println("Is Auction Sale on?");
        System.out.println("Expected: false");
        System.out.println("Actual: " + AuctionSale.auctionSale);
        assertFalse(AuctionSale.auctionSale);

        TransactionsModifier.currentUser = null;
    }

    @Test
    public void testBuy(){

        // As a seller put some games up for selling so the Buyer can purchase. (Fortnite 20 DD 10)
        // (Warface 30) (COD 50 DD 20) Minecraft (999.99 DD 50)

//        03 Fortnite                  Tes1            10.00 020.00
//        03 Warface                   Tes1            30.00 030.00
//        03 COD                       Tes1            20.00 050.00
//        03 Minecraft                 Tes1            50.00 999.99

        // Login Seller/Buyer Nick
        TransactionsModifier.currentUser = nick;
        System.out.println("User: " + michael.getUserName() + " has logged in!" + "\n");

        String transactionSell =  "03 Fortnite                  Tes1            10.00 020.00";
        String transactionSell1 = "03 Warface                   Tes1            30.00 030.00";
        String transactionSell2 = "03 COD                       Tes1            20.00 050.00";
        String transactionSell3 = "03 Minecraft                 Tes1            50.00 999.99";
        Sell.execute(transactionSell);
        Sell.execute(transactionSell1);
        Sell.execute(transactionSell2);
        Sell.execute(transactionSell3);

        TransactionsModifier.currentUser = null;
        System.out.println("User: " + nick.getUserName() + " has logged off!" + "\n");

        // As a seller I want to purchase a game from another seller (Should Not Be allowed, therefore
        // this shouldn't do anything)

        // Login Seller Micheal
        TransactionsModifier.currentUser = michael;
        System.out.println("User: " + michael.getUserName() + " has logged in!" + "\n");

        double previousBalance0 = michael.getUserBalance();
        String buyTransaction = "04 Fortnite                  Tes1            Test3          ";
        String gameName = buyTransaction.substring(3, 28).trim();
        String sellerName = buyTransaction.substring(29, 44).trim();
        String buyerName = buyTransaction.substring(45).trim();


        System.out.println("____________BEFORE_________________");
        System.out.println("Game Owning Expected: " + false + "\n");
        System.out.println("Game Owning by User Exists: " + userOwnsGame(buyerName, gameName) + "\n");
        assertFalse(userOwnsGame(buyerName, gameName));

        System.out.println(buyTransaction);
        System.out.println("____________AFTER_________________");
        Buy.execute(buyTransaction);
        System.out.println("Game Owning Expected: " + false + "\n");
        System.out.println("Game Owning by User Exists: " + userOwnsGame(buyerName, gameName) + "\n");
        assertFalse(userOwnsGame(buyerName, gameName));
        assertEquals(nick.getUserName(), sellerName);
        assertEquals(previousBalance0, michael.getUserBalance());


        // As a buyer I want to purchase a game that a seller isn't selling. (Should Not Be allowed, therefore
        // this shouldn't do anything)

        // Login Seller Buyer
        TransactionsModifier.currentUser = null;
        System.out.println("User: " + michael.getUserName() + " has logged out!" + "\n");

        TransactionsModifier.currentUser = buyer;
        System.out.println("User: " + buyer.getUserName() + " has logged in!" + "\n");
        double previousBalance1 = buyer.getUserBalance();

        String buyTransaction1 = "04 Manhunter3                Tes1            Test4          ";
        String gameName1 = buyTransaction1.substring(3, 28).trim();
        String sellerName1 = buyTransaction1.substring(29, 44).trim();
        String buyerName1 = buyTransaction1.substring(45).trim();


        System.out.println("____________BEFORE_________________");
        System.out.println("Game Owning Expected: " + false + "\n");
        System.out.println("Game Owning by User Exists: " + userOwnsGame(buyerName1, gameName1) + "\n");
        assertFalse(userOwnsGame(buyerName1, gameName1));

        System.out.println(buyTransaction1);
        System.out.println("____________AFTER_________________");
        Buy.execute(buyTransaction1);
        System.out.println("Game Owning Expected: " + false + "\n");
        System.out.println("Game Owning by User Exists: " + userOwnsGame(buyerName1, gameName1) + "\n");
        assertFalse(userOwnsGame(buyerName1, gameName1));
        assertEquals(previousBalance1, buyer.getUserBalance());


        // As a buyer I want to buy a game that I can't afford. (Should Not Be allowed, therefore
        // this shouldn't do anything)

        double previousBalance = buyer.getUserBalance();
        buyer.addUserBalance(-(previousBalance)); // Make Buyer Broke

        String buyTransaction2 = "04 Minecraft                 Tes1            Test4          ";
        String gameName2 = buyTransaction2.substring(3, 28).trim();
        String sellerName2 = buyTransaction2.substring(29, 44).trim();
        String buyerName2 = buyTransaction2.substring(45).trim();


        System.out.println("____________BEFORE_________________");
        System.out.println("Game Owning Expected: " + false + "\n");
        System.out.println("Game Owning by User Exists: " + userOwnsGame(buyerName2, gameName2) + "\n");
        assertFalse(userOwnsGame(buyerName2, gameName2));

        System.out.println(buyTransaction2);
        System.out.println("____________AFTER_________________");
        Buy.execute(buyTransaction2);
        System.out.println("Game Owning Expected: " + false + "\n");
        System.out.println("Game Owning by User Exists: " + userOwnsGame(buyerName2, gameName2) + "\n");
        assertFalse(userOwnsGame(buyerName2, gameName2));
        buyer.addUserBalance(previousBalance); // Flush Buyer with Cash
        assertEquals(previousBalance, buyer.getUserBalance());

        // As a buyer I want to buy a game from a seller that doesn't exist. (Should Not Be allowed, therefore
        // this shouldn't do anything)

        double previousBalance2 = buyer.getUserBalance();

        String buyTransaction3 = "04 Minecraft                 Tes2             Test4          ";
        String gameName3 = buyTransaction3.substring(3, 28).trim();
        String sellerName3 = buyTransaction3.substring(29, 44).trim();
        String buyerName3 = buyTransaction3.substring(45).trim();
        System.out.println(sellerName3);

        System.out.println("____________BEFORE_________________");
        System.out.println("Game Owning Expected: " + false + "\n");
        System.out.println("Game Owning by User Exists: " + userOwnsGame(buyerName3, gameName3) + "\n");
        assertFalse(userOwnsGame(buyerName3, gameName3));

        System.out.println(buyTransaction3);
        System.out.println("____________AFTER_________________");
        Buy.execute(buyTransaction3);
        System.out.println("Game Owning Expected: " + false + "\n");
        System.out.println("Game Owning by User Exists: " + userOwnsGame(buyerName3, gameName3) + "\n");
        assertFalse(userOwnsGame(buyerName3, gameName3));
        assertEquals(previousBalance2, buyer.getUserBalance());
        assertFalse(UsersDatabase.Users.containsKey(sellerName3));

        // As a buyer I want to purchase a game that a seller is selling. (Should Be allowed, therefore
        // this should add the game to the buyers user games and inventory and should decrement the user's
        // balance by the game's price.)

        buyer.addUserBalance(100.0);
        double expectedBalance = buyer.getUserBalance() - 50.0;
        String buyTransaction4 = "04 COD                       Tes1            Test4          ";
        String gameName4 = buyTransaction4.substring(3, 28).trim();
        String sellerName4 = buyTransaction4.substring(29, 44).trim();
        String buyerName4 = buyTransaction4.substring(45).trim();

        double expectedBalanceSeller = nick.getUserBalance() + 50.0;

        System.out.println(nick.getUserBalance());
        System.out.println(buyer.getUserBalance());
        
        System.out.println("____________BEFORE_________________");
        System.out.println("Game Owning Expected: " + false + "\n");
        System.out.println("Game Owning by User Exists: " + userOwnsGame(buyerName4, gameName4) + "\n");
        assertFalse(userOwnsGame(buyerName4, gameName4));

        System.out.println(buyTransaction4);
        System.out.println("____________AFTER_________________");
        Buy.execute(buyTransaction4);
        System.out.println("Game Owning Expected: " + true + "\n");
        System.out.println("Game Owning by User Exists: " + userOwnsGame(buyerName4, gameName4) + "\n");
        assertEquals(nick.getUserName(), sellerName4);
        assertTrue(userOwnsGame(buyerName4, gameName4));

        assertEquals(expectedBalance, buyer.getUserBalance());
        assertEquals(expectedBalanceSeller, nick.getUserBalance());
        buyer.addUserBalance(-50.0); // Flush Buyer with Cash


        // As a buyer I want to purchase a game that I have already Purchased. (Shouldn't Be allowed, therefore
        // it should not do anything. Constraint Error)

        double previousBalance4 = buyer.getUserBalance();
        System.out.println("____________BEFORE_________________");
        System.out.println("Game Owning Expected: " + true + "\n");
        System.out.println("Game Owning by User Exists: " + userOwnsGame(buyerName4, gameName4) + "\n");
        assertTrue(userOwnsGame(buyerName4, gameName4));

        System.out.println(buyTransaction4);
        System.out.println("____________AFTER_________________");
        Buy.execute(buyTransaction4);
        System.out.println("Game Owning Expected: " + true + "\n");
        System.out.println("Game Owning by User Exists: " + userOwnsGame(buyerName4, gameName4) + "\n");

        assertTrue(userOwnsGame(buyerName4, gameName4));
        assertEquals(previousBalance4, buyer.getUserBalance());

        // Turn AuctionSale On as Talha (Admin)

        // Login Seller Admin
        TransactionsModifier.currentUser = talha;
        System.out.println("User: " + talha.getUserName() + " has logged in!" + "\n");

        boolean wasAuctionSaleOn = false;
        AuctionSale.turnSale();
        if (!AuctionSale.auctionSale){
            AuctionSale.auctionSale = true;
        }
        else{
            wasAuctionSaleOn = true;
        }
        assertTrue(AuctionSale.auctionSale);

        // As a buyer I want to purchase a different game that a seller is selling with auctionSale ON.
        // (Should Be allowed, therefore this should add the game to the buyers user games and inventory
        // and should decrement the user's balance by the game's price with the discount applied.)

        TransactionsModifier.currentUser = buyer;

        buyer.addUserBalance(100.0); // Flush Buyer with Cash
        double expectedBalance1 = buyer.getUserBalance() - (30.0 - 9.0);

        String buyTransaction5 = "04 Warface                   Tes1            Test4          ";
        String gameName5 = buyTransaction5.substring(3, 28).trim();
        String sellerName5 = buyTransaction5.substring(29, 44).trim();
        String buyerName5 = buyTransaction5.substring(45).trim();
        System.out.println(buyerName5);

        System.out.println("____________BEFORE_________________");
        System.out.println("Game Owning Expected: " + false + "\n");
        System.out.println("Game Owning by User Exists: " + userOwnsGame(buyerName5, gameName5) + "\n");
        assertFalse(userOwnsGame(buyerName5, gameName5));

        System.out.println(buyTransaction5);
        System.out.println("____________AFTER_________________");
        Buy.execute(buyTransaction5);
        System.out.println("Game Owning Expected: " + true + "\n");
        System.out.println("Game Owning by User Exists: " + userOwnsGame(buyerName5, gameName5) + "\n");

        assertEquals(nick.getUserName(), sellerName5);
        assertTrue(userOwnsGame(buyerName5, gameName5));

        assertEquals(expectedBalance1, buyer.getUserBalance());

        buyer.addUserBalance((30.0 - 9.0)); // Give a Refund of Cash


        buyer.addUserBalance(-(100.0)); // Unflush Buyer of Cash

        // Turn AuctionSale OFF as Talha (Admin)

        TransactionsModifier.currentUser = talha;
        AuctionSale.turnSale();
        if (!wasAuctionSaleOn){
            AuctionSale.auctionSale = false;
        }

        //Delete Users from file
        GamesDatabase.removeGame(nick, "Fortnite");
        GamesDatabase.removeGame(nick, "Warface");
        GamesDatabase.removeGame(nick, "COD");
        GamesDatabase.removeGame(nick, "Minecraft");

        UsersDatabase.removeUser(nick.getUserName());
        UsersDatabase.removeUser(buyer.getUserName());
        // Current Set to null
        TransactionsModifier.currentUser = null;
        System.out.println("User: " + buyer.getUserName() + " has logged out!" + "\n");




    }

//
//    @Test
//    public void test {
//
//
//    }


}
