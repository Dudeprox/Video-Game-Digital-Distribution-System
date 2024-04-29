import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * TransactionsModifier class implements all the transactions and runs the backend of the system.
 */
public class TransactionsModifier {

    private static ArrayList<String> transactions = TransactionsReader.transactions;
    public static User currentUser = null;
    private static boolean login = false;
    public static HashMap<String, Double> dailyAdd = new HashMap<>();

    /**
     * Loops over the static variable "transactions" from TransactionsReader class which contains all the transactions
     * in the daily transaction file after which it reads for the transaction code and performs the corresponding
     * transactions in the backend
     * @param args an array of strings
     */
    public static void main(String[] args){
        for(String transaction: transactions){
            String transactionCode = transaction.substring(0, 2);
            switch(transactionCode){
                case "00":
                    if(!login){
                        login = Login.execute(transaction);
                    }
                    else {
                        System.out.println("ERROR: Another User Already Logged in!");
                    }
                    break;
                case "01":
                    Create.execute(transaction.trim());
                    break;
                case "02":
                    Delete.execute(transaction.trim());
                    break;
                case "03":
                    Sell.execute(transaction.trim());
                    break;
                case "04":
                    Buy.execute(transaction);
                    break;
                case "05":
                    Refund.execute(transaction.trim());
                    break;
                case "06":
                    AddCredit.execute(transaction.trim());
                    break;
                case "07":
                    AuctionSale.execute(transaction.trim());
                    break;
                case "08":
                    RemoveGame.execute(transaction);
                    break;
                case "09":
                    Gift.execute(transaction);
                    break;
                case "10":
                    if(currentUser != null) {
                        login = !Logout.execute(transaction.trim());
                    }else{
                        System.out.println("ERROR: Please Log in!");
                    }
                    break;
                default:
                    System.out.println("FATAL ERROR: Invalid Transaction!");
                }
            }
        }

    /**
     * Checks that the transaction is a valid transaction from the pattern entered
     * @param transaction the transaction being checked for format
     * @param pattern the format the transaction should be in
     * @return true if the transaction matches the format
     */    
    public static boolean Checker(String transaction, String pattern){
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(transaction);
        return m.matches();
    }
}
