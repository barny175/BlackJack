/*
 * (C) 2012 Kerio Technologies s.r.o.
 */
package blackjack.simulation.player;

import blackjack.engine.Card;
import blackjack.engine.CardHand;
import blackjack.engine.Move;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import java.util.Set;

/**
 *
 * @author mbarnas
 */
public class BellevuePlayer extends BasicStrategyPlayer {

    @Inject
    public BellevuePlayer(@Named(BasicStrategyPlayer.DEPOSIT) int money) {
        super(money);
    }

    @Override
    public Move move(CardHand cards, Card dealerUpCard, Set<Move> allowedMoves) {
        return super.move(cards, dealerUpCard, allowedMoves);
    }

    @Override
    public String getName() {
        return "Bellevue Basic Strategy";
    }

}
