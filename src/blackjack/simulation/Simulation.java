/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack.simulation;

import blackjack.engine.CardSource;
import blackjack.engine.Engine;
import blackjack.engine.IllegalMoveException;
import blackjack.engine.Player;
import blackjack.engine.Rules;
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
	private int bestScore = 0;
	private int numberOfGames = 0;
	private Engine engine;
	private CardSource cardShuffler;
	private Rules rules;

	public Simulation(Rules rules) {
		this.rules = rules;
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

	public void setInitialMoney(int initialMoney) {
		this.initialMoney = initialMoney;
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
		this.engine = new Engine(cardShuffler);
		
		this.engine.setRules(rules);
		this.engine.addPlayer(player);
	}
}
