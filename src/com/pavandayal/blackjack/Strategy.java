// provide a move when given a situation
package com.pavandayal.blackjack;

import java.util.Scanner;

public class Strategy {

	public Move move(Player player, Hand dealer) {
		Hand playerHand = player.getCurrentHand();
		boolean splitable = playerHand.canSplit() && player.getBank() >= player.getHands().size()*playerHand.getBet();
		Scanner input = new Scanner(System.in);
		String response;

		System.out.println();
		System.out.println("dealer: " + dealer);
		System.out.println("you:    " + player);

		if (playerHand.isBlackJack()) {
			if (!player.isSplit()) {
				System.out.println("BLACKJACK");
			}
			System.out.println("<press enter to continue>");
			input.nextLine();
			return Move.STAY;
		}

		if (playerHand.handTotal() > 21) {
			System.out.println("BUST");
			System.out.println("<press enter to continue>");
			input.nextLine();
			return Move.STAY;
		}

		if (playerHand.isDoubleDown()) {
			System.out.println("STAY");
			System.out.println("<press enter to continue>");
			input.nextLine();
			return Move.STAY;
		}

		if (splitable) {
			System.out.println("Would you like to split? [y/n]");
			System.out.print(">> ");
			response = input.nextLine();
			if (response.length() > 0 && response.toLowerCase().charAt(0) == 'y') {
				System.out.println("SPLIT");
				return Move.SPLIT;
			}
		}

		System.out.println("[D]ouble, [H]it, or [S]tay?");
		System.out.print(">> ");
		response = input.nextLine();
		switch (response.length() == 0 ? 's' : response.toLowerCase().charAt(0)) {
			case 'd': System.out.println("DOUBLE"); return Move.DOUBLE;
			case 'h': System.out.println("HIT"); return Move.HIT;
			default: System.out.println("STAY"); return Move.STAY;
		}
	}

	public int getBet() {
		return 2;
	}

	public int getBet(int bank) {
		Scanner input = new Scanner(System.in);
		String response;
		System.out.println();
		System.out.println("How much to bet? (between $1 and $" + bank + ")");
		int bet = 0;
		System.out.print(">> ");
		response = input.nextLine();
		try {
			bet = Integer.parseInt(response);
		} catch (Exception e) {}
		while (bet < 1 || bet > bank) {
			System.out.print("Enter a bet >> ");
			response = input.nextLine();
			try {
				bet = Integer.parseInt(response);
			} catch (Exception e) {}
		}
		return bet;
	}

}
