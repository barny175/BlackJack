/*
 * (C) 2012 Kerio Technologies s.r.o.
 */
package blackjack.simulation.player;

import blackjack.engine.Card;
import blackjack.engine.CardHand;
import blackjack.engine.Game;
import blackjack.engine.Move;
import blackjack.engine.ShuffleObserver;
import java.util.Set;

/**
 *
 * @author mbarnas
 */
public class SimpleCountingPlayer extends BasePlayer implements ShuffleObserver {
    private final BasePlayer player;
    private int count = 0;

    public SimpleCountingPlayer(BasePlayer player, int money) {
        super(money);
        this.player = player;
    }

    @Override
    public int bet() {
        return super.bet();
    }

    @Override
    public void gameEnded(Game game) {
        super.gameEnded(game);
        countCards(game.playerCards());
        countCards(game.dealerCards());
    }

    @Override
    public String getName() {
        return "Simple Counting " + player.getName();
    }

    @Override
    public Move move(CardHand cards, Card dealerUpCard, Set<Move> allowedMoves) {
        return player.move(cards, dealerUpCard, allowedMoves);
    }

    private void countCards(CardHand playerCards) {
        for (Card c : playerCards.getCards()) {
            if (c == Card.ACE)
                count--;
            if (c == Card.FOUR)
                count++;
        }
    }

    @Override
    public void shuffling() {
        this.count = 0;
    }    
}
