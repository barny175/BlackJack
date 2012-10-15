/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack.simulation.player;

import blackjack.engine.BlackJackSum;
import blackjack.engine.BlackJackSum.Sum;
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
	public Move move() {
		Sum sum = BlackJackSum.sum(cards.getCards());
		if (sum.isBlackJack)
			return Move.Stand;
		
		if (sum.sum <= 11)
			return Move.Hit;
		
		if (sum.sum == 17) 
			return Move.Stand;
		
		return Move.Stand;
	}

	@Override
	public String getName() {
		return "Simple";
	}
}
