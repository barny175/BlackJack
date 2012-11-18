/*
 * (C) 2012 Kerio Technologies s.r.o.
 */

package blackjack.engine.rules

import org.junit.Test
import static org.junit.Assert.*
import blackjack.engine.*
import static org.mockito.Mockito.*
import org.mockito.*
import static blackjack.Utils.*

/**
 *
 * @author mbarnas
 */
class BellevueRulesTest {
    @Test(expected=IllegalMoveException.class)
    void disallowedDouble() {
        def cardSrc = getCardSource([Card.SIX, Card.FIVE, Card.SIX, Card.TEN, Card.TWO, Card.TWO])
        def engine = new Engine(new BellevueRules(), cardSrc)
        
        def player = mock(Player.class)
        when(player.bet()).thenReturn(10)
        when(player.move(any(), any(), any())).thenReturn(Move.Double)
        
        engine.addPlayer(player)

        engine.newGame()
        engine.start()
    }
    
    @Test
    void allowedDouble() {
        for (Card c : Card.SEVEN..Card.NINE) {
            def cardSrc = getCardSource([Card.TWO, Card.FIVE, c, Card.TEN, Card.TWO, Card.TWO])
            def engine = new Engine(new BellevueRules(), cardSrc)

            def player = mock(Player.class)
            when(player.bet()).thenReturn(10)
            when(player.move(any(), any(), any())).thenReturn(Move.Double)

            engine.addPlayer(player)

            engine.newGame()
            engine.start()
        }
    }
	
	@Test
	void onlyOneCardAfterSplitOfAces() {
		def engine = new Engine(new BellevueRules(), getCardSource([Card.ACE, Card.SIX, Card.ACE, Card.FIVE, Card.SIX, Card.NINE, Card.SEVEN]))
		def player = mock(Player.class)
		when(player.bet()).thenReturn(10)
		when(player.move(any(), any(), any())).thenReturn(Move.Split)
		engine.addPlayer(player)
		engine.newGame()
		engine.start()
		
		verify(player, times(1)).move(any(), any(), any())
		verify(player).addMoney(-10)
		verify(player).addMoney(10)
	}
}

