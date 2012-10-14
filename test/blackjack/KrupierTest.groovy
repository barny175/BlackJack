/*
 * (C) 2012 Kerio Technologies s.r.o.
 */

package blackjack

import org.junit.Test;
import static org.junit.Assert.*
import blackjack.engine.*;

/**
 *
 * @author mbarnas
 */
class KrupierTest {
	@Test
    void move() {
        def krupier = new Dealer()
        krupier.addCard(Card.TWO)
        assertEquals(Move.Hit, krupier.move())
        
        krupier.addCard(Card.TEN)
        assertEquals(Move.Hit, krupier.move())

        krupier.addCard(Card.FOUR)
        assertEquals(Move.Hit, krupier.move())
        
        krupier.addCard(Card.ACE)
        assertEquals(Move.Stand, krupier.move())
    }
    
    @Test
    void ace() {
        def krupier = new Dealer()
        krupier.addCard(Card.ACE)
        assertEquals(Move.Hit, krupier.move())
        
        krupier.addCard(Card.TWO)
        assertEquals(Move.Hit, krupier.move())
        
        krupier.addCard(Card.FIVE)
        assertEquals(Move.Stand, krupier.move())
    }
    
    @Test
    void twoAces() {
        def krupier = new Dealer()
        krupier.addCard(Card.ACE)
        krupier.addCard(Card.ACE)
        assertEquals(Move.Hit, krupier.move())
    }
}

