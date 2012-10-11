/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack.simulation;

import blackjack.engine.Card;
import blackjack.engine.GameResult;
import blackjack.engine.Player;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mbarnas
 */
public abstract class BlackJackPlayer implements Player {
	protected List<Card> cards = new ArrayList<Card>(10);
	private int money;

	public BlackJackPlayer(int money) {
		this.money = money;
	}

	@Override
	public void addCard(Card card) {
		this.cards.add(card);
	}

	@Override
	public void addMoney(int amount) {
		this.money += amount;
	}

	@Override
	public List<Card> getCards() {
		return cards;
	}

	@Override
	public void resetCards() {
		this.cards.clear();
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
}
