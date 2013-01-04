/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
class BasicRulesTest {
	@Test
	void splitOnlySameCards() {
		def player = mock(Player.class)
		def engine = mock(Engine.class)
		
		def game = new Game(engine, player)
		game.addPlayerCard(Card.THREE)
		game.addPlayerCard(Card.THREE)
		game.setGameState(GameState.FirstDeal)

		def rules = new BasicRules()		
		def allowedMoves = rules.getAllowedMoves(game)
		assertTrue(allowedMoves.contains(Move.Split))
	}
	
	@Test
	void splitDifferentFigures() {
		def player = mock(Player.class)
		def engine = mock(Engine.class)
		
		def game = new Game(engine, player)
		game.addPlayerCard(Card.TEN)
		game.addPlayerCard(Card.JACK)
		game.setGameState(GameState.FirstDeal)

		def rules = new BasicRules()		
		def allowedMoves = rules.getAllowedMoves(game)
		assertTrue(allowedMoves.contains(Move.Split))
	}
	
	@Test
	void dontAllowSplitOfDifferentCards() {
		def player = mock(Player.class)
		def engine = mock(Engine.class)
		
		def game = new Game(engine, player)
		game.addPlayerCard(Card.TWO)
		game.addPlayerCard(Card.THREE)
		game.setGameState(GameState.FirstDeal)

		def rules = new BasicSplitRules()		
		assertFalse(rules.isSplitPossible(game))
	}
}

