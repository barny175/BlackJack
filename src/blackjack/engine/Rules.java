/*
 * (C) 2012 Kerio Technologies s.r.o.
 */
package blackjack.engine;

import blackjack.engine.rules.BasicRules;
import java.util.Set;

/**
 *
 * @author mbarnas
 */
@com.google.inject.ImplementedBy(BasicRules.class)
public interface Rules {
        
	public static int BLACKJACK = 21;
	
    Set<Move> getAllowedMoves(Game game);
	
	boolean isBlackJack(CardHand cards);

    GameState nextState(Game game);	
}
