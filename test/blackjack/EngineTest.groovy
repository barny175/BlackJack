/*
 * (C) 2012 Kerio Technologies s.r.o.
 */

package blackjack

import org.junit.Test
import static org.junit.Assert.*
import blackjack.engine.*
import static org.mockito.Mockito.*
/**
 *
 * @author mbarnas
 */
class EngineTest {
    @Test
    void bets() {
        def player = mock(Player.class)
        when(player.getBet()).thenReturn(10)
        
        def cardSrc = getCardSource([Card.TWO, Card.TWO, Card.TWO, Card.TWO])
        def engine = new Engine(cardSrc)
        engine.addPlayer(player)
        engine.start()
        
        verify(player).getBet()        
    }
    
	@Test
    void firstDeal() {
        def cardSrc = getCardSource([Card.TWO, Card.TWO, Card.ACE,Card.TWO])
        
        def engine = new Engine(cardSrc)
        
        Player player = mock(Player.class)
        engine.addPlayer(player)
        
        engine.start()
        
        verify(player).addCard(Card.TWO)
        verify(player).addCard(Card.ACE)
    }
    
    @Test
    void blackJackOnlyPlayer() {
        def cardSrc = getCardSource([Card.ACE, Card.FIVE, Card.TEN, Card.ACE])
        def engine = new Engine(cardSrc)
        
        def player = mock(Player.class)
        when(player.getBet()).thenReturn(10)
        when(player.getCards()).thenReturn([Card.ACE, Card.TEN])
        
        engine.addPlayer(player)
        engine.start()
        
        verify(player).addCard(Card.ACE)
        verify(player).addCard(Card.TEN)
        verify(player).addMoney(25)
    }
        
    @Test
    void blackJackPlayerAndKrupier() {
        def cardSrc = getCardSource([Card.ACE, Card.ACE, Card.TEN, Card.TEN])
        def engine = new Engine(cardSrc)
        
        def player = mock(Player.class)
        when(player.getBet()).thenReturn(10)
        when(player.getCards()).thenReturn([Card.ACE, Card.TEN])
        
        engine.addPlayer(player)
        engine.start()
        
        verify(player).addMoney(10)
    }
    
    @Test
    void playerBusts() {
        def cardSrc = getCardSource([Card.TEN, Card.SEVEN, Card.SEVEN, Card.TWO, Card.TEN, Card.TEN])
        def engine = new Engine(cardSrc)
        
        def player = new StubPlayer(100)
        
        engine.addPlayer(player)

        engine.start()
        engine.continueGame()
        
        player.res = GameResult.PlayerBusted
        assertEquals(90, player.getMoney())
    }
    
    @Test
    void dealerBusts() {
        def cardSrc = getCardSource([Card.TEN, Card.SEVEN, Card.TWO, Card.EIGHT, Card.TWO, Card.TEN])
        def engine = new Engine(cardSrc)
        
        def player = new StubPlayer(100)
        
        engine.addPlayer(player)

        engine.start()
        engine.continueGame()
        
        player.res = GameResult.PlayerWin
        assertEquals(110, player.getMoney())
    }
    
    @Test
    void playerWins() {
        testPlayerWin([Card.TEN, Card.TWO, Card.EIGHT] , [Card.SEVEN, Card.TEN, Card.TEN])
        testPlayerWin([Card.TEN, Card.SEVEN, Card.TWO], [Card.TEN, Card.FIVE, Card.TWO])
        testPlayerWin([Card.TEN, Card.SEVEN, Card.THREE], [Card.TEN, Card.SEVEN, Card.TWO])
    }

    void testPlayerWin(def cardsPlayer, def cardsDealer) {
        def cardSrc = getCardSource(cardsPlayer, cardsDealer)
        def engine = new Engine(cardSrc)
        
        def player = new StubPlayer(100)
        
        engine.addPlayer(player)

        engine.start()
        engine.continueGame()
        
        player.setMove(Move.Stand)
        
        engine.continueGame()
        
        player.res = GameResult.PlayerWin
        assertEquals(110, player.getMoney())        
    }
    
    @Test
    void dealerWins() {
        def cardSrc = getCardSource([Card.TEN, Card.SIX, Card.TWO, Card.TEN, Card.EIGHT, Card.FOUR])
        def engine = new Engine(cardSrc)
        
        def player = new StubPlayer(100)
        
        engine.addPlayer(player)

        engine.start()
        engine.continueGame()
        
        player.res = GameResult.DealerWin
        assertEquals(90, player.getMoney())
    }

    private CardSource getCardSource(def cardsPlayer, def cardsDealer) {
        assert cardsPlayer.size() == cardsDealer.size()
        def mergedCards = []
        cardsPlayer.size().times {
            mergedCards[it * 2] = cardsPlayer[it]
            mergedCards[it * 2 + 1] = cardsDealer[it]
        }
        
        return getCardSource(mergedCards)
    }
    
    private CardSource getCardSource(def cards) {
        def cardSrc = mock(CardSource.class)
        def w = when(cardSrc.next())
        cards.each { w = w.thenReturn(it) }
        w.thenReturn(null)
        return cardSrc
    }
}

