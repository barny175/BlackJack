/*
 * (C) 2012 Kerio Technologies s.r.o.
 */

package blackjack

import org.junit.Test
import static org.junit.Assert.*
import blackjack.engine.*
import blackjack.engine.shufflers.*
import blackjack.simulation.SimulationCardShuffler


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
    public void simulationCardShuffler() {
		testShuffler(new SimulationCardShuffler(1, Card.ACE, Card.TWO, Card.JACK))
    }
	
	
    @Test
    public void simulationShufflerFirstCards() {
        def shuffler = new SimulationCardShuffler(1, Card.ACE, Card.TWO, Card.JACK)
        assertEquals(Card.ACE, shuffler.next())
        assertEquals(Card.TWO, shuffler.next())
        assertEquals(Card.JACK, shuffler.next())
		
		shuffler.newGame();
		
		assertEquals(Card.ACE, shuffler.next())
        assertEquals(Card.TWO, shuffler.next())
        assertEquals(Card.JACK, shuffler.next())
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

