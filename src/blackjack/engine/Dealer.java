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
    
    public void addCard(Card card) {
        this.cards.add(card);
    }
    
    public Move move() {
        final int sum = getSum();
        
        if (sum >= 17)
            return Move.Stand;
        
        return Move.Hit;
    }

    private int getSum() {
        int sum = 0;
        for (Card c : cards) {
            if (c == Card.ACE) {
                sum += 11;
            } else {
                sum += c.getValue();
            }
        }
        
        return sum;
    }

    @Override
    public List<Card> getCards() {
        return this.cards;
    }
}
