/*
 * (C) 2012 Kerio Technologies s.r.o.
 */

package blackjack.engine

import org.junit.Test
import static org.junit.Assert.*
import blackjack.*
import static org.mockito.Mockito.*
import org.mockito.*
import static blackjack.Utils.*

/**
 *
 * @author mbarnas
 */
class EngineTest {
    @Test
    void bets() {
        def player = mock(Player.class)
        when(player.bet()).thenReturn(10)
        
        def engine = getEngine([Card.TWO, Card.TWO, Card.TWO, Card.TWO])
        engine.addPlayer(player)
        engine.newGame()
        
        verify(player).bet()        
    }
    
    @Test
    void blackJackOnlyPlayer() {
        def cardSrc = getCardSource([Card.ACE, Card.FIVE, Card.TEN, Card.ACE])
        def engine = getEngine(cardSrc)
        
        def player = mock(Player.class)
        when(player.bet()).thenReturn(10)
        
        engine.addPlayer(player)
        engine.newGame()
        engine.start()
        
        verify(player).addMoney(15)
    }
        
    @Test
    void blackJackPlayerAndKrupier() {
        def engine = getEngine([Card.ACE, Card.ACE, Card.TEN, Card.TEN])
        
        def player = mock(Player.class)
        when(player.bet()).thenReturn(10)
        
        engine.addPlayer(player)
        Game game = engine.newGame()
        engine.start()
        
        verify(player, never()).addMoney(0)
        assertEquals(GameResult.Push, game.gameResult())
    }
    
    @Test
    void dealerBlackJack() {
        def cardSrc = getCardSource([Card.TWO, Card.TEN, Card.TEN, Card.ACE])
        def engine = getEngine(cardSrc)
        
        def player = mock(Player.class)
        when(player.bet()).thenReturn(10)
        
        engine.addPlayer(player)
        Game game = engine.newGame()
        engine.start()
        
        assertEquals(GameResult.DealerWin, game.gameResult())    
    }
    
    @Test
    void dealerPeeks() {
        def cardSrc = getCardSource([Card.TEN, Card.SEVEN, Card.TWO, Card.TEN, Card.TWO])
        def engine = getEngine(cardSrc)
        engine.setPeek(true)
        
		def player = mock(Player.class)
        when(player.bet()).thenReturn(10)
        when(player.move(any(), any(), any())).thenReturn(Move.Hit).thenReturn(Move.Stand)
        
        engine.addPlayer(player)

        Game game = engine.newGame()
        engine.start()
        
        assertTrue(game.dealerCards().getCards().contains(Card.SEVEN))
		assertTrue(game.dealerCards().getCards().contains(Card.TEN))
    }
    
    @Test
    void dealerNoPeek() {
        def cardSrc = getCardSource([Card.TEN, Card.SEVEN, Card.TWO, Card.TEN, Card.TWO, Card.TEN])
        def engine = getEngine(cardSrc)
        engine.setPeek(false)
        
		def player = mock(Player.class)
        when(player.bet()).thenReturn(10)
        when(player.move(any(), any(), any())).thenReturn(Move.Hit).thenReturn(Move.Stand)
        
        engine.addPlayer(player)

        Game game = engine.newGame()
        engine.start()
        
        assertTrue(game.dealerCards().getCards().contains(Card.SEVEN))
		assertTrue(game.dealerCards().getCards().contains(Card.TWO))
    }
	
    @Test
    void playerBusts() {
        def cardSrc = getCardSource([Card.TEN, Card.SEVEN, Card.SEVEN, Card.TEN, Card.TEN, Card.TEN])
        def engine = getEngine(cardSrc)
        
        def player = new StubPlayer(100)
        
        engine.addPlayer(player)

        engine.newGame()
        engine.start()
        
        assertEquals(GameResult.PlayerBusted, player.res)
        assertEquals(90, player.getMoney())
    }
    
