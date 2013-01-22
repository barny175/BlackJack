/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
class SimulationPlayerTest {
	@Test
    void allowedMove() {
		
        def player = new SimulationPlayer([Card.TWO, Card.FIVE] as Card[], Card.TWO, "Stand", 100)
		
		CardHand cards = new CardHand()
		cards.addCard(Card.TWO)
		cards.addCard(Card.FIVE)
		
		def move = player.move(cards, Card.TWO, [Move.Double, Move.Hit, Move.Stand] as Set)
		def expected = Move.Stand
		assertEquals(expected, move)
		
		def basicPlayer = new BasicStrategyPlayer(100)
		assertEquals(Move.Hit, basicPlayer.move(cards, Card.TWO, [Move.Double, Move.Hit, Move.Stand] as Set))
    }
	
	@Test
    void disallowedMove() {
        def player = new SimulationPlayer([Card.TWO, Card.FIVE] as Card[], Card.TWO, "Split", 100)
		
		CardHand cards = new CardHand()
		cards.addCard(Card.TWO)
		cards.addCard(Card.FIVE)
		
		def basicPlayer = new BasicStrategyPlayer(100)
		
		def allowedMoves = [Move.Double, Move.Hit, Move.Stand] as Set
		
		def move = player.move(cards, Card.TWO, allowedMoves)
		def expected = basicPlayer.move(cards, Card.TWO, allowedMoves)
		assertEquals(expected, move)
    }
}

