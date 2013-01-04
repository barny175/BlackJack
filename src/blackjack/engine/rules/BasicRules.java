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
}
