import java.util.Scanner;

public class InBetweenRunner {
    public static void main(String[] args) {
        // create and initialize a scanner
        Scanner kbd = new Scanner(System.in);

        // create an inbetween object
        InBetween game = new InBetween(kbd);

        // call the playGame method to play the game
        game.playGame();

        // close the scanner
        kbd.close();
    }
}
