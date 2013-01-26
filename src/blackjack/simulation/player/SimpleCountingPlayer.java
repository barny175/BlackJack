/*
 * (C) 2012 Kerio Technologies s.r.o.
 */
package blackjack.simulation.player;

import blackjack.engine.Card;
import blackjack.engine.CardHand;
import blackjack.engine.Game;
import blackjack.engine.Move;
import blackjack.engine.ShuffleObserver;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import java.util.Set;

/**
 *
 * @author mbarnas
 */
public class SimpleCountingPlayer extends BasePlayer implements ShuffleObserver {
    private final BasePlayer player;
    private int count = 0;
	private boolean registered;

	@Inject
    public SimpleCountingPlayer(BasePlayer player, @Named(BasicStrategyPlayer.DEPOSIT)int money) {
        super(money);
        this.player = player;
    }

    @Override
    public int bet() {
		if (count > 9)
			return 40;
		else if (count < 9)
			return 10;
		else
			return 20;
    }

    @Override
    public void gameEnded(Game game) {
        super.gameEnded(game);
    }

    @Override
    public String getName() {
        return "Simple Counting " + player.getName();
    }

    @Override
    public Move move(CardHand cards, Card dealerUpCard, Set<Move> allowedMoves) {
        return player.move(cards, dealerUpCard, allowedMoves);
    }

    @Override
    public void shuffling() {
        this.count = 0;
    }

	@Override
	public void newGame(Game game) {
		if (!registered) {
			game.registerShufflingObserver(this);
			registered = true;
		}
	}

	@Override
	public void cardDrawn(Card card) {
		if (card.getValue() > 2 && card.getValue() < 8) {
			count++;
		}
		if (card.getSoftValue() >= 10) {
			count--;
		}
	}
}
