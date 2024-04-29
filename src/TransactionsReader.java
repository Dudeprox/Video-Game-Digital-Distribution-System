import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

/**
 * TransactionsReader class reads the file where all the transactions have been inputted after which it updates the
 * static variable "transactions" which helps the backend run all the necessary transactions
 */
public class TransactionsReader {

    private static String fileName;
    private static File dailyTransactionFile;
    public static ArrayList<String> transactions = new ArrayList<>();

    /**
     * Opens the file named fileName if the file exists otherwise it creates a new empty file.
     */
    public static void fileOpener() {
        if(!(dailyTransactionFile = new File(fileName)).exists()){
            try{
                dailyTransactionFile.createNewFile();
            } catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    /**
     * Changes the static variable "fileName" after which it reads the file and inputs all the transactions into a
     * public static variable "transactions"
     *
     * @param fileName the name of the file that contains all the transactions
     */
    public static void main(String fileName) {
        TransactionsReader.fileName = fileName;
        fileOpener();
        try {
            RandomAccessFile reader = new RandomAccessFile(fileName, "rw");

            String line = reader.readLine();
            while (line != null) {
                transactions.add(line.trim());
                line = reader.readLine();
            }
            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
