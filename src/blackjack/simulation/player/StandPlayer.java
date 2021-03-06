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
public class StandPlayer extends BasePlayer {

	public StandPlayer(int money) {
		super(money);
	}

	@Override
	public Move move(CardHand cards, Card dealerUpCard, Set<Move> allowedMoves) {
		return Move.Stand;
	}

	@Override
	public String getName() {
		return "Standing";
	}
}
