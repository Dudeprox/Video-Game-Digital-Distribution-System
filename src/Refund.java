/**
 * Refund class implements the transaction interface where it refunds credit from an existing non-new buyer to an
 * existing non-new seller.
 */
public class Refund implements Transaction{

    /**
     * Checks all the fatal and constraint errors present (or not present) in the given refund transaction
     * @param transaction the refund string transaction in daily.txt
     */
    public static void execute(String transaction){
        // Checks whether the transaction format is valid
        if(!TransactionsModifier.Checker(transaction, "^(05)\\s(.){15}\\s(.){15}\\s\\d{6}(\\.)\\d{2}$")) {
            System.out.println("FATAL ERROR: Invalid Transaction!");
            return;
        }

        // Checks that a user has logged in!
        if(TransactionsModifier.currentUser == null){
            System.out.println("FATAL ERROR: Please Log in!");
            return;
        }
        
        //Checks to make sure that the logged in user is an admin
        if(!TransactionsModifier.currentUser.getUserType().equals(User.ADMIN)){
            System.out.println("CONSTRAINT ERROR: Privileged Transaction!");
            return;
        }
        String buyer = transaction.substring(3, 18).trim();
        String seller = transaction.substring(19, 34).trim();
        double credit = Double.parseDouble(transaction.substring(35).trim());

        // Checks to make sure that buyer and seller are both current users (not new)
        if(!UsersDatabase.Users.containsKey(buyer) || !UsersDatabase.Users.containsKey(seller)){
            System.out.println("CONSTRAINT ERROR: Buyer or Seller is not a current User!");
            return;
        }

        // Checks to make sure that the buyer and seller are two different users
        if(buyer.equals(seller)){
            System.out.println("CONSTRAINT ERROR: You cannot refund a user to themselves!");
            return;
        }
        User sellerUser = UsersDatabase.Users.get(seller);
        User buyerUser = UsersDatabase.Users.get(buyer);

        // Checks that a buyer is anything but a seller and a seller is anything but a buyer and runs the helper
        if(!buyerUser.getUserType().equals(User.SELLER) && !sellerUser.getUserType().equals(User.BUYER)) {
            refund(buyerUser, sellerUser, credit);
        }
        else{
            System.out.println("CONSTRAINT ERROR: Buyer is a Seller or Seller is a Buyer!");
        }
    }

    /**
     * A helper method that checks whether the seller has enough balance to refund and they buyers account won't exceed
     * the max account balance after adding the refund. It then makes all the changes to the file and database
     * accordingly.
     *
     * @param buyer an existing non-new buyer
     * @param seller an existing non-new seller
     * @param credit the credit needed to refund from buyer to seller
     */
    public static void refund(User buyer, User seller, double credit){
        // Checks to make sure that the seller has enough balance to proceed with the refund
        if(!(credit <= seller.getUserBalance())){
            System.out.println("CONSTRAINT ERROR: Seller does not have enough balance!");
            return;
        }

        // Checks to make sure that the buyer's account balance will not exceed max account balance possible after the
        // refund otherwise an error is printed and it then makes all the changes to the file and database
        // accordingly.
        if(buyer.getUserBalance() + credit <= AddCredit.MAXACCOUNTBALANCE){
            UsersDatabase.Users.get(seller.getUserName()).addUserBalance(-(credit));
            UsersDatabase.Users.get(buyer.getUserName()).addUserBalance(credit);
            UsersDatabase.updateCredits(buyer.getUserName(), seller.getUserName());
            System.out.println("Refund completed!");
        }
        else{
            System.out.println("CONSTRAINT ERROR: Buyer has maxed balance!");
        }
    }


}
