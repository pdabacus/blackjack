// List of possible player moves
package com.pavandayal.blackjack;

public enum Move {
	DOUBLE(1),
	SPLIT(2),
	STAY(3),
	HIT(4);

	private int index;

	Move(int i) {
		index = i;
	}

	public int getIndex() {
		return index;
	}

	public static Move fromIndex(int i) {
		switch(i) {
			case 1: return DOUBLE;
			case 2: return SPLIT;
			case 3: return STAY;
			case 4: return HIT;
			default: return STAY;
		}
	}
}