    @Test
    void dealerBusts() {
        def cardSrc = getCardSource([Card.TEN, Card.SEVEN, Card.TWO, Card.EIGHT, Card.TWO, Card.TEN])
        def engine = getEngine(cardSrc)
        
        Player player = mock(Player.class)
        when(player.move(any(), any(), any())).thenReturn(Move.Hit).thenReturn(Move.Stand)
        when(player.bet()).thenReturn(10)
        
        ArgumentCaptor<Game> gameCaptor = ArgumentCaptor.forClass(Game.class);

        engine.addPlayer(player)

        engine.newGame()
        engine.start()
        
        verify(player).gameEnded(gameCaptor.capture())
        Game game = gameCaptor.getValue()
        assertEquals(GameResult.PlayerWin, game.gameResult())
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
        def engine = getEngine(cardSrc)
        
        def player = new StubPlayer(100)
        player.setMoves([Move.Hit, Move.Stand])
        
        engine.addPlayer(player)
        engine.newGame()
        
        engine.start()
        
        player.res = GameResult.PlayerWin
        assertEquals(110, player.getMoney())        
    }
    
    @Test
    void dealerWins() {
        def cardSrc = getCardSource([Card.TEN, Card.SIX, Card.TWO, Card.TEN, Card.SEVEN, Card.FOUR])
        def engine = getEngine(cardSrc)
        
        def player = new StubPlayer(100)
        player.setMoves([Move.Hit, Move.Stand])
        engine.addPlayer(player)

        engine.newGame()
        engine.start()
        
        player.res = GameResult.DealerWin
        assertEquals(90, player.getMoney())
    }

    @Test
    void push() {
        def cardSrc = getCardSource([Card.TEN, Card.SEVEN, Card.SEVEN, Card.TEN])
        def engine = getEngine(cardSrc)
        
        def player = new StubPlayer(100)
        
        engine.addPlayer(player)

        engine.newGame()
        
        player.setMoves([Move.Stand])
        engine.start()
        
        player.res = GameResult.Push
        assertEquals(100, player.getMoney())
    }

    @Test
    void getDealerCards() {
        def cardSrc = getCardSource([Card.TEN, Card.SIX, Card.TWO, Card.ACE])
        def engine = getEngine(cardSrc)
        
        def player = mock(Player.class)
        engine.addPlayer(player)
        when(player.move(any(), any(), any())).thenReturn(Move.Stand)

        engine.newGame()
        
        def card = engine.getDealerUpCard()
        assertEquals(Card.SIX, card)
    }
    
    @Test
    void dontAskStandingForMove() {
        def cardSrc = getCardSource([Card.TEN, Card.SIX, Card.SIX, Card.THREE, Card.TEN])
        def engine = getEngine(cardSrc)
        
        def player = mock(Player.class)
        
        engine.addPlayer(player)
        when(player.move(any(), any(), any())).thenReturn(Move.Stand)

        engine.newGame()
        
        engine.start();
        
        verify(player, times(1)).move(any(), any(), any())        
    }
    
    @Test
    void doubleDown() {
        def cardSrc = getCardSource([Card.NINE, Card.FIVE, Card.SEVEN, Card.TEN, Card.TWO, Card.TWO])
        def engine = getEngine(cardSrc)
        
        def player = mock(Player.class)
        when(player.bet()).thenReturn(10)
        when(player.move(any(), any(), any())).thenReturn(Move.Double)
        
        engine.addPlayer(player)

        engine.newGame()
        engine.start()
        
        ArgumentCaptor<Game> gameCaptor = ArgumentCaptor.forClass(Game.class);
        verify(player).gameEnded(gameCaptor.capture())
        Game game = gameCaptor.getValue()
        assertEquals(GameResult.PlayerWin, game.gameResult())

        verify(player, never()).addMoney(10)
        verify(player, times(1)).move(any(), any(), any())
    }

    @Test
    void insuranceDealerHasBlackjack() {
        def cardSrc = getCardSource([Card.NINE, Card.ACE, Card.SEVEN, Card.TEN, Card.TWO, Card.TWO])
        def engine = getEngine(cardSrc)
        
        def player = mock(Player.class)
        when(player.bet()).thenReturn(10)
		when(player.insuranceBet()).thenReturn(4)
        
        engine.addPlayer(player)

        engine.newGame()
        engine.start()
        
        verify(player).addMoney(-10)
		verify(player).addMoney(8)
        verify(player, times(1)).insuranceBet()
    }
	
