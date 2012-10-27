/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack.simulation.player;

import blackjack.engine.Card;
import blackjack.engine.CardHand;
import blackjack.engine.Move;

/**
 *
 * @author mbarnas
 */
public class OneHitPlayer extends BasePlayer {
	private boolean first = true;

	public OneHitPlayer(int money) {
		super(money);
	}

	
	@Override
	public Move move(CardHand cards, Card dealerUpCard) {
		if (first) {
			first = false;
			return Move.Hit;
		}
		
		return Move.Stand;
	}

	@Override
	public String getName() {
		return "One Hit";
	}
	
}
