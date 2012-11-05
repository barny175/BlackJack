/*
 * (C) 2012 Kerio Technologies s.r.o.
 */

package blackjack

import blackjack.engine.CardHand
import blackjack.engine.CardSource
import static org.mockito.Mockito.*


/**
 *
 * @author mbarnas
 */
public class Utils {
	public static CardHand getCardHand(def cards) {
        def ch = new CardHand()
        cards.each { ch.addCard(it) }
        return ch
    }
    
    public static CardSource getCardSource(def cardsPlayer, def cardsDealer) {
        assert cardsPlayer.size() == cardsDealer.size()
        def mergedCards = []
        cardsPlayer.size().times {
            mergedCards[it * 2] = cardsPlayer[it]
            mergedCards[it * 2 + 1] = cardsDealer[it]
        }
        
        return getCardSource(mergedCards)
    }
    
    public static CardSource getCardSource(def cards) {
        def cardSrc = mock(CardSource.class)
        def w = when(cardSrc.next())
        cards.each { w = w.thenReturn(it) }
        w.thenReturn(null)
        return cardSrc
    }
}

