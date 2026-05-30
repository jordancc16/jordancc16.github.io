// Class: Card
// Purpose: Defines one standard playing card
// @author TAS
// @version November 2025
public class Card {
	public static final String[] SUITS = { "CLUBS", "HEARTS", "DIAMONDS", "SPADES" };
	public static final String[] FACES = { "NOT USED", "ACE", "TWO", "THREE", "FOUR",
			"FIVE", "SIX", "SEVEN", "EIGHT", "NINE", "TEN", "JACK", "QUEEN", "KING" };

	// instance variables
	private String suit;
	private int face;

	// constructor
	public Card(String s, int f) {
		suit = s;
		face = f;
	}

	// modifiers
	public void setFace(int f) {
		face = f;
	}

	public void setSuit(String s) {
		suit = s;
	}

	public String getSuit() {
		return suit;
	}

	public int getFace() {
		return face;
	}

	// take in a boolean variable to determine whether an ace will have the value of
	// 14 or 1.
	public int getValue(boolean lowHighAce) {
		if (getFace() == 1) // ACE
			if (lowHighAce == true) {
				return 14;
			} else {
				return 1;
			}

		else // for all others, point value is just the index into the FACES array
			return getFace();
	}

	// toString method that returns the face, suit, and value of the card
	public String toString() {
		return FACES[face] + " of " + suit + " (" + getValue(true) + ")";
	}

	// different toString method that takes in a boolean variable as a parameter
	// that takes into account the two possible values of aces.
	public String toString(boolean lowHighAce) {
		if (lowHighAce == false && getFace() == 1) {
			return FACES[face] + " of " + suit + " (" + getValue(false) + ")";
		} else if (lowHighAce == true && getFace() == 1) {
			return FACES[face] + " of " + suit + " (" + getValue(true) + ")";
		} else {
			return FACES[face] + " of " + suit + " (" + getValue(true) + ")";
		}
	}
}