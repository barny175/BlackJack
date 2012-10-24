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

public class HitPlayer extends BasePlayer {

	public HitPlayer(int money) {
		super(money);
	}

	@Override
	public Move move(CardHand cards) {
		return Move.Hit;
	}

	@Override
	public String getName() {
		return "Hitting";
	}
}
