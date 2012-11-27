/*
 * (C) 2012 Kerio Technologies s.r.o.
 */

package blackjack

import org.junit.Test
import blackjack.engine.*
import blackjack.engine.shufflers.*

/**
 *
 * @author mbarnas
 */
class CardShufflerTest {
	@Test
    public void baseCardShuffler() {
        def shuffler = new CardShuffler(1)
		testShuffler(shuffler)
    }
	
	@Test
    public void twoThirdsCardShuffler() {
		testShuffler(new TwoThirdsShuffler(1, 1))
    }
	
	@Test
    public void everyGameCardShuffler() {
		testShuffler(new EveryGameCardShuffler())
    }
	
	private void testShuffler(CardSource shuffler) {
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

