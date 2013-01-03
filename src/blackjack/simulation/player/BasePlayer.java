/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack.simulation.player;

import blackjack.engine.*;

/**
 *
 * @author mbarnas
 */
public abstract class BasePlayer implements Player {
	private int bet = 10;
	private int money;
    protected Game currentGame;

	public BasePlayer(int money) {
		this.money = money;
	}

	public void setBet(int bet) {
		this.bet = bet;
	}
	
	@Override
	public void addMoney(int amount) {
		this.money += amount;
	}

	@Override
	public void gameEnded(Game game) {
	}
	
	public int getMoney() {
		return money;
	}

	@Override
	public int bet() {
		return bet;
	}
	
	public abstract String getName();

	@Override
	public void newGame(Game game) {
		currentGame = game;
	}
}
