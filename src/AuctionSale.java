import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * AuctionSale class implements the transaction interface where it starts a auction sale for the games that are being
 * sold, and it also turns the auction sale off.
 */
public class AuctionSale implements Transaction{

    private static String fileName = "auctionsale.txt";
    private static File auctionSaleFile;
    public static boolean auctionSale = false;

    /**
     * Opens the file named "auctionsale.txt" if the file exists otherwise it creates a new empty file.
     */
    public static void fileOpener() {
        if (!(auctionSaleFile = new File(fileName)).exists()) {
            try {
                auctionSaleFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * This helper method turns the sale on and off depending on the static variable "auctionSale".
     * Note: Turning the sale on and off involves writing to the file "auctionsale.txt". It always writes a false
     * if the file is empty.
     */
    public static void turnSale(){
        fileOpener();
        try {
            PrintWriter writer = new PrintWriter(auctionSaleFile);
            writer.print("");
            writer.close();
            RandomAccessFile reader = new RandomAccessFile(fileName, "rw");
            String toWrite = "";
            if (auctionSale) {
                toWrite += "false";
            }else{
                toWrite += "true";
            }
            reader.write(toWrite.getBytes(StandardCharsets.UTF_8));
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Reads the file "auctionsale.txt" and changes the static variable "auctionSale" depending on the true or false
     * value present in the file.
     *
     * @param args array of Strings
     */
    public static void main(String[] args){
        fileOpener();
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String line;
            if((line = br.readLine()) != null) {
                auctionSale = Boolean.parseBoolean(line);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks all the fatal and constraint errors present (or not present) in the given auction sale transaction and
     * turns the sale on and off according to what is present in the "auctionsale.txt" file and the current user must
     * be an admin.
     *
     * @param transaction the auction sale string transaction in daily.txt
     */
    public static void execute(String transaction){
        // Checks whether the transaction format is valid
        if(!TransactionsModifier.Checker(transaction, "^(07)\\s(.){15}\\s\\w{2}\\s\\d{6}(\\.)\\d{2}$")){
            System.out.println("FATAL ERROR: Invalid Transaction!");
            return;
        }

        // Checks that a user has logged in!
        if(TransactionsModifier.currentUser == null){
            System.out.println("FATAL ERROR: Please Log in!");
            return;
        }
        
        // Updates the static variable "auctionSale" depending on the file "auctionSale.txt"
        AuctionSale.main(new String[]{""});
        String admin = transaction.substring(3, 18).trim();
        String userType = transaction.substring(19, 21).trim();

        // Checks whether current user is admin and flips the auction sale on and off depending on the static variable
        // auctionSale.
        if(TransactionsModifier.currentUser.getUserType().equals(User.ADMIN) &&
                TransactionsModifier.currentUser.getUserName().equals(admin)){
            turnSale();
            if(auctionSale){
                auctionSale = false;
                System.out.println("Auction Sale Turned Off!");
            }else{
                auctionSale = true;
                System.out.println("Auction Sale Turned On!");
            }
        }else{
            System.out.println("CONSTRAINT ERROR: Non-Admins cannot flip Auction Sale!");
        }

        // Checks whether the transaction inputted has the proper user type
        if(!userType.equals(TransactionsModifier.currentUser.getUserType()) &&
                TransactionsModifier.currentUser.getUserName().equals(admin)){
            System.out.println("FATAL ERROR: Current User is an Admin instead of " + userType + "!");
        }

    }
}
