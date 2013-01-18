/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack.simulation;

import blackjack.engine.CardSource;
import blackjack.engine.DoubleRules;
import blackjack.engine.Engine;
import blackjack.engine.IllegalMoveException;
import blackjack.engine.Player;
import blackjack.engine.Rules;
import blackjack.engine.rules.BasicDoubleRules;
import blackjack.engine.rules.BasicRules;
import blackjack.simulation.player.*;

/**
 *
 * @author mbarnas
 */
public class Simulation {

	public static final int GAMES = 1000000;
	private BasePlayer player;
	private int games = 100000;
	private int initialMoney = 100000;
	private int bet = 2;
	private boolean doubleAfterSplit = true;
	private DoubleRules doubleRules = new BasicDoubleRules();
	protected Rules rules = new BasicRules();
	private boolean peek = true;
	private int bestScore = 0;
	private int numberOfGames = 0;
	private Engine engine;
	private CardSource cardShuffler;
	private boolean resplitAces;

	public void setResplitAces(boolean resplitAces) {
		this.resplitAces = resplitAces;
	}

	public void setPlayer(BasePlayer player) {
		this.player = player;
	}

	public void setCardShuffler(CardSource cardShuffler) {
		this.cardShuffler = cardShuffler;
	}

	public int getBestScore() {
		return bestScore;
	}

	public float getWinPerGame() {
		return ((float) (bestScore - initialMoney)) / numberOfGames / bet;
	}

	public int getNumberOfGames() {
		return numberOfGames;
	}
	
	public void setBet(int bet) {
		this.bet = bet;
	}

	public void setGames(int games) {
		this.games = games;
	}

	public void setDoubleAfterSplit(boolean doubleAfterSplit) {
		this.doubleAfterSplit = doubleAfterSplit;
	}

	public void setDoubleRules(DoubleRules doubleRules) {
		this.doubleRules = doubleRules;
	}

	public void setInitialMoney(int initialMoney) {
		this.initialMoney = initialMoney;
	}

	public void setPeek(boolean peek) {
		this.peek = peek;
	}

	public void setRules(Rules rules) {
		this.rules = rules;
	}

	public void run() throws IllegalMoveException {
		prepareEngine(cardShuffler, player);

		int i = 0;
		for (; i < games; i++) {
			this.engine.newGame();
			this.engine.start();

			if (player.getMoney() < 10) {
				break;
			}
		}

		this.bestScore = player.getMoney();

		this.numberOfGames = i;
	}

	private void prepareEngine(final CardSource cardShuffler, final Player player) {
		this.engine = new Engine(rules, cardShuffler);
		this.engine.setPeek(this.peek);
		this.engine.setDoubleAfterSplit(this.doubleAfterSplit);
		this.engine.setDoubleRules(this.doubleRules);
		this.engine.setResplitAces(resplitAces);
		this.engine.addPlayer(player);
	}
}
