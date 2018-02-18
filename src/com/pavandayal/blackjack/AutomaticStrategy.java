// Provides look up table for what action to do given current hand
package com.pavandayal.blackjack;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.Arrays;
import org.json.JSONObject;
import org.json.JSONArray;

public class AutomaticStrategy extends Strategy {
	private static final int[] dealerTotals = {1,2,3,4,5,6,7,8,9,10};
	private static final int[] playerTotals = {4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,-12,-13,-14,-15,-16,-17,-18,-19,-20,-21,91,92,93,94,95,96,97,98,99,90};
	private static final String[] playerMoves = {"double", "split", "stay", "hit"};
	private final String strategyFileLocation;
	private final JSONObject json;
	private final int[][] lookup;

	public AutomaticStrategy(String s) {
		strategyFileLocation = s;
		json = new JSONObject(readStrategyFile());
		lookup = new int[dealerTotals.length][playerTotals.length];
		int dealerIndex;
		int playerIndex;
		Move playerMove;
		JSONObject dealer = (JSONObject) json.get("dealer");
		JSONObject total;
		JSONArray move;
		for (String dealerTotal : dealer.keySet()) {
			dealerIndex = indexOf(Math.abs(Integer.parseInt(dealerTotal)), dealerTotals);
			total = (JSONObject) dealer.get(dealerTotal);
			for (String totalMove : total.keySet()) {
				playerMove = Move.valueOf(totalMove.toUpperCase());
				move = (JSONArray) total.get(totalMove);
				for (Object playerTotal : move) {
					playerIndex = indexOf(Integer.parseInt(String.valueOf(playerTotal)), playerTotals);
					if (playerIndex >= 0 && dealerIndex >= 0) {
						lookup[dealerIndex][playerIndex] = playerMove.getIndex();
					}
				}
			}
		}

		int playerTotal;
		int softIndex = indexOf(-12, playerTotals);
		for (int d = 0; d < lookup.length; d++) {
			for (int p = softIndex; p < lookup[0].length; p++) {
				if (lookup[d][p] == 0) {
					playerTotal = Math.abs(playerTotals[p]);
					if (playerTotal >= 90) {
						playerTotal %= 10;
						playerTotal *= 2;
						if (playerTotal == 0) {
							playerTotal = 20;
						}
						if (playerTotal == 2) {
							playerTotal = 12;
						}
					}
					playerIndex = indexOf(playerTotal, playerTotals);
					lookup[d][p] = lookup[d][playerIndex];
				}
			}
		}
	}

	private String readStrategyFile() {
		String s = "";
		String line = "";
		try {
			FileInputStream f = new FileInputStream(strategyFileLocation);
			InputStreamReader is = new InputStreamReader(f);
			BufferedReader br = new BufferedReader(is);

			line = br.readLine();
			while (line != null) {
				s += line;
				line = br.readLine();
			}

			br.close();
			is.close();
			f.close();
		} catch (FileNotFoundException e) {
			//System.err.println("Error: Couldn't find file '"+strategyFileLocation+"'");
			System.exit(1);
		} catch (IOException e) {
			//System.err.println("Error: Couldn't read file '"+strategyFileLocation+"'");
			System.exit(1);
		}

		return s;
	}

	private int indexOf(int element, int[] array) {
		for (int i = 0; i < array.length; i++) {
			if (array[i] == element) {
				return i;
			}
		}
		return -1;
	}

	private int closestIndex(int element, int[] array) {
		int close = 0;
		int residual = Math.abs(array[0] - element);
		int newResidual;
		for (int i = 1; i < array.length; i++) {
			newResidual = Math.abs(array[i] - element);
			if (newResidual < residual) {
				residual = newResidual;
				close = i;
			}
		}
		return close;
	}

	public String toString() {
		String s = Arrays.toString(dealerTotals) + "\n";
		for (int p = 0; p < lookup[0].length; p++) {
			s += playerTotals[p] + ": ";
			for (int d = 0; d < lookup.length; d++) {
				s += Move.fromIndex(lookup[d][p]).name().charAt(0) + "  ";
			}
			s += "\n";
		}

		return s.replaceAll("-", "S");
	}
	
	public Move move(Player player, Hand dealer) {
		return move(player.getCurrentHand(), dealer);
	}

	public Move move(Hand player, Hand dealer) {
		int dealerHandTotal = Math.abs(dealer.handTotal());
		int playerHandTotal = player.handTotal();

		if (playerHandTotal > 21 || dealerHandTotal > 21) {
			return Move.STAY;
		}

		if (playerHandTotal == 20) {
			playerHandTotal = 90;
		}else if (player.canSplit()) {
			playerHandTotal = (player.get(0).value % 10) + 90;
		}

		int playerIndex = indexOf(playerHandTotal, playerTotals);
		int dealerIndex =  indexOf(dealerHandTotal, dealerTotals);
		if (playerIndex < 0) {
			playerIndex = closestIndex(playerHandTotal, playerTotals);
			//System.out.println("let player hand be '"+playerTotals[playerIndex]+"'");
		}
		if (dealerIndex < 0) {
			dealerIndex =  closestIndex(dealerHandTotal, dealerTotals);
			//System.out.println("let dealer hand be '"+dealerTotals[dealerIndex]+"'");
		}

		if (playerIndex < 0 || dealerIndex < 0) {
			System.err.println("Error: couldn't determine strategy for " + player + " vs " + dealer);
			return Move.STAY;
		}

		return Move.fromIndex(lookup[dealerIndex][playerIndex]);
	}

	public int getBet() {
		return 2;
	}

	public int getBet(int bank) {
		return 2;
	}

}
