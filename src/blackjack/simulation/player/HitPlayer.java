/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack.simulation.player;

import blackjack.engine.Card;
import blackjack.engine.CardHand;
import blackjack.engine.Move;
import java.util.Set;

/**
 *
 * @author mbarnas
 */

public class HitPlayer extends BasePlayer {

	public HitPlayer(int money) {
		super(money);
	}

	@Override
	public Move move(CardHand cards, Card dealerUpCard, Set<Move> allowedMoves) {
		return Move.Hit;
	}

	@Override
	public String getName() {
		return "Hitting";
	}
}
