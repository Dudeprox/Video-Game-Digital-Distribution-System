# Requirements and Clarifications

**Note:** Any invalid transaction format will be considered a **FATAL ERROR**. All transactions must follow a 
specific pattern in the daily.txt file. With the assumption that all transactions are valid consider the 
following requirements and clarifications about the backend program.

**Login Transaction:**

1. A User attempting to log in must have their username present the backend's
database. If no such username exists within the current backend database then
the backend presents a **CONSTRAINT ERROR**, where the user is not logged in.

**Note:** If the user is attempting to log in with a username that exists in the 
current backend database, the user will be logged even if the remaining 
credentials of the user fail to match with the pre-existing credentials present 
in the current backend database.

**Logout Transaction:**

1. A user attempting to log out must have the same username as the 
current logged-in user, who is present the backend's database. If 
username fails to match with current logged-in user the backend presents a 
**CONSTRAINT ERROR**.

2. If the user attempting to log out has the same username as the current
logged-in user then the backend will appropriately log out the user.

**Note:** If the user is attempting to log out has a username that fails to match the
current logged-in user the backend will be unable to log the user out of the system.

**Create Transaction:**

1. A user is created with appropriate credentials if and only if the current 
logged-in user executing the transaction has usertype, Admin.
   
2. The user being created must not have the same username as a pre-existing user in the 
program database.

**Note:** If the current logged-in user, attempting to create a user is not an Admin, then the backend 
will be appropriately provided a **CONSTRAINT ERROR** with no further tasks being executed by the 
program backend.

**Delete Transaction:**

1. A user is deleted with appropriate credentials is created if and only if the current 
logged-in user executing the transaction is an Admin, and the user being deleted pre-exists
in the current backend database.

**Note:** If the current logged-in user attempting to delete a user, fails these conditions then the backend 
will be appropriately provided a **CONSTRAINT ERROR** with no further tasks being executed by the 
program backend.

**Sell Transaction:**

1. A user currently attempting to sell a game must be the current logged-in user of not type "Buyer", 
the user does not own the game in their inventory, and the game is not already being sold by the current
selling user. 

2. A game sold by a specific user is tracked accordingly to whether it was sold on the 
current day or sold on previous days. This allows buyers to only purchase games
which were put up for sale on previous days, not the current day the user is attempting to buy.

3. The current seller user cannot sell a specific game more than once, unless they remove the selling 
game first.

4. Many distinct sellers can the same specific game up for sale simultaneously.

**Note:** If the current logged-in user attempting to sell a game without the appropriate
requirements then the backend will be appropriately provided a **CONSTRAINT ERROR** 
with no further tasks being executed by the program backend.

**Buy Transaction:**

1. A user buying a game must the current logged-in user of not type "Seller",
the user does not own the game from a previous purchase, the buyer is buying from a 
seller which pre-exists in the backend database, the buyer and seller are not of same 
username, and the result balance of both the buyer and seller don't raise a constraint error.

2. A game sold by a specific user on the current day cannot be purchase by the
buying user on that same current day. The buyer is able to only purchase games which 
were put up for sale on previous days, not on the current day the user is attempting to buy.

3. The current buyer must not be selling the game that they are currently attempting to buy.

4. The current buyer user cannot purchase a specific game more than once even if the buyer
is purchasing the same game from another distinct seller.

5. The game purchased from the seller user by the buyer user will be sold to the
buyer on regular price if the auction sale isn't activated, otherwise the discount set by the
seller will be applied, and the game will be sold appropriately.

**Note:** If the current logged-in user attempting to buy a game without these appropriate
requirements then the backend will be appropriately provided a **CONSTRAINT ERROR** 
with no further tasks being executed by the program backend.

**Refund Transaction:**

1. The current user preforming the refund transaction must be an Admin.

2. The buyer and seller must be pre-existing users in the database. Similarly, this refund 
transaction cannot be preformed on users that are created on the current day on which 
the refund transaction is being preformed.

3. The seller and buyer that the refund transaction is being preformed on must be distinct users.

