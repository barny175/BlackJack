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
    Set<Move> getAllowedMoves(Game game);
}
