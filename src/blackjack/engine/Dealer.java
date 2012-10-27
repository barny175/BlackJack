/*
 * (C) 2012 Kerio Technologies s.r.o.
 */
package blackjack.engine;

/**
 *
 * @author mbarnas
 */
public class Dealer {

    public Move move(CardHand cards) {
        final int sum = cards.softSum();

        if (sum >= 17) {
            return Move.Stand;
        }

        return Move.Hit;
    }
}
