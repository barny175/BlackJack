/*
 * (C) 2012 Kerio Technologies s.r.o.
 */
package blackjack.engine.rules;

import blackjack.engine.CardHand;
import blackjack.engine.Game;
import blackjack.engine.Move;
import java.util.Set;

/**
 *
 * @author mbarnas
 */
public class BellevueRules extends BasicRules {

    @Override
    public Set<Move> getAllowedMoves(Game game) {
        Set<Move> allowedMoves = super.getAllowedMoves(game);
        
        final int playerHardSum = game.playerCards().hardSum();
        if (playerHardSum < 9 || playerHardSum > 11)
            allowedMoves.remove(Move.Double);
        
        return allowedMoves;
    }

	@Override
	public boolean isBlackJack(CardHand cards) {
		return !cards.isSplitted() && super.isBlackJack(cards);
	}
}
