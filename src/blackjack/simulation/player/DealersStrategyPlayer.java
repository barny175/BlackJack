/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack.simulation.player;

import blackjack.engine.Move;

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
	public Move move() {
		final int sum = cards.softSum();

        if (sum >= 17) {
            return Move.Stand;
        }

        return Move.Hit;
	}
	
}
