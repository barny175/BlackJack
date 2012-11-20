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
	private int money;
    protected Game currentGame;

	public BasePlayer(int money) {
		this.money = money;
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
		return 10;
	}
	
	public abstract String getName();
}
