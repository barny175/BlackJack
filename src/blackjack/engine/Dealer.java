/*
 * (C) 2012 Kerio Technologies s.r.o.
 */
package blackjack.engine;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mbarnas
 */
public class Dealer implements CardHolder {

    private List<Card> cards = new ArrayList<Card>();
    private Card hiddenCard;
    private boolean revealed = false;

    @Override
    public void addCard(Card card) {
        if (!revealed && hiddenCard != null)
             throw new RuntimeException("The hole card should already be revealed.");
       
        if (!revealed && cards.size() == 1 && getSum() + getCardValue(card) != 21) {
            hiddenCard = card;
        } else {
            this.cards.add(card);
        }
    }

    public Move move() {
        final int sum = getSum();

        if (sum >= 17) {
            return Move.Stand;
        }

        return Move.Hit;
    }

    private int getSum() {
        int sum = 0;
        for (Card c : cards) {
            sum += getCardValue(c);
        }

        if (hiddenCard != null) {
            sum += getCardValue(hiddenCard);
        }

        return sum;
    }

    private int getCardValue(Card c) {
        if (c == Card.ACE) {
            return 11;
        } else {
            return c.getValue();
        }
    }

    @Override
    public List<Card> getCards() {
        return this.cards;
    }

    void revealHoleCard() {
        if (revealed) {
            throw new RuntimeException("The hole card was already revealed.");
        }

        if (hiddenCard != null) {
            cards.add(hiddenCard);
        }

        hiddenCard = null;
        revealed = true;
    }

    @Override
    public void resetCards() {
        cards.clear();
        revealed = false;
        hiddenCard = null;
    }
}
