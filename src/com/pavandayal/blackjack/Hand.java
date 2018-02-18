// Simulates a a set of cards, the value of the set, and  bet attached
package com.pavandayal.blackjack;

import java.util.ArrayList;

public class Hand extends ArrayList<Card> implements Comparable {
	private final int bet;
	private final boolean winsBusts;
	private boolean doubleDown;

	public Hand(int b) {
		super();
		bet = b;
		winsBusts = false;
		doubleDown = false;
	}

	public Hand(boolean wins) {
		super();
		bet = 0;
		winsBusts = wins;
		doubleDown = false;
	}

	public int getBet() {
		return bet;
	}

	public void doubleDown() {
		doubleDown = true;
	}

	public boolean isDoubleDown() {
		return doubleDown;
	}

	public boolean isBlackJack() {
		return Math.abs(handTotal()) == 21 && size() == 2;
	}

	public boolean canSplit() {
		return size() == 2 && get(0).equals(get(1));
	}

	public int handTotal() {
		int aces = 0;
		int total = 0;
		int val;
		for (Card c : this) {
			val = c.value;
			if (val == 1) {
				aces++;
				total += 10;
			}
			if (val > 10) {
				total += 10 - val;
			}
			total += val;
		}

		while (total > 21 && aces > 0) {
			total -= 10;
			aces--;
		}

		if (aces > 0) {
			total *= -1;
		}

		return total;
	}

	public String toString() {
		String s = "";
		if (size() < 1) {
			return s;
		}
		s += "| ";
		for (int i = 0; i < size(); i++) {
			s += this.get(i) + " | ";
		}
		return s + "total: " + Math.abs(handTotal()) + " |";
	}

	public boolean equals(Hand other) {
		return compareTo(other) == 0;
	}

	public int compareTo(Object o) {
		Hand other = (Hand) o;
		boolean otherBlackJack = other.isBlackJack();
		if (isBlackJack()) {
			if (otherBlackJack) {
				return 0;
			} else {
				return 1;
			}
		} else {
			if (otherBlackJack) {
				return -1;
			}
		}

		int thisTotal = Math.abs(handTotal());
		int otherTotal = Math.abs(other.handTotal());
		if (thisTotal > 21) {
			if (otherTotal > 21) {
				return (other.winsBusts ? -1 : 1);
			} else {
				return -1;
			}
		} else {
			if (otherTotal > 21) {
				return 1;
			}
		}

		if (thisTotal < otherTotal) {
			return -1;
		} else if (thisTotal > otherTotal) {
			return 1;
		} else {
			return 0;
		}
	}

}
