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
public class OneHitPlayer extends BlackJackPlayer {
	private boolean first = true;

	public OneHitPlayer(int money) {
		super(money);
	}

	
	@Override
	public Move move() {
		if (first) {
			first = false;
			return Move.Hit;
		}
		
		return Move.Stand;
	}
	
}
