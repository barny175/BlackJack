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
class BlackJackSumTest {
	@Test
    void noAces() {
        def cards = [Card.TWO, Card.TEN, Card.FIVE]
        assertEquals(17, BlackJackSum.sum(cards).sum)
        assertEquals(false, BlackJackSum.sum(cards).isBlackJack)
    }
    
    @Test
    void oneAce() {
        assertEquals(13, BlackJackSum.sum([Card.TWO, Card.TEN, Card.ACE]).sum)
        assertEquals(false, BlackJackSum.sum([Card.TWO, Card.TEN, Card.ACE]).isBlackJack)
        
        assertEquals(21, BlackJackSum.sum([Card.TEN, Card.TEN, Card.ACE]).sum)
        assertEquals(false, BlackJackSum.sum([Card.TEN, Card.TEN, Card.ACE]).isBlackJack)
        
        assertEquals(20, BlackJackSum.sum([Card.TEN, Card.NINE, Card.ACE]).sum)
        assertEquals(false, BlackJackSum.sum([Card.TEN, Card.NINE, Card.ACE]).isBlackJack)
    }
    
    @Test
    void twoAces() {
        def sum = BlackJackSum.sum([Card.ACE, Card.ACE])
        assertEquals(12, sum.sum)
        assertEquals(Boolean.FALSE, sum.isBlackJack)
        
        sum = BlackJackSum.sum([Card.ACE, Card.ACE, Card.TEN])
        assertEquals(12, sum.sum)
        assertEquals(Boolean.FALSE, sum.isBlackJack)
        
        sum = BlackJackSum.sum([Card.ACE, Card.TEN, Card.ACE, Card.EIGHT])
        assertEquals(20, sum.sum)
        assertEquals(Boolean.FALSE, sum.isBlackJack)
    }
    
    @Test
    void blackjack() {
        assertEquals(21, BlackJackSum.sum([Card.ACE, Card.TEN]).sum)
        assertEquals(Boolean.TRUE, BlackJackSum.sum([Card.ACE, Card.TEN]).isBlackJack)
    }
    
    @Test
    void testToString() {
        def sum = BlackJackSum.sum([Card.ACE, Card.ACE])
        assertEquals('[12, false]', sum.toString())
    }
}
