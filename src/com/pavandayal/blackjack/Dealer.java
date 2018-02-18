// Simulates abstract player with a set of cards, bet and, and strategy
package com.pavandayal.blackjack;

public class Dealer {
	private AutomaticStrategy strategy;
	private Shoe shoe;
	private Hand hand;
	private Card hidden;

	public Dealer(AutomaticStrategy st, Shoe sh) {
		strategy = st;
		shoe = sh;
		resetHand();
	}

	public void resetHand() {
		hand = new Hand(true);
		hidden = null;
		if (shoe.cardsLeft() < shoe.numCardsPerDeck()/2) {
			shoe.reset();
		}
	}

	public String toString() {
		return String.valueOf(hand);
	}

	public void hit() {
		hand.add(shoe.deal());
	}
	
	public void hitHidden() {
		hidden = shoe.deal();
	}

	public Card deal() {
		return shoe.deal();
	}
	
	public boolean hasBlackJack() {
		return (hidden.value == 1 && hand.get(0).value >= 10) || (hidden.value >= 10 && hand.get(0).value == 1);
	}

	public void move() {
		move(false);
	}

	public void move(boolean showMoves) {
		if (hidden != null) {
			hand.add(hidden);
			hidden = null;
		}
		Move m = strategy.move(hand, hand);
		if (showMoves) {
			System.out.println();
			System.out.println("dealer:");
			System.out.println(this);
		}
		while (!m.equals(Move.STAY)) {
			hand.add(deal());
			m = strategy.move(hand, hand);
			if (showMoves) {
				System.out.println(this);
				if (hand.handTotal() > 21) {
					System.out.println("BUST");
				}
			}
		}
	}

	public Hand getHand() {
		return hand;
	}

}
