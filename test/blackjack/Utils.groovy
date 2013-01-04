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
    
	public static CardSource getCardSourceNoPeek(def cardsPlayer, def cardsDealer) {
	    def mergedCards = cardsPlayer.plus(1, cardsDealer[0])
		mergedCards.addAll(cardsDealer[1..-1])

        return getCardSource(mergedCards)
    	
	}
	
    public static CardSource getCardSource(def cardsPlayer, def cardsDealer) {
        def mergedCards = []
		def max = Math.max(cardsPlayer.size(), cardsDealer.size())
		max.times {
			if (it < cardsPlayer.size())
	            mergedCards.add(cardsPlayer[it])
			if (it < cardsDealer.size())
		        mergedCards.add(cardsDealer[it])
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

