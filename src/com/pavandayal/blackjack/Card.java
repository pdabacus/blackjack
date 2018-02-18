// Simulates a single card with value and suite
package com.pavandayal.blackjack;

public class Card {
	public final int value;
	public final char suite;
	public final String title;

	public Card(int v) {
		value = v;
		suite = '\u2660';
		title = String.valueOf(v) + String.valueOf(suite);
	}

	public Card(int v, char s) {
		value = v;
		suite = s;
		title = String.valueOf(v) + String.valueOf(s);
	}

	public Card(int v, char s, String t) {
		value = v;
		suite = s;
		title = t;
	}

	public String toString() {
		return title;
	}

	public boolean equals(Card other) {
		return this.value == other.value || this.value > 9 && other.value > 9;
	}

}
