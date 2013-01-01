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
        return EnumSet.copyOf(allMoves);
    }

	
    @Override
    public boolean isBlackJack(CardHand cards) {
        return cards.count() == 2 && cards.softSum() == Rules.BLACKJACK;
    }

    @Override
    public GameState nextState(Game game) {
        switch (game.gameState()) {
            case AfterSplit:
                return GameState.FirstDeal;
        }
        throw new IllegalStateException("Method is not expected to be used in this place.");
    }
}
