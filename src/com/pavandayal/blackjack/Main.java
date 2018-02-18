// Play blackjeck
package com.pavandayal.blackjack;

public class Main {

	public static void main(String[] args) {
		String[] a = {"10"};
		//args = a;

		AutomaticStrategy as = new AutomaticStrategy("strategy/dealer.json");
		Shoe s = new Shoe(2);
		Dealer d = new Dealer(as, s);
		Table t = new Table(d);
		Player p;
		if (args.length > 0) {
			p = new Player(new Strategy(), Integer.parseInt(args[0]));
		} else {
			p = new Player(new AutomaticStrategy("strategy/basic.json"), 10);
		}
		t.addPlayer(p);
		t.playGame((args.length > 0));
	}

}
