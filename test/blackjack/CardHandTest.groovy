/*
 * (C) 2012 Kerio Technologies s.r.o.
 */

package blackjack

import org.junit.Test;
import static org.junit.Assert.*;
import blackjack.engine.*;

/**
 *
 * @author mbarnas
 */
class CardHandTest {
	@Test
    void noAces() {
        def cards = [Card.TWO, Card.TEN, Card.FIVE]
        
        CardHand cardHand = new CardHand()
        cards.each { cardHand.addCard(it) }
        
        assertEquals(17, cardHand.softSum())
        assertEquals(false, cardHand.isBlackJack())
    }
    
    @Test
    void oneAce() {
        CardHand cardHand = new CardHand()
        [Card.TWO, Card.TEN, Card.ACE].each { cardHand.addCard(it) }
        
        assertEquals(13, cardHand.softSum())
        assertEquals(false, cardHand.isBlackJack())
        
        cardHand.reset();
        
        [Card.TEN, Card.TEN, Card.ACE].each { cardHand.addCard(it) }
        assertEquals(21, cardHand.softSum())
        assertEquals(false, cardHand.isBlackJack())
        
        cardHand.reset();
        
        [Card.TEN, Card.NINE, Card.ACE].each { cardHand.addCard(it) }
        assertEquals(20, cardHand.softSum())
        assertEquals(false, cardHand.isBlackJack())
    }
    
    @Test
    void twoAces() {
        CardHand cardHand = new CardHand()
        [Card.ACE, Card.ACE].each { cardHand.addCard(it) }
        
        assertEquals(12, cardHand.softSum())
        assertEquals(false, cardHand.isBlackJack())
        
        cardHand.reset();
        
        [Card.ACE, Card.ACE, Card.TEN].each { cardHand.addCard(it) }
        assertEquals(12, cardHand.softSum())
        assertEquals(Boolean.FALSE, cardHand.isBlackJack())
        
        cardHand.reset();
         
        [Card.ACE, Card.TEN, Card.ACE, Card.EIGHT].each { cardHand.addCard(it) }
        assertEquals(20, cardHand.softSum())
        assertEquals(Boolean.FALSE, cardHand.isBlackJack())
    }
    
    @Test
    void blackjack() {
        CardHand cardHand = new CardHand()
        [Card.ACE, Card.TEN].each { cardHand.addCard(it) }
        assertEquals(21, cardHand.softSum())
        assertEquals(Boolean.TRUE, cardHand.isBlackJack())
    }
    
    @Test
    void testToString() {
        CardHand cardHand = new CardHand()
        [Card.ACE, Card.ACE].each { cardHand.addCard(it) }
        assertEquals('[12, false]', cardHand.toString())
    }
}
