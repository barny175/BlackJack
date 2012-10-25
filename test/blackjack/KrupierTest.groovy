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
        assertEquals(Move.Hit, krupier.move(Utils.getCardHand([Card.TWO])))
        
        assertEquals(Move.Hit, krupier.move(Utils.getCardHand([Card.TWO, Card.TEN])))

        assertEquals(Move.Hit, krupier.move(Utils.getCardHand([Card.TWO, Card.TEN, Card.FOUR])))
        
        assertEquals(Move.Stand, krupier.move(Utils.getCardHand([Card.TWO, Card.TEN, Card.FOUR, Card.ACE])))
    }
    
    @Test
    void ace() {
        def krupier = new Dealer()
        assertEquals(Move.Hit, krupier.move(Utils.getCardHand([Card.ACE])))
        
        assertEquals(Move.Hit, krupier.move(Utils.getCardHand([Card.ACE, Card.TWO])))
        
        assertEquals(Move.Stand, krupier.move(Utils.getCardHand([Card.ACE, Card.TWO, Card.FIVE])))
    }
    
    @Test
    void twoAces() {
        def krupier = new Dealer()
        assertEquals(Move.Hit, krupier.move(Utils.getCardHand([Card.ACE,Card.ACE])))
    }
}

