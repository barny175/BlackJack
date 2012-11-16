/*
 * (C) 2012 Kerio Technologies s.r.o.
 */
package blackjack.simulation.player;

import blackjack.engine.*;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import java.io.PrintWriter;
import java.util.Set;

/**
 *
 * @author mbarnas
 */
public class EquityCurvePlayer extends BasePlayer {
    private final Player player;
    private final PrintWriter writer;

    @Inject
    public EquityCurvePlayer(@Named(BasicStrategyPlayer.DEPOSIT) int money, @Named("DECORATED_PLAYER") Player player, PrintWriter writer) {
        super(money);
        
        this.player = player;
        this.writer = writer;
    }

    @Override
    public String getName() {
        return "decorated by Equity Curve";
    }

    @Override
    public Move move(CardHand cards, Card dealerUpCard, Set<Move> allowedMoves) {
        return player.move(cards, dealerUpCard, allowedMoves);
    }

    @Override
    public void gameEnded(Game result) {
        writer.println(this.getMoney());
        writer.flush();
        super.gameEnded(result);
    }
}
