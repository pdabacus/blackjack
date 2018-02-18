// Simulates a multiple decks, each of 52 cards
package com.pavandayal.blackjack;

import java.util.Arrays;

public class Shoe {
	private final int decks;
	private final int[] values = {1,2,3,4,5,6,7,8,9,10,11,12,13};
	private final char[] suites = {'\u2660','\u2665','\u2663','\u2666'};
	private final Card[] shoeCards;
	private int[] shoeCounts;
	private int[] discardCounts;

	public Shoe(int d) {
		decks = d;
		int numCards = numCardsPerDeck();
		shoeCards = new Card[numCards];
		shoeCounts = new int[numCards];
		discardCounts = new int[numCards];
		int i = 0;
		for (char s : suites) {
			for (int v : values) {
				shoeCards[i] = new Card(v, s, cardTitle(v,s));
				i++;
			}
		}
		reset();
	}

	private String cardTitle(int v, char s) {
		String title;
		switch (v) {
			case 1:  title = "A"; break;
			case 11: title = "J"; break;
			case 12: title = "Q"; break;
			case 13: title = "K"; break;
			default: title = String.valueOf(v); break;
		}
		return title + String.valueOf(s);
	}

	private String spacedString(Object o, int n) {
		String objString = String.valueOf(o);
		String spaces = "";
		for (int i = objString.length(); i < n; i++) {
			spaces += " ";
		}
		return spaces + objString;
	}

	public String toString() {
		String s = "Card\t\tShoe\t\tDiscard\n";
		String tmp;
		for (int i = 0; i < numCardsPerDeck(); i++) {
			s += spacedString(shoeCards[i], 4) + "\t\t";
			s += spacedString(shoeCounts[i], 4) + "\t\t";
			s += spacedString(discardCounts[i], 4) + "\n";
		}
		return s.substring(0, s.length()-1);
	}

	public void reset() {
		Arrays.fill(shoeCounts, decks);
		Arrays.fill(discardCounts, 0);
	}

	public int numCardsPerDeck() {
		return values.length * suites.length;
	}

	private int random(int a, int b) {
		return a + (int) (Math.random()*(b-a+1));
	}

	public Card deal() {
		int r = random(1, cardsLeft());
		if (r < 1) return null;
		for (int i = 0; i < numCardsPerDeck(); i++) {
			r -= shoeCounts[i];
			if (r < 1) {
				shoeCounts[i]--;
				discardCounts[i]++;
				return shoeCards[i];
			}
		}
		System.err.println("Warning: No cards left in shoe to draw from: shuffling");
		reset();
		return deal();
	}

	public int cardsLeft() {
		int numCards = 0;
		for (int i = 0; i < numCardsPerDeck(); i++) {
			numCards += shoeCounts[i];
		}
		return numCards;
	}

	public int cardsDealt() {
		int numCards = 0;
		for (int i = 0; i < numCardsPerDeck(); i++) {
			numCards += discardCounts[i];
		}
		return numCards;
	}

}