    @Test
    void insuranceDealerHasNotBlackjack() {
        def cardSrc = getCardSource([Card.NINE, Card.ACE, Card.TEN, Card.SEVEN, Card.TWO, Card.TWO])
        def engine = getEngine(cardSrc)
        
        def player = mock(Player.class)
        when(player.bet()).thenReturn(10)
		when(player.insuranceBet()).thenReturn(5)
        when(player.move(any(), any(), any())).thenReturn(Move.Stand)
		
        engine.addPlayer(player)

        engine.newGame()
        engine.start()
        
        verify(player).addMoney(10)
		verify(player).addMoney(-5)
        verify(player, times(1)).insuranceBet()
    }
	
    @Test(expected=IllegalArgumentException.class)
    void insuranceMaxAmount() {
        def cardSrc = getCardSource([Card.NINE, Card.ACE, Card.TEN, Card.SEVEN, Card.TWO, Card.TWO])
        def engine = getEngine(cardSrc)
        
        def player = mock(Player.class)
        when(player.bet()).thenReturn(10)
		when(player.insuranceBet()).thenReturn(6)
        when(player.move(any(), any(), any())).thenReturn(Move.Stand)
		
        engine.addPlayer(player)

        engine.newGame()
        engine.start()
    }

    @Test(expected=IllegalMoveException.class)
    void noSplitAfterInsurance() {
        def cardSrc = getCardSource([Card.ACE, Card.ACE, Card.ACE, Card.SEVEN, Card.TWO, Card.TWO])
        def engine = getEngine(cardSrc)
        
        def player = mock(Player.class)
        when(player.bet()).thenReturn(10)
		when(player.insuranceBet()).thenReturn(3)
        when(player.move(any(), any(), any())).thenReturn(Move.Split)
		
        engine.addPlayer(player)

        engine.newGame()
        engine.start()
    }
	
    @Test(expected=IllegalMoveException.class)
    void disallowedDouble() {
        def cardSrc = getCardSource([Card.TWO, Card.FIVE, Card.SEVEN, Card.TEN, Card.TWO, Card.TWO])
        def engine = getEngine(cardSrc)
        
        def player = mock(Player.class)
        when(player.bet()).thenReturn(10)
        when(player.move(any(), any(), any())).thenReturn(Move.Hit).thenReturn(Move.Double)
        
        engine.addPlayer(player)

        engine.newGame()
        engine.start()
    }
    
    @Test(expected=IllegalMoveException.class)
    void disallowedSplit() {
        def cardSrc = getCardSource([Card.TWO, Card.FIVE, Card.SEVEN, Card.TEN, Card.TWO, Card.TWO])
        def engine = getEngine(cardSrc)
        
        def player = mock(Player.class)
        when(player.bet()).thenReturn(10)
        when(player.move(any(), any(), any())).thenReturn(Move.Hit).thenReturn(Move.Split)
        
        engine.addPlayer(player)

        engine.newGame()
        engine.start()
    }

    @Test
    void splitGame() {
        def cardSrc = getCardSource([Card.TWO, Card.SEVEN, Card.TWO, Card.TEN, Card.TEN, Card.TEN])
        def engine = getEngine(cardSrc)
        
        def player = mock(Player.class)
        when(player.bet()).thenReturn(10)
        when(player.move(any(), any(), any())).thenReturn(Move.Split).thenReturn(Move.Stand).thenReturn(Move.Stand)
        
        engine.addPlayer(player)

        engine.newGame()
        engine.start()
        
        verify(player, times(3)).move(any(), any(), any())
        verify(player, times(2)).addMoney(-10)
    }

    @Test
    void doubleAfterSplit() {
        def cardSrc = getCardSource([Card.TWO, Card.SEVEN, Card.TWO, Card.TEN, Card.TEN, Card.TEN, Card.THREE])
        def engine = getEngine(cardSrc)
        engine.setDoubleAfterSplit(true);
		
        def player = mock(Player.class)
        when(player.bet()).thenReturn(10)
        when(player.move(any(), any(), any())).thenReturn(Move.Split).thenReturn(Move.Stand).thenReturn(Move.Double)
        
        engine.addPlayer(player)

        engine.newGame()
        engine.start()
        
        verify(player, times(3)).move(any(), any(), any())
        verify(player, times(1)).addMoney(-10)
		verify(player, times(1)).addMoney(-20)
    }
	