4. The seller user, that the refund transaction is being preformed on, must be any user type
but "Buyer" and the balance of the seller subsequently to the transaction must not violate 
constraints.

5. The buyer user, that the refund transaction is being preformed on, must be any user type
but "Seller" and the balance of the buyer subsequently to the transaction must not violate 
constraints.

**Note:** If the current logged-in user attempting to refund without these appropriate
requirements then the backend will be appropriately provided a **CONSTRAINT ERROR** 
with no further tasks being executed by the program backend.

**AddCredit Transaction:**

1. The current user must be logged in and can be of any user type to preform this transaction.

2. If the current user executing the add credit transaction is not of type Admin, then
the user being credited by the transaction must be the logged in or be the current user.

3. An Admin user can credit any pre-existing user in the database, including themselves.

4. The user that is being credited on the current day must not surpass their 
daily maximum credit limit of 1000 dollars. This is reset every new day, for each
   pre-exiting user in the database.

**Note:** If the current logged-in user attempting to addcredit without these appropriate
requirements then the backend will be appropriately provided a **CONSTRAINT ERROR** 
with no tasks would be executed by the program backend, such that the database remains 
unaffected.


**Auctionsale Transaction:**

1. The current user preforming this transaction must be logged in, and must be an Admin.

2. Activating the auction sale involves discounts of selling game to be applied appropriatelyy in a valid
buy transaction.

3. Once the auction sale is activated then the discounts must be applied on all following days of the 
current day when buyers are purchasing games until an Admin decides to deactivate the auction sale. 
In the case of deactivation, the discounts will not be applied to any games being purchase, 
until the auction sale is again reactivated.

**Note:** If the current logged-in user attempting to flip the auction sale without these appropriate
requirements then the backend will be appropriately provided a **CONSTRAINT ERROR** 
with no tasks would be executed by the program backend, such that the auction sale remains 
unaffected.

**Removegame Transaction:**

1. The current logged-in user can be of any user type in order to preform this transaction. 

**NOTE:** Since this transaction is semi-privileged then it will be executed differently based on the 
current user's user type.

2. If the current user is not an Admin, then the current user's username must match the 
owner's username in the given transaction. 

3. If the current user is a "Buyer" then they must own the game that is being removed.

4. If the current user is "Buyer/Seller", then either the user must own the game through a 
purchase, or they must be selling the game. If this current user owns the game through
a purchase then the game is removed from the game inventory. If this current user is selling the
game then through this remove transaction the game will no longer is sold by the user in the 
   backend database.

**NOTE:** The current user cannot be both owning and selling the same game simultaneously. 

5. If the current user is "Seller" then the game being removed must be currently sold by
the user in the database, otherwise a constraint error is presented by the backend.

**Note:** If the current logged-in user attempting to remove game without these appropriate
requirements then the backend will be appropriately provided a **CONSTRAINT ERROR** 
with no tasks would be executed by the program backend, such that the database remains 
unaffected.

**Gift Transaction:**

1. The current logged-in user can be any user type in order to preform this transaction. 
   
**Note:** Since this transaction is semi-privileged then it will be executed differently based on the 
current user's user type.

2. Both the gift giver and receiver must exist in the program backend database.

3. The gift giver must be distinct from the receiver.

4. The game being gifted from the gift giver to the receiver cannot be owned by the receiver, before
the transaction.

5. An Admin gift giver, can either own the game (not bought on the current day) or 
be selling game (not put on sale on the current day) for it to be gifted to the receiver. If these 
conditions aren't met by the admin gift giver then game can be created and gifted to the receiver.

6. A non-admin user must either own the game (not bought on the current day) or 
be selling game (not put on sale on the current day) for it to be gifted to the receiver. If these
conditions aren't met then the user cannot gift to receiver, and the backend provides a 
constraint error.

**Note:** If the current logged-in user attempting to gift a game without these appropriate
requirements then the backend will be appropriately provided a **CONSTRAINT ERRORS**
with no tasks would be executed by the program backend, such that the database remains 
unaffected.
