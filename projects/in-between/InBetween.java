import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class InBetween {
    // instance variables
    private Deck deck;
    private Card[] board;

    private PlayerInfo player1;
    private PlayerInfo player2;
    private PlayerInfo currentPlayer;

    private int pot;
    private int rounds;
    private Scanner kbd;

    // True means ace card is high (14) while false means ace card is low (1).
    private static boolean lowHigh;

    public InBetween(Scanner kbd) {
        // use constructor to assign values
        deck = new Deck(1);
        board = new Card[3];

        player1 = new PlayerInfo("Player 1", 30, 0, 0, 0);
        player2 = new PlayerInfo("Player 2", 30, 0, 0, 0);
        currentPlayer = player1;
        pot = 0;
        rounds = 0;
        this.kbd = kbd;
    }

    // main method that keeps playing rounds until game ends
    public void playGame() {
        boolean keepPlaying = true;
        int maxBet;
        boolean valid = false;

        // welcome message and asks what game they want to play
        System.out.println("Hello! Welcome to In-Between!");
        System.out.println("To resume a saved game, enter the file name, or just press enter to start a new game: ");
        // set a string variable equal to the next line the user types
        String start = kbd.nextLine();
        // check if the user presses the enter key
        if (start.isEmpty()) {
            // start a new game
            System.out.println("Starting a new game...");
        } else {
            try {
                // read in a previous game from a text file
                loadGame(start);
            } catch (Exception e) {
                // If there's an problem with the file, inform the user and continue on with a
                // new game.
                System.out.println(
                        "There was an problem with the file: " + e.getMessage() + "\nStarting a new game instead...");
            }
        }

        // shuffle deck at the beginning of a new game
        if (rounds == 0) {
            deck.shuffle();
            System.out.println("*** Shuffling the deck ***\n");
        }

        // both players bet 3 chips
        player1.bet(3);
        player2.bet(3);
        // the 6 chips are added to the pot
        pot += 6;

        System.out.println("Added 3 chips from each player to the pot. Current pot: " + pot + "\n");
        // while the user wants to keep playing, play the game.
        while (keepPlaying) {
            // print the board with the first two cards
            printBoard();

            // set the maxBet variable to the pot or the player's amount of chips, depending
            // on which is more.
            if (currentPlayer.getChips() >= pot) {
                maxBet = pot;
            } else {
                maxBet = currentPlayer.getChips();
            }

            int bet = 0;
            valid = false;
            // while the input for the bet is invalid, keep asking for inputs until one is
            // valid.
            while (!valid) {
                // asks for a bet
                System.out.print(currentPlayer.getName() + " has " + currentPlayer.getChips()
                        + " chips, how much would you like to bet? Minimum is 1 and maximum is " + maxBet + ": ");
                // if the user types an integer, set the bet equal to that integer
                if (kbd.hasNextInt()) {
                    bet = kbd.nextInt();
                    if (bet >= 1 && bet <= maxBet) {
                        // bet has to be an amount between 1 and the maxBet to be valid.
                        valid = true;
                    }
                } else {
                    // clear the input if it is invalid (for example, not an integer).
                    kbd.next();
                }
            }

            // deal the third card
            dealThirdCard();

            // if third card is in between the two first cards, make the current player win
            // and update chips

            // wherever the parameter is true, the ace number will always be 14, because
            // only the first card (board[0]) dealt can be changed.
            if (board[1].getValue(true) > board[0].getValue(lowHigh)
                    && board[1].getValue(true) < board[2].getValue(true)
                    || board[1].getValue(true) > board[2].getValue(true)
                            && board[1].getValue(true) < board[0].getValue(lowHigh)) {
                // print board with all 3 cards
                printSecondBoard();
                // if third card is in between the first two cards, the current player wins.
                System.out.println(
                        "Middle card is in between! " + currentPlayer.getName() + " wins " + bet + " from the pot.");
                // add a win
                currentPlayer.addWin();
                // add chips to the player's amount
                currentPlayer.addChips(bet);
                // subtract chips from the pot
                pot -= bet;
                // check if third card is equal to first or second card
            } else if (board[1].getValue(true) == board[0].getValue(lowHigh)
                    || board[1].getValue(true) == board[2].getValue(true)) {
                // print board with all 3 cards
                printSecondBoard();
                // if the third card is equal to the first or second card, subtract their bet
                // and the pot from their chip amount.
                System.out.println("Middle card is not in between! " + currentPlayer.getName()
                        + " loses their bet and an additional amount equal to the amount in the pot.");
                // add a loss
                currentPlayer.addLoss();
                // subtract the player's bet and the amount in the pot
                currentPlayer.addChips(-bet - pot);
                // add the amount in the original pot to the pot
                pot += pot;
                // add the bet to the pot as well
                pot += bet;
                // check if the first two cards are sequential
            } else if (board[0].getValue(lowHigh) - 1 == board[2].getValue(true)
                    || board[0].getValue(lowHigh) + 1 == board[2].getValue(true)) {
                // don't print second board because there is no need to deal a third card
                System.out.println("Cards are sequential! " + currentPlayer.getName() + " loses bet and round ends.");
                // add a loss
                currentPlayer.addLoss();
                // subtract the player's bet
                currentPlayer.addChips(-bet);
                // add the bet to the pot
                pot += bet;
            } else {
                // print board with all 3 cards
                printSecondBoard();
                // if third card isn't in betweeen first and second card, the current players
                // loses.
                System.out.println("Middle card is not in between! " + currentPlayer.getName() + " loses their bet.");
                // add a loss
                currentPlayer.addLoss();
                // subtract the player's bet
                currentPlayer.addChips(-bet);
                // add the bet to the pot
                pot += bet;
            }

            // if a player runs out of chips, make them rebuy chips.
            if (currentPlayer.getChips() < 1) {
                System.out.println(currentPlayer.getName() + " is out of chips and must rebuy.");
                currentPlayer.rebuy();
            }

            // switch the current player to the other player
            endRound();
            // increment the number of rounds by 1
            rounds++;

            // if the pot is empty, ask if the player wants to continue, pause and save, or
            // quit.
            if (pot == 0) {
                System.out.print("Pot is empty! (c)ontinue, (p)ause, or (q)uit? ");
                // save their input in a variable called decision.
                String decision = kbd.next();
                if (decision.equals("c")) {
                    // if decision is c, end the round and display the amount of rounds and pot.
                    System.out.println("End of round " + rounds + ". Current pot: " + pot + "\n");
                    keepPlaying = true;
                    // both players put 3 chips into the pot to make the pot 6 again
                    player1.bet(3);
                    player2.bet(3);
                    pot += 6;

                    System.out.println("Added 3 chips from each player to the pot. Current pot: " + pot + "\n");
                }
                // if decision is p
                if (decision.equals("p")) {
                    // save game to text file
                    saveGame("ib.txt");
                    // stop playing the game
                    keepPlaying = false;
                }
                if (decision.equals("q")) {
                    // end the game and display final stats
                    endGame();
                    // stop playing the game
                    keepPlaying = false;
                }
            } else {
                // if pot isn't zero, still end the round.
                System.out.println("End of round " + rounds + ". Current pot: " + pot + "\n");
            }

            // Shuffle the deck every 5 rounds.
            if (rounds % 5 == 0) {
                deck.shuffle();
                System.out.println("*** Shuffling the deck ***\n");
            }
        }
    }

    // when a round ends, update stats and switch players
    private void endRound() {
        if (currentPlayer == player1) {
            currentPlayer = player2;
        } else {
            currentPlayer = player1;
        }
    }

    // print out a nicely formatted representation of the board
    public void printBoard() {
        // deal the first card
        board[0] = deck.dealCard();

        // Default value of an ace is 14.
        lowHigh = true;

        // if the card is an ace, update this variable based on whether or not the user
        // enters a valid input.
        boolean validInput = false;

        // Check if card is an ace.
        if (board[0].getValue(true) == 14) {
            // While the input isn't valid, keeping asking for a valid input.
            while (!validInput) {
                // ask if theuser wants a high or low ace
                System.out.println("First card is an ACE. Do you want it to be (l)ow or (h)igh? ");
                if (kbd.hasNext()) {
                    // set lowOrHigh equal to the string the user typed.
                    String lowOrHigh = kbd.next();
                    // if the input is either l or h, it can be used.
                    if (lowOrHigh.equals("l") || lowOrHigh.equals("h")) {
                        validInput = true;
                        if (lowOrHigh.equals("l")) {
                            // if lowOrHigh is "l", set lowHigh equal to false so that the ace is low (1).
                            lowHigh = false;
                        }
                    }
                } else {
                    // Clear invalid input and check the next one
                    kbd.next();
                }
            }
        }
        // deal the second card
        board[2] = deck.dealCard();
        // print out the board with the two cards
        System.out.println("Current Board: [" + board[0].toString(lowHigh) + ",     , "
                + board[2].toString() + "]");
    }

    public void dealThirdCard() {
        // third card is dealt in the second slot of the board
        board[1] = deck.dealCard();
    }

    public void printSecondBoard() {
        // print board after third card is dealt
        System.out.println("Current Board: [" + board[0].toString(lowHigh) + ", " + board[1].toString() + ", "
                + board[2].toString() + "]");
    }

    // save the current game state to a file
    public void saveGame(String fileName) {
        try {
            // create and initialize a PrintWriter
            PrintWriter out = new PrintWriter(new File(fileName));
            // save the number of rounds and both players' info.
            out.println(rounds);
            out.println(player1.toString());
            out.println(player2.toString());
            // close the PrintWriter
            out.close();

            // print saving statements
            System.out.println("Saving game to " + fileName + "...");
            System.out.println("Game saved successfully.");
        } catch (Exception e) {
            // print an error message if the file isn't found
            System.out.println("File not found: " + e.getMessage());
        }
    }

    // load a game state from a file
    public void loadGame(String fileName) throws Exception {
        // create and initialize a scanner to read the file
        Scanner fileScanner = new Scanner(new File(fileName));

        // set the amount of rounds equal to the first integer
        rounds = fileScanner.nextInt();
        // set the pot to zero
        pot = 0;

        // go to the next line in the text file
        fileScanner.nextLine();

        String playerOne = fileScanner.nextLine();
        // split the line by each comma and space
        String[] playerOneArray = playerOne.split(", ");
        // create and assign variables for the name, chips, rebuys, wins, and losses for
        // player1
        String name1 = playerOneArray[0];
        int chips1 = Integer.parseInt(playerOneArray[1]);
        int rebuys1 = Integer.parseInt(playerOneArray[2]);
        int wins1 = Integer.parseInt(playerOneArray[3]);
        int losses1 = Integer.parseInt(playerOneArray[4]);
        // adjust player1 based on the player info
        player1 = new PlayerInfo(name1, chips1, rebuys1, wins1, losses1);

        String playerTwo = fileScanner.nextLine();
        String[] playerTwoArray = playerTwo.split(", ");
        // split the line by each comma and space
        String name2 = playerTwoArray[0];
        // create and assign variables for the name, chips, rebuys, wins, and losses for
        // player2
        int chips2 = Integer.parseInt(playerTwoArray[1]);
        int rebuys2 = Integer.parseInt(playerTwoArray[2]);
        int wins2 = Integer.parseInt(playerTwoArray[3]);
        int losses2 = Integer.parseInt(playerTwoArray[4]);
        // adjust player2 based on the player info
        player2 = new PlayerInfo(name2, chips2, rebuys2, wins2, losses2);

        // if the number of rounds is even, player1 starts. if it's odd, player2 starts.
        if (rounds % 2 == 0) {
            currentPlayer = player1;
        } else if (rounds % 2 == 1) {
            currentPlayer = player2;
        }

        // When loading in the game from a text file, the number of rounds should be
        // substracted by 1
        // because I have rounds++ in the playGame method before I display which round
        // it is.
        rounds--;

        // print loading statements
        System.out.println("Game loaded successfully from " + fileName + ".");
        System.out.println("Resuming saved game from " + fileName + "...");

        // close the scanner
        fileScanner.close();
    }

    // display final stats and winner
    public void endGame() {
        System.out.println("Game Over!");
        // include win percentages
        System.out.println(player1.getName() + " has " + player1.getChips() + " chips with " + player1.getNumRebuys()
                + " rebuys and a win percentage of " + player1.getWinPercentage() + "%.");
        System.out.println(player2.getName() + " has " + player2.getChips() + " chips with " + player2.getNumRebuys()
                + " rebuys and a win percentage of " + player2.getWinPercentage() + "%.");
    }
}
