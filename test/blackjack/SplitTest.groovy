/*
 * (C) 2012 Kerio Technologies s.r.o.
 */

package blackjack

import org.junit.Test
import static org.junit.Assert.*
import blackjack.engine.*
import static org.mockito.Mockito.*
import static blackjack.Utils.*

/**
 *
 * @author mbarnas
 */
class SplitTest {
    @Test
    void splitGame() {
        def cardSrc = getCardSource([Card.TWO, Card.SEVEN, Card.TWO, Card.TEN, Card.TEN, Card.TEN])
        def engine = new Engine(cardSrc)
        
        def player = mock(Player.class)
        when(player.bet()).thenReturn(10)
        when(player.move(any(), any())).thenReturn(Move.Split).thenReturn(Move.Stand).thenReturn(Move.Stand)
        
        engine.addPlayer(player)

        engine.newGame()
        engine.start()
        
        verify(player, times(3)).move(any(), any())
        verify(player, times(2)).addMoney(-10)
    }
}

