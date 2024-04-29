import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * GamesDatabase class implements the transaction interface where it reads, adds and removes games from the file
 * "games.txt"
 */
public class GamesDatabase {
    private static String fileName = "games.txt";
    private static File GamesDataBase;
    public static HashMap<String, Game> selling = new HashMap<>();
    public static HashMap<String, Game> sellToday = new HashMap<>();

    /**
     * Opens the file named "games.txt" if the file exists otherwise it creates a new empty file.
     */
    public static void fileOpener() {
        if(!(GamesDataBase = new File(fileName)).exists()){
            try{
                GamesDataBase.createNewFile();
            } catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    /**
     * This helper method reads the "games.txt" file and updates the static variable "selling" which creates a "shop"
     * for users to buy games from.
     * @param args an array of strings
     */
    public static void main(String[] args){
        fileOpener();
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String line;
            while ((line = br.readLine()) != null) {
                ArrayList<String> userInfo = lineParser(line.trim());
                Game game = new Game(userInfo.get(0), Double.parseDouble(userInfo.get(1)), userInfo.get(2),
                        Double.parseDouble(userInfo.get(3)));
                selling.put(userInfo.get(0), game);
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
            } else if(line.charAt(i) != ','){
                information += line.charAt(i);
            }
        }
        info.add(counter, information);
        return info;
    }

    /**
     * Helper method that adds a line to the "games.txt" file when a user sells the game
     * @param game the game being sold
     */
    public static void addGame(Game game){
        try {
            RandomAccessFile reader = new RandomAccessFile(fileName, "rw");
            String firstLine = reader.readLine();
            String toWrite = "";
            if(firstLine == null){
                toWrite = game.getName() + "," + game.getPrice() + "," + game.getOwner() + "," +
                        game.getAuctionSale();
            }
            else{
                toWrite = "\n" + game.getName() + "," + game.getPrice() + "," + game.getOwner() + "," +
                        game.getAuctionSale();
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
     * contained the game name and the owner's username
     * @param owner the user who owns the game being removed
     * @param game the game being removed from the "shop"
     */
    public static void removeGame(User owner, String game){
        try {
            ArrayList<String> lines = new ArrayList<String>();

            RandomAccessFile reader = new RandomAccessFile(fileName, "rw");
            String line = reader.readLine();
            while (line != null) {
                lines.add(line);
                line = reader.readLine();
            }
            reader.close();

            PrintWriter writer = new PrintWriter(fileName);
            writer.print("");
            writer.close();

            RandomAccessFile reader2 = new RandomAccessFile(fileName, "rw");
            int counter = 0;
            for (String line1 : lines) {
                line1 = line1.trim();
                String[] info = line1.split(",");
                String properLine = "";
                if (counter == 0 && !info[0].equals(game) && !info[2].equals(owner.getUserName())) {
                    reader2.write(line1.getBytes(StandardCharsets.UTF_8));
                    counter += 1;
                } else if (counter > 0 && !info[0].equals(game) && !info[2].equals(owner.getUserName())) {
                    properLine = "\n" + line1;
                    reader2.write(properLine.getBytes(StandardCharsets.UTF_8));
                    counter += 1;
                }
            }
            reader2.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
