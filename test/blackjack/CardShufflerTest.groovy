/*
 * (C) 2012 Kerio Technologies s.r.o.
 */

package blackjack

import org.junit.Test
import blackjack.engine.*

/**
 *
 * @author mbarnas
 */
class CardShufflerTest {
	@Test
    public void allCards() {
        def shuffler = new CardShuffler(1)
        def cardCounts = [:]
        def card = null
        while (card = shuffler.next()) {
            if (!cardCounts[card])
                cardCounts[card] = 1
            else
                cardCounts[card]++;
        }
        
        GroovyTestCase.assertEquals(4, cardCounts[Card.TWO])
        GroovyTestCase.assertEquals(4, cardCounts[Card.NINE])
        GroovyTestCase.assertEquals(4, cardCounts[Card.ACE])
        GroovyTestCase.assertEquals(4, cardCounts[Card.TEN])
        GroovyTestCase.assertEquals(4, cardCounts[Card.QUEEN])
        GroovyTestCase.assertEquals(4, cardCounts[Card.JACK])
        GroovyTestCase.assertEquals(4, cardCounts[Card.KING])
    }
}

