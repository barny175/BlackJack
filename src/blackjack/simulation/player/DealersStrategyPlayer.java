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
public class DealersStrategyPlayer extends BasePlayer {

	public DealersStrategyPlayer(int money) {
		super(money);
	}

	@Override
	public String getName() {
		return "Dealer's Strategy";
	}

	@Override
	public Move move(CardHand cards, Card dealerUpCard, Set<Move> allowedMoves) {
		final int sum = cards.softSum();

        if (sum >= 17) {
            return Move.Stand;
        }

        return Move.Hit;
	}
	
}
