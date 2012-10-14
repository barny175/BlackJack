/*
 * (C) 2012 Kerio Technologies s.r.o.
 */
package blackjack.engine;

import java.util.List;

/**
 *
 * @author mbarnas
 */
public interface CardHolder {
    void addCard(Card card);
    
    void reset();

    Move move();

    List<Card> getCards();
}
