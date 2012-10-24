/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack.simulation.player;

import blackjack.engine.CardHand;
import blackjack.engine.Move;

/**
 *
 * @author mbarnas
 */
public class SimplePlayer extends BasePlayer  {

	public SimplePlayer(int money) {
		super(money);
	}

	@Override
	public Move move(CardHand cards) {
		if (cards.isBlackJack())
			return Move.Stand;
		
		if (cards.softSum() <= 11)
			return Move.Hit;
		
		if (cards.softSum() == 17) 
			return Move.Stand;
		
		return Move.Stand;
	}

	@Override
	public String getName() {
		return "Simple";
	}
}
