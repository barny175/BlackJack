/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack.engine.rules;

import blackjack.engine.Game;

/**
 *
 * @author mbarnas
 */
public class BellevueDoubleRules extends BasicDoubleRules {
	@Override
	public boolean isDoublePossible(Game game) {
		final int playerHardSum = game.playerCards().hardSum();
		if (playerHardSum < 9 || playerHardSum > 11)
            return false;

		return super.isDoublePossible(game);
	}
}
