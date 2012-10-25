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
        when(player.bet()).thenReturn(10)
        
        def cardSrc = getCardSource([Card.TWO, Card.TWO, Card.TWO, Card.TWO])
        def engine = new Engine(cardSrc)
        engine.addPlayer(player)
        engine.newGame()
        
        verify(player).bet()        
    }
    
    @Test
    void blackJackOnlyPlayer() {
        def cardSrc = getCardSource([Card.ACE, Card.FIVE, Card.TEN, Card.ACE])
        def engine = new Engine(cardSrc)
        
        def player = mock(Player.class)
        when(player.bet()).thenReturn(10)
        
        engine.addPlayer(player)
        engine.newGame()
        engine.start()
        
        verify(player).addMoney(15)
    }
        
    @Test
    void blackJackPlayerAndKrupier() {
        def cardSrc = getCardSource([Card.ACE, Card.ACE, Card.TEN, Card.TEN])
        def engine = new Engine(cardSrc)
        
        def player = mock(Player.class)
        when(player.bet()).thenReturn(10)
        
        engine.addPlayer(player)
        engine.newGame()
        engine.start()
        
        verify(player, never()).addMoney(0)
        assertEquals(GameState.Push, engine.getGameState())
    }
    
    @Test
    void dealerBlackJack() {
        def cardSrc = getCardSource([Card.TWO, Card.ACE, Card.TEN, Card.TEN])
        def engine = new Engine(cardSrc)
        
        def player = mock(Player.class)
        when(player.bet()).thenReturn(10)
        
        engine.addPlayer(player)
        engine.newGame()
        engine.start()
        
        verify(player).result(GameState.DealerWin)
        assertEquals(GameState.DealerWin, engine.getGameState())    
    }
    
    
    @Test
    void playerBusts() {
        def cardSrc = getCardSource([Card.TEN, Card.SEVEN, Card.SEVEN, Card.TEN, Card.TEN, Card.TEN])
        def engine = new Engine(cardSrc)
        
        def player = new StubPlayer(100)
        
        engine.addPlayer(player)

        engine.newGame()
        engine.start()
        
        assertEquals(GameState.PlayerBusted, player.res)
        assertEquals(90, player.getMoney())
    }
    
    @Test
    void dealerBusts() {
        def cardSrc = getCardSource([Card.TEN, Card.SEVEN, Card.TWO, Card.EIGHT, Card.TWO, Card.TEN])
        def engine = new Engine(cardSrc)
        
        Player player = mock(Player.class)
        when(player.move(any())).thenReturn(Move.Hit).thenReturn(Move.Stand)
        when(player.bet()).thenReturn(10)
        
        engine.addPlayer(player)

        engine.newGame()
        engine.start()
        
        verify(player).result(GameState.PlayerWin)
        verify(player).addMoney(10)
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
        player.setMoves([Move.Hit, Move.Stand])
        
        engine.addPlayer(player)
        engine.newGame()
        
        engine.start()
        
        player.res = GameState.PlayerWin
        assertEquals(110, player.getMoney())        
    }
    
    @Test
    void dealerWins() {
        def cardSrc = getCardSource([Card.TEN, Card.SIX, Card.TWO, Card.TEN, Card.SEVEN, Card.FOUR])
        def engine = new Engine(cardSrc)
        
        def player = new StubPlayer(100)
        player.setMoves([Move.Hit, Move.Stand])
        engine.addPlayer(player)

        engine.newGame()
        engine.start()
        
        player.res = GameState.DealerWin
        assertEquals(90, player.getMoney())
    }

    @Test
    void push() {
        def cardSrc = getCardSource([Card.TEN, Card.SEVEN, Card.SEVEN, Card.TEN])
        def engine = new Engine(cardSrc)
        
        def player = new StubPlayer(100)
        
        engine.addPlayer(player)

        engine.newGame()
        
        player.setMoves([Move.Stand])
        engine.start()
        
        player.res = GameState.Push
        assertEquals(100, player.getMoney())
    }

    @Test
    void getDealerCards() {
        def cardSrc = getCardSource([Card.TEN, Card.SIX, Card.TWO, Card.ACE])
        def engine = new Engine(cardSrc)
        
        def player = mock(Player.class)
        engine.addPlayer(player)
        when(player.move(any())).thenReturn(Move.Stand)

        engine.newGame()
        
        def card = engine.getDealerUpCard()
        assertEquals(Card.SIX, card)
    }
    
    @Test
    void dontAskStandingForMove() {
        def cardSrc = getCardSource([Card.TEN, Card.SIX, Card.SIX, Card.THREE, Card.TEN])
        def engine = new Engine(cardSrc)
        
        def player = mock(Player.class)
        
        engine.addPlayer(player)
        when(player.move(any())).thenReturn(Move.Stand)

        engine.newGame()
        
        engine.start();
        
        verify(player, times(1)).move(any())        
    }
    
    @Test
    void shuffle() {
        def cardSrc = mock(CardSource.class)
        when(cardSrc.next()).thenReturn(Card.TWO)
        
        def engine = new Engine(cardSrc)
        def player = mock(Player.class)
        
        engine.addPlayer(player)
        
        engine.newGame()
        
        verify(cardSrc).shuffle()
    }
    
    @Test
    void doubleDown() {
        def cardSrc = getCardSource([Card.NINE, Card.FIVE, Card.SEVEN, Card.TEN, Card.TWO, Card.TWO])
        def engine = new Engine(cardSrc)
        
        def player = mock(Player.class)
        when(player.bet()).thenReturn(10)
        when(player.move(any())).thenReturn(Move.Double)
        
        engine.addPlayer(player)

        engine.newGame()
        engine.start()
        
        verify(player, never()).addMoney(10)
        verify(player).result(GameState.PlayerWin)
        verify(player, times(1)).move(any())
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

