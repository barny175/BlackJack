/*
 * (C) 2012 Kerio Technologies s.r.o.
 */

package blackjack

import org.junit.Test
import static org.junit.Assert.*
import blackjack.engine.*
import blackjack.simulation.*

/**
 *
 * @author mbarnas
 */
class SimulationCardShufflerTest {
    @Test
    public void allCards() {
        def shuffler = new SimulationCardShuffler(1, Card.ACE, Card.TWO, Card.JACK)
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

    @Test
    public void firstThreeCards() {
        def shuffler = new SimulationCardShuffler(1, Card.ACE, Card.TWO, Card.JACK)
        assertEquals(Card.ACE, shuffler.next())
        assertEquals(Card.TWO, shuffler.next())
        assertEquals(Card.JACK, shuffler.next())
    }	
}

