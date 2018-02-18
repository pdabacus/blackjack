// Where game of blackjack is played
package com.pavandayal.blackjack;

import java.util.List;
import java.util.ArrayList;

public class Table {
	private Dealer dealer;
	private List<Player> players;
	private int roundsPlayed;

	public Table(Dealer d) {
		dealer = d;
		players = new ArrayList<Player>();
		roundsPlayed = 0;
	}

	public String toString() {
		String s = "| ";
		for (Player p : players) {
			s += p + " | ";
		}
		return s.substring(0, s.length()-1);
	}

	public void addPlayer(Player p) {
		players.add(p);
	}

	public void removePlayer(Player p) {
		players.remove(p);
	}

	public void playRound(boolean showOutput) {
		dealer.resetHand();
		for (Player p : players) {
			p.beginHand();
			p.hit(dealer.deal());
		}
		dealer.hit();
		for (Player p : players) {
			p.hit(dealer.deal());
		}
		dealer.hitHidden();

		if (!dealer.hasBlackJack()) {
			for (Player p : players) {
				p.move(dealer);
				if (showOutput) {
					System.out.println();
					System.out.println(p);
				}
			}
		}
		dealer.move(showOutput);
		//System.out.println(">>>> " + dealer + players.get(0));

		int compareHand;
		int money;
		List<Player> removePlayers = new ArrayList<Player>();
		for (Player p : players) {
			for (Hand hand : p.getHands()) {
				compareHand = hand.compareTo(dealer.getHand());
				money = 0;
				if (hand.isBlackJack() && p.getHands().size() == 1) {
					money += hand.getBet()/2;
				}
				money += compareHand*hand.getBet();
				money *= (hand.isDoubleDown() ? 2 : 1);
				p.transferMoney(money, showOutput);

			}
			if (p.getBank() <= 0) {
				removePlayers.add(p);
			}
			p.resetHands();
		}
		for (Player p : removePlayers) {
			removePlayer(p);
		}
		roundsPlayed++;
	}

	public void playGame(boolean showOutput) {
		roundsPlayed = 0;
		while (players.size() > 0) {
			playRound(showOutput);
			if (showOutput) {
				System.out.println();
			}
			System.out.println("| Rounds played: " + roundsPlayed + " " + this);
		}
	}

}
