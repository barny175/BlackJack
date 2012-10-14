/*
 * (C) 2012 Kerio Technologies s.r.o.
 */

package blackjack.simulation.player

import org.junit.Test
import static org.junit.Assert.*
import blackjack.engine.*
import static org.mockito.Mockito.*

/**
 *
 * @author mbarnas
 */
class BasicStrategyPlayerTest {
	@Test
    void dealerHasTwoOrThree() {
        for (Card dealerCard : [Card.TWO, Card.THREE]) {
            def game = mock(Game.class)
            when(game.getDealerUpCard()).thenReturn(dealerCard)

            def player = new BasicStrategyPlayer(10)
            player.setGame(game)

            for (Card c : Card.values()[2..-1]) {
                player.reset();
                player.addCard(Card.TWO)
                player.addCard(c)
                def expectedResult = Card.TWO.getValue() + c.getValue() < 13 ? Move.Hit : Move.Stand;
                assertEquals("Failed for card " + c + "dealer card " + dealerCard, expectedResult, player.move())
            }
        }
    }
    
    @Test
    void dealerHasFourToSix() {
        for (Card dealerCard : [Card.FOUR, Card.FIVE, Card.SIX]) {
            def game = mock(Game.class)
            when(game.getDealerUpCard()).thenReturn(dealerCard)

            def player = new BasicStrategyPlayer(10)
            player.setGame(game)

            for (Card c : Card.values()[2..-1]) {
                player.reset();
                player.addCard(Card.TWO)
                player.addCard(c)
                def expectedResult = Card.TWO.getValue() + c.getValue() < 12 ? Move.Hit : Move.Stand;
                assertEquals("Failed for card " + c + " dealer card " + dealerCard, expectedResult, player.move())
            }
        }
    }
    
    @Test
    void dealerHasMoreThanSix() {
        for (Card dealerCard : Card.SEVEN..Card.KING) {
            def game = mock(Game.class)
            when(game.getDealerUpCard()).thenReturn(dealerCard)

            def player = new BasicStrategyPlayer(10)
            player.setGame(game)

            for (Card c : Card.values()[2..-1]) {
                player.reset();
                player.addCard(Card.TWO)
                player.addCard(c)
                def expectedResult = Move.Hit;
                assertEquals("Failed for card " + c + " dealer card " + dealerCard, expectedResult, player.move())
            }
        }
    }
    
    @Test
    void dealerHasAce() {
        def game = mock(Game.class)
        when(game.getDealerUpCard()).thenReturn(Card.ACE)

        def player = new BasicStrategyPlayer(10)
        player.setGame(game)

        for (Card c : Card.values()[2..-1]) {
            player.reset();
            player.addCard(Card.TWO)
            player.addCard(c)
            def expectedResult = Move.Hit;
            assertEquals("Failed for card " + c, expectedResult, player.move())
        }
    }
}

