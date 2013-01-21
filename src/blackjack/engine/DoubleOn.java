/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack.engine;

/**
 *
 * @author mbarnas
 */
public enum DoubleOn {
	All,
	NineToAce;
	
	public boolean isDoublePossible(Game game) {
		if (game.gameState() != GameState.FirstDeal) {
            return false;
        }
		
		if (game.playerCards().count() != 2) {
			return false;
		}
		
		if (this == NineToAce) {
			final int playerHardSum = game.playerCards().hardSum();
			if (playerHardSum < 9 || playerHardSum > 11) {
				return false;
			}
		}
		
		return true;
	}
}
