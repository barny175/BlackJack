/*
 * (C) 2012 Kerio Technologies s.r.o.
 */
package blackjack.engine.rules;

import blackjack.engine.*;
import java.util.EnumSet;
import java.util.Set;

/**
 *
 * @author mbarnas
 */
public class BellevueRules extends BasicRules {

    @Override
    public Set<Move> getAllowedMoves(Game game) {
        Set<Move> allowedMoves = super.getAllowedMoves(game);
        
        final CardHand playerCards = game.playerCards();
        
        if (game.isSplitted() && playerCards.count() == 1 && playerCards.getCards().get(0) == Card.ACE) {
            return EnumSet.noneOf(Move.class);
        }
        
        return allowedMoves;
    }

	@Override
	public boolean isBlackJack(CardHand cards) {
		return !cards.isSplitted() && super.isBlackJack(cards);
	}

    @Override
    public GameState nextState(Game game) {
        if (game.gameState() == GameState.AfterSplit && game.playerCards().count() == 2
                && game.playerCards().getCards().get(0) == Card.ACE)
            return GameState.DealersGame;
        
        return super.nextState(game);
    }
}
