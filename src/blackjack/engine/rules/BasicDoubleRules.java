/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack.engine.rules;

import blackjack.engine.DoubleRules;
import blackjack.engine.Game;
import blackjack.engine.GameState;

/**
 *
 * @author mbarnas
 */
public class BasicDoubleRules implements DoubleRules {

	@Override
	public boolean isDoublePossible(Game game) {
        if (game.gameState() != GameState.FirstDeal) {
            return false;
        }
		
		if (game.playerCards().count() != 2)
			return false;
		
		return true;
	}	
}
