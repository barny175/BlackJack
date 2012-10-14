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
public class StandPlayer extends BasePlayer {

	public StandPlayer(int money) {
		super(money);
	}

	@Override
	public Move move() {
		return Move.Stand;
	}
}
