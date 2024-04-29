# Features

##Operation of Program:

**NOTE: Read before program execution.**

**Opening Program Folder:**

Firstly, when opening the project in an IDE, open the project from the **src** folder.
This is due to creation, rewriting and reading of backend database files.

**Program Execution with daily.txt or front end input files:**

The front end input file also referred to as daily files must be present in **src** folder when
executing the backend program.

**Main Program Execution (ClientMain.java)**

When having a specific daily file present in the **src** folder, the program is executed through
the running ClintMain.main in Client.java.

Subsequently, to the execution of ClintMain.main, the program requires the daily file name 
in the console, in case of the daily file of being named differently to **daily.txt**.


##Files and Formats:

**users.txt:**

This file represents the users' database. Any changes to the users such as creation,
deletion, crediting, changes to game inventory are saved through this file. Any reading
about the users' such as type, balance, name, user games is also done from 
this file. Hence, this file acts as the backend database for existing users.

In the beginning of every program execution the backend loads the data into program data such 
as attributes for each user from *users.txt*.

The data is stored in the file in a format of:

USERNAME,BALANCE,USERTYPE,GAMENAME-PRICE-GAMENAME2-PRICE2

An example of this format is given below:

Nick,3506.16,FS,TFT2-90.0-GTA5-100.0-CSGO-30.0

In this example the username is *Nick*, the account balance is
*3506.16*, the user type is *FS*, and the game inventory is 
*TFT2-90.0-GTA5-100.0*. Notice each game name and price is split by
"-", so the game *TFT2* has price *90.0*, the game *GTA5* has price 
*100.0*, and the game *CSGO* has price *30.0*.

**NOTE:** The example contains three games in the user's inventory, and the format contain 
two games for user, so a user can must unlimited games through this continued form of 
game inventory format.

**games.txt:**

This file represents the market database. Any game put for sale by a to seller user
is added to this file. It contains the information of each game sold by a seller, that
information includes; the game name, price of the game, the seller user selling the game, and
the game discount percent in case of the activation of an auction sale. Hence, 
this file acts as the backend database for selling games by seller users.

Once games are put up for sale they are stored in this file and "selling today" attribute,
which keeps track of games sold on the current day. Once the program re-runs (new day), 
the attribute "selling" is updated from this file, but "selling today" isn't updated. Hence,
the 'selling' database in the backend program is updated using this file, every time the 
program runs.

The data is stored in the file in a format of:

GAMENAME,PRICE,SELLER,DISCOUNT

An example of this format is given below:

Minecraft,999.99,Tes1,50.0

In this example the game name of the game being sold is *Minecraft*, the price of the game
is *999.99*, the seller name of a pre-existing user is *Tes1*, and the discount percentage is
*50.0*.

**NOTE:** Directly adding a game being sold in this file of valid credentials and format will be
considered as the game **not** sold on the current day.

**auctionsale.txt:**

This file represents the "Sale" sign for the backend's database. This file
either only contain the string "true" or the string "false". Any time the auction sale is
activated the string "true" is written in the file, and when the auction sale is deactivated the
string "false" is written in the file. Every time the program runs an attribute indicating the state of
the auction sale is updated according to the string written in the file. This is because the activation
of an auction sale can span multiple days.

The activation of the auction sale applies discounts to game when bought, otherwise they are sold at
regular price. Hence, this file acts as the for "Sale" sign for the program.

The data is stored in the file in a format of:

STRINGBOOLEAN

An example of this format is given below:

true

In this example only 'true' is written to the file which shows the auction sale to be active 
for the oncoming days of program execution.

**NOTE:** Manually writing true or false in the file before program runs will update the attribute for 
auction sale accordingly once the program is executed.


##JUnit Tests:

**NOTE: Read this before executing TestSuite.java**

**Assumptions:**

An admin user must pre-existing with the users' database, so before executing 
the test suite the following admin user must be written in **users.txt**.

*Admin User*

Talha,68220.0,AA,Csgo-10.0

**Execution of TestSuite.java:**

To effectively test the program via *TestSuite.java*, the test suite must run an
even number of times. This is due to games being sold and added to *games.txt* in some
tests involving more that just the current day. 

**NOTE:** Running the test suite an odd number of times may leave games or users
database affected by tests preformed in TestSuite.java. In order to prevent this, 
running the tests even number of times leaves the database unaffected.

##Design:


**Interface Inheritance**

The file *Transaction.java* represents an interface for a general
transaction. This interface is implemented by each specific transaction
in the program backend.

The transaction interface contains an *execute()* method used to preform
the necessary steps required to execute each transaction. Hence, the program
using Interface Inheritance.

The decision to use Interface Inheritance in the program backend was established
due to the similarity for a general transaction. To expand on this, we realized the
fundamental nature of a transaction is to execute a number of operations in the backend
of this program. So each transaction required a general execution method, which 
needed to be implemented in each district transaction. This required the use of
inheritance, but since each transaction carries out operations in the backend
of the program, we implemented Interface Inheritance.

## Unique Features:

* Executing Transactions

When executing the login transaction, if the user attempting to log in has a username
that pre-exists in the users' database we disregard matching credentials such as 
user type or balance, and the user is successfully logged into the program.

When executing the logout transaction, if the user attempting to log out has a username
that pre-exists in the users' database and that specific pre-existing user is 
logged in, we disregard matching credentials such as user type or balance, 
and the user is successfully logged out of the program.

However, transactions other than log in and log out, take credentials such as
balance, usertype, usernames, etc. in account when preforming the transaction.
If these credentials do not match ones pre-existing in the backend database, then the program
resolves this using constraint errors.

* Creating Users

When user is created on the current day by the appropriate user, it does not exist in the 
program database, but is written to the appropriate user's database file, so the created user
cannot be accessed on the current day it was initially created on. When running the program on the 
following day of the creation of a new user, the program data is updated through the appropriate 
user's database file such that the user can then be accessed in the program data. 
This is implemented due to the constraints of transactions, which involve preforming it on only the
users that were created on previous days or not created on the current day of execution.

* Current Day Database

In sell transaction we keep track of games being sold on the current day through the
use of a hashmap attribute called "sellingToday". This hashmap is updated while program
is running and when transactions are currently being executed. This hashmap does not
get updated in program start up from any pre-existing database, however the attribute
"selling" does. The attribute "selling" is updated from the database in a startup or 
different day of execution. This allows the program to keep track of which games we put
up for sale on the current day and on previous days.

In buy transaction we keep track of game purchased on the current day through the
use of a hashmap attribute called "purchasedToday". This hashmap is updated while program
is running and when transactions are currently being executed. This hashmap does not
get updated in program start up from any pre-existing database. This implementation
is due to constraints involving games purchased on current days of program execution
and game that were purchased before program execution.

* Auction Sale Implementation

The indication of an action sale is done by a static boolean attribute called 'auctionsale'. The
attribute's value being true indicates the auction sale is active, otherwise it indicates 
the auction sale is deactivated. Writing the value in the file *auctionsale.txt* saves
it, so the attribute can be updated for following days after the current day. So every in every
start up the value of the attribute 'auctionsale' is updated through reading of the file
*auctionsale.txt*.

**NOTE:** Initially the auction sale attribute is set to false, and the file *auctionsale.txt* remains
empty, until activated by the appropriate user.