// Simulates abstract player with a set of cards, bet and, and strategy
package com.pavandayal.blackjack;

import java.util.List;
import java.util.ArrayList;

public class Player {
	private Strategy strategy;
	private int bank;
	private int currentHand;
	private List<Hand> hands;

	public Player(Strategy s, int b) {
		strategy = s;
		bank = b;
		currentHand = 0;
		resetHands();
	}

	public void resetHands() {
		hands = new ArrayList<Hand>();
		currentHand = 0;
	}

	public void beginHand() {
		hands.add(new Hand(strategy.getBet(bank)));
	}

	public String toString() {
		String s = "bank: $" + bank + "\n";
		for (Hand h : hands) {
			s += String.valueOf(h) + "\n";
		}
		return s.substring(0, s.length()-1);
	}

	public boolean isSplit() {
		return hands.size() > 1;
	}

	public void hit(Card c) {
		hands.get(currentHand).add(c);
	}

	public void move(Dealer d) {
		Hand dealerHand = d.getHand();
		Hand h;
		while (currentHand < hands.size()) {
			h = hands.get(currentHand);
			switch (strategy.move(this, dealerHand).name()) {
				case "SPLIT":
					hands.add(currentHand+1, new Hand(h.getBet()));
					hands.get(currentHand+1).add(h.remove(1));
					break;
				case "DOUBLE":
					h.doubleDown();
					h.add(d.deal());
					break;
				case "HIT":
					h.add(d.deal());
					break;
				default:
					currentHand++;
			}
		}
	}

	public List<Hand> getHands() {
		return hands;
	}

	public Hand getCurrentHand() {
		return hands.get(currentHand);
	}

	public int getBank() {
		return bank;
	}

	public void transferMoney(int m) {
		transferMoney(m, false);
	}

	public void transferMoney(int m, boolean showOutput) {
		if (showOutput) {
			System.out.println();
			System.out.println("You " + (m<0?"lost":"won") + " $" + Math.abs(m));
		}
		bank += m;
	}

}
