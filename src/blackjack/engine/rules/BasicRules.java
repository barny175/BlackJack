/*
 * (C) 2012 Kerio Technologies s.r.o.
 */
package blackjack.engine.rules;

import blackjack.engine.*;
import com.google.common.collect.Sets;
import java.util.EnumSet;
import java.util.Set;

/**
 *
 * @author mbarnas
 */
public class BasicRules implements Rules {
    private Set<Move> allMoves = Sets.newEnumSet(EnumSet.allOf(Move.class), Move.class);
    
    @Override
    public Set<Move> getAllowedMoves(Game game) {
        Set<Move> allowedMoves = EnumSet.copyOf(allMoves);
        
        if (game.gameState() != GameState.FirstDeal) {
            allowedMoves.remove(Move.Double);
            allowedMoves.remove(Move.Split);
        }        
                    
        return allowedMoves;
    }
    
}