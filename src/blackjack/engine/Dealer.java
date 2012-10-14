/*
 * (C) 2012 Kerio Technologies s.r.o.
 */
package blackjack.engine;

import java.util.List;

/**
 *
 * @author mbarnas
 */
public class Dealer implements CardHolder {

    private CardHand cards = new CardHand();
    private Card visibleCard;
    private boolean first = true;

    @Override
    public void addCard(Card card) {
        if (first) {
            visibleCard = card;
            first = false;
        } 
        this.cards.addCard(card);
    }

    @Override
    public Move move() {
        final int sum = cards.softSum();

        if (sum >= 17) {
            return Move.Stand;
        }

        return Move.Hit;
    }

    public Card getVisibleCard() {
        return visibleCard;
    }

    public List<Card> getCards() {
        return this.cards.getCards();
    }

    @Override
    public void reset() {
        cards.reset();
        first = true;
        visibleCard = null;
    }
}
