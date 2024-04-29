import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * UsersDatabase class implements the transaction interface where it reads, adds and removes users from the file
 * "users.txt"
 */
public class UsersDatabase {

    private static String fileName = "users.txt";
    private static File UsersDataBase;
    public static HashMap<String, User> Users = new HashMap<>();

    /**
     * Opens the file named "users.txt" if the file exists otherwise it creates a new empty file.
     */
    public static void fileOpener() {
        if(!(UsersDataBase = new File(fileName)).exists()){
            try{
                UsersDataBase.createNewFile();
            } catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    /**
     * This helper method reads the "users.txt" file and updates the static variable "Users" which creates an entire
     * database of users for our backend
     * @param args an array of strings
     */
    public static void main(String args[]) {

        fileOpener();
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String line;
            while ((line = br.readLine()) != null) {
                ArrayList<String> userInfo = lineParser(line.trim());
                ArrayList<String> games;
                if (userInfo.get(3).equals("")) {
                    games = new ArrayList<>();
                }else {
                    String[] array = userInfo.get(3).split("-");
                    games = new ArrayList<>(Arrays.asList(array));
                }
                User user = new User(userInfo.get(0), Double.parseDouble(userInfo.get(1)),
                        userInfo.get(2), games);
                Users.put(userInfo.get(0), user);
                TransactionsModifier.dailyAdd.put(userInfo.get(0), 0.00);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Helper method that reads the line and puts all the info of a line into an array list of strings so that
     * the information is obtained from each line properly.
     * @param line the line that needs to be broken apart from the file
     * @return an array list of strings that contains the info found in the file
     */
    private static ArrayList<String> lineParser(String line){
        ArrayList<String> info = new ArrayList<>(3);
        int counter = 0;
        String information = "";
        for(int i = 0; i < line.length(); i++){
            if(line.charAt(i) == ','){
                info.add(counter, information);
                counter += 1;
                information = "";
            }else {
                information += line.charAt(i);
            }
        }
        info.add(counter, information);
        return info;
    }

    /**
     * Helper method that adds a line to the "users.txt" file when a new user is created based on the user's username,
     * userBalance, userType and the games the user owns
     * @param username the name of the new user
     * @param userBalance the balance of the new user
     * @param userType the type of the new user
     * @param games the inventory of games a user owns
     */
    public static void addUser(String username, double userBalance, String userType, String games){
        try {
            RandomAccessFile reader = new RandomAccessFile(fileName, "rw");
            String firstLine = reader.readLine();
            String toWrite;
            if (firstLine == null){
                toWrite = username + "," + userBalance + "," + userType + "," + games;
            }
            else{
                toWrite = "\n" + username + "," + userBalance + "," + userType + "," + games;
            }
            reader.seek(reader.length());
            reader.write(toWrite.getBytes(StandardCharsets.UTF_8));
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Helper method that reads the file and saves all the lines into an arraylist, then it turns the file into an 
     * empty file after which it writes all the lines back from the array list on to the file except for the line that 
     * contained the username of the user
     * @param username the name of the user
     */
    public static void removeUser(String username){
    	try {
    	    ArrayList<String> lines = new ArrayList<String>();

    	    RandomAccessFile reader = new RandomAccessFile(fileName, "rw");
    	    String line = reader.readLine();
    	    while(line != null){
    	        lines.add(line);
    	        line = reader.readLine();
            }
    	    reader.close();

            PrintWriter writer = new PrintWriter(UsersDataBase);
            writer.print("");
            writer.close();

            RandomAccessFile reader2 = new RandomAccessFile(fileName, "rw");
            int counter = 0;
    	    for(String line1: lines){
    	        line1 = line1.trim();
    	        String[] info = line1.split(",");
    	        String properLine = "";
    	        if(counter == 0 && !info[0].equals(username)){
                    reader2.write(line1.getBytes(StandardCharsets.UTF_8));
                    counter += 1;
                }
    	        else if(counter > 0 && !info[0].equals(username)){
    	            properLine = "\n" + line1;
                    reader2.write(properLine.getBytes(StandardCharsets.UTF_8));
                    counter += 1;
                }
            }
    	    reader2.close();

    	} catch (IOException e) {
    		e.printStackTrace();}
    }

    /**
     * Helper method that adds/removes credits to a user on the file by removing then adding the user back with the
     * new user balance
     * @param amount the new user balance that needs to be added/removed from the old balance
     * @param user the user who's balance is being updated
     */
    public static void addCredit(double amount, User user){
        removeUser(user.getUserName());
        addUser(user.getUserName(), amount, user.getUserType(), user.toStringUserGames());
    }

    /**
     * Helper method that adds/removes games for a user on the file by removing then adding the user back with the
     * new inventory of games the user owns
     * @param user the user who's inventory is being updated
     */
    public static void updateGame(User user){
        removeUser(user.getUserName());
        addUser(user.getUserName(), user.getUserBalance(), user.getUserType(), user.toStringUserGames());
    }

    /**
     * Helper method that updates the credits of a buyer and seller on the file when a buy transaction has gone 
     * through.
     * @param buyer_username the name of the buyer
     * @param seller_username the name of the seller
     */
    public static void updateCredits(String buyer_username, String seller_username) {
        User buyer = Users.get(buyer_username);
        User seller = Users.get(seller_username);
        addCredit(buyer.getUserBalance(), buyer);
        addCredit(seller.getUserBalance(), seller);
    }
}