	@Test
    void doubleAfterSplitForbidden() {
        def cardSrc = getCardSource([Card.TWO, Card.SEVEN, Card.TWO, Card.TEN, Card.TEN, Card.TEN, Card.THREE])
        def engine = getEngine(cardSrc)
        engine.setDoubleAfterSplit(false);
		
        def player = mock(Player.class)
        when(player.bet()).thenReturn(10)
        when(player.move(any(), any(), any())).thenReturn(Move.Split).thenReturn(Move.Stand).thenReturn(Move.Double)
        
        engine.addPlayer(player)

		try {
			engine.newGame()
			engine.start()
			fail("Double should be forbidden after split.");
		} catch (IllegalMoveException ex) {
			
		} 
    }
	
	@Test
    void splitAces() {
        def cardSrc = getCardSource([Card.ACE, Card.ACE, Card.SEVEN, Card.TEN], [Card.TEN, Card.NINE])
        def engine = getEngine(cardSrc)
        
        def player = mock(Player.class)
        when(player.bet()).thenReturn(10)
        when(player.move(any(), any(), any())).thenReturn(Move.Split)
        
        engine.addPlayer(player)

        engine.newGame()
        engine.start()
        
        verify(player, times(1)).move(any(), any(), any())
        verify(player, times(1)).addMoney(-10)
		verify(player, times(1)).addMoney(10)
    }
	
	@Test
    void splitAcesDealerHasBlackjack() {
        def cardSrc = getCardSourceNoPeek([Card.ACE, Card.ACE, Card.SEVEN, Card.TEN], [Card.TEN, Card.ACE])
        def engine = getEngine(cardSrc)
		engine.setPeek(false)
        
        def player = mock(Player.class)
        when(player.bet()).thenReturn(10)
        when(player.move(any(), any(), any())).thenReturn(Move.Split)
        
        engine.addPlayer(player)

        engine.newGame()
        engine.start()
        
        verify(player, times(1)).move(any(), any(), any())
        verify(player, times(2)).addMoney(-10)
    }
	
	@Test
    void splitTensDealerHasBlackjack() {
        def cardSrc = getCardSourceNoPeek([Card.TEN, Card.QUEEN, Card.ACE, Card.ACE], [Card.TEN, Card.ACE])
        def engine = getEngine(cardSrc)
		engine.setPeek(false)
        
        def player = mock(Player.class)
        when(player.bet()).thenReturn(10)
        when(player.move(any(), any(), any())).thenReturn(Move.Split).thenReturn(Move.Stand).thenReturn(Move.Stand)
        
        engine.addPlayer(player)

        engine.newGame()
        engine.start()
        
        verify(player, times(3)).move(any(), any(), any())
        verify(player, times(2)).addMoney(-10)
    }
	
	@Test
	void dontAllowSplitOfDifferentCards() {
		def player = mock(Player.class)
		ArgumentCaptor<Game> playerCaptor = ArgumentCaptor.forClass(Player.class);		
		when(player.move(any(), any(), playerCaptor.capture())).thenReturn(Move.Stand)
		
		def engine = getEngine([Card.NINE, Card.TWO, Card.TEN, Card.TEN, Card.TEN])
		engine.addPlayer(player)

		def game = engine.newGame()
		engine.start()

		assertNull(playerCaptor.getValue().find { it == Move.Split} )

	}
	
    @Test
    void twoGames() {
        def cardSrc = getCardSource([Card.TEN, Card.SEVEN, Card.SEVEN, Card.TEN, Card.TEN, Card.EIGHT, Card.SEVEN, Card.NINE])
        def engine = getEngine(cardSrc)
        
        def player = new StubPlayer(100)
        
        engine.addPlayer(player)

        engine.newGame()
        
        player.setMoves([Move.Stand])
        engine.start()
        
        assertEquals(Card.SEVEN, engine.dealerUpCard)
        
        player.res = GameResult.Push
        assertEquals(100, player.getMoney())
        
        player.setMoves([Move.Stand])
        engine.newGame()
        engine.start()
        
        assertEquals(Card.EIGHT, engine.dealerUpCard)
        player.res = GameResult.Push
    }
    
    Engine getEngine(def cards) {
        getEngine(getCardSource(cards))
    }
    
    Engine getEngine(CardSource cardsrc) {
        def engine = new Engine(cardsrc)
		engine.setResplitAces(true)
		engine.setDoubleRules(DoubleOn.All)
		return engine
    }
}
