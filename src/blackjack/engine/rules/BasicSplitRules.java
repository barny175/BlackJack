/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack.engine.rules;

import blackjack.engine.CardHand;
import blackjack.engine.Game;
import blackjack.engine.GameState;
import blackjack.engine.SplitRules;

/**
 *
 * @author mbarnas
 */
public class BasicSplitRules implements SplitRules{

	@Override
	public boolean isSplitPossible(Game game) {
		if (game.gameState() != GameState.FirstDeal) {
            return false;
        }
		
		final CardHand playerCards = game.playerCards();
		if (playerCards.count() != 2 || playerCards.get(0).getValue() != playerCards.get(1).getValue()) {
			return false;
		}
		
		return true;
	}
}
