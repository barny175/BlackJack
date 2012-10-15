/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack.simulation.player;

import blackjack.engine.*;
import java.util.List;

/**
 *
 * @author mbarnas
 */
public abstract class BasePlayer implements Player {
	protected CardHand cards = new CardHand();
	private int money;
    protected Game currentGame;

	public BasePlayer(int money) {
		this.money = money;
	}

	@Override
	public void addCard(Card card) {
		this.cards.addCard(card);
	}

	@Override
	public void addMoney(int amount) {
		this.money += amount;
	}

    @Override
    public void setGame(Game game) {
        this.currentGame = game;
    }

	@Override
	public List<Card> getCards() {
		return cards.getCards();
	}

	@Override
	public void reset() {
		this.cards = new CardHand();
	}

	@Override
	public void result(GameResult result) {
	}
	
	public int getMoney() {
		return money;
	}

	@Override
	public int getBet() {
		this.money -= 10;
		return 10;
	}
	
	public abstract String getName();
}
