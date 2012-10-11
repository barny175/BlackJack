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
        def cardSrc = getCardSource([Card.TEN, Card.SEVEN, Card.SEVEN, Card.TEN, Card.TEN, Card.TEN])
        def engine = new Engine(cardSrc)
        
        def player = new StubPlayer(100)
        
        engine.addPlayer(player)

        engine.start()
        engine.nextDraw()
        
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
        engine.nextDraw()
        
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
        engine.nextDraw()
        
        player.setMove(Move.Stand)
        
        engine.nextDraw()
        
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
        engine.nextDraw()
        
        player.res = GameResult.DealerWin
        assertEquals(90, player.getMoney())
    }

    @Test
    void push() {
        def cardSrc = getCardSource([Card.TEN, Card.SEVEN, Card.SEVEN, Card.TEN])
        def engine = new Engine(cardSrc)
        
        def player = new StubPlayer(100)
        
        engine.addPlayer(player)

        engine.start()
        
        player.setMove(Move.Stand)
        engine.nextDraw()
        
        player.res = GameResult.Push
        assertEquals(100, player.getMoney())
    }

    @Test
    void getDealerCards() {
        def cardSrc = getCardSource([Card.TEN, Card.SIX, Card.TWO, Card.ACE])
        def engine = new Engine(cardSrc)
        
        def player = mock(Player.class)
        engine.addPlayer(player)
        when(player.move()).thenReturn(Move.Stand)

        engine.start()
        
        def cards = engine.getDealerCards()
        assertEquals(1, cards.size())
        assertEquals(Card.SIX, cards[0])

        engine.nextDraw()
        
        assertEquals(2, cards.size())
        assertNotNull(cards.find {it == Card.SIX})
        assertNotNull(cards.find {it == Card.ACE})
    }
    
    @Test
    void dontAskStandingForMove() {
        def cardSrc = getCardSource([Card.TEN, Card.SIX, Card.SIX, Card.THREE, Card.TEN])
        def engine = new Engine(cardSrc)
        
        def player = mock(Player.class)
        engine.addPlayer(player)
        when(player.move()).thenReturn(Move.Stand)

        engine.start()
        
        engine.nextDraw();
        engine.nextDraw();
        
        verify(player, times(1)).move()        
    }
    
    @Test
    void shuffle() {
        def cardSrc = mock(CardSource.class)
        when(cardSrc.next()).thenReturn(Card.TWO)
        
        def engine = new Engine(cardSrc)
        def player = mock(Player.class)
        engine.addPlayer(player)
        
        engine.start()
        
        verify(cardSrc).shuffle()
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

