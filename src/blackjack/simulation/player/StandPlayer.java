/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack.simulation.player;

import blackjack.engine.Move;
import blackjack.simulation.BlackJackPlayer;

/**
 *
 * @author mbarnas
 */
public class StandPlayer extends BlackJackPlayer {

	public StandPlayer(int money) {
		super(money);
	}

	@Override
	public Move move() {
		return Move.Stand;
	}
}
