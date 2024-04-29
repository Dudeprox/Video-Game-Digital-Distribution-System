import java.io.File;
import java.util.Scanner;

/**
 * ClientMain class that runs the entire back end depending on the file name inputted.
 */
public class ClientMain {

    public static File transactionFile;

    /**
     * Runs a small front end asking for the daily transaction file after which it runs the entire back end
     * @param args an array of strings
     */
    public static void main(String[] args) {
        UsersDatabase.main(new String[]{""});
        System.out.printf("Please Enter Transaction File Name: ");
        Scanner obj = new Scanner(System.in);
        String input = obj.nextLine().trim();
        if (!(transactionFile = new File(input)).exists()) {
                System.out.println("ERROR: Please enter valid transaction file!");
        } else {
            TransactionsReader.main(input);
            GamesDatabase.main(new String[]{""});
            TransactionsModifier.main(new String[]{""});
        }

    }
}
