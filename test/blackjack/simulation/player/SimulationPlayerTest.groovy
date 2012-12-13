/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package blackjack.simulation.player


import org.junit.Test
import static org.junit.Assert.*
import blackjack.engine.*
import static org.mockito.Mockito.*
import blackjack.engine.rules.BasicRules
/**
 *
 * @author mbarnas
 */
class SimulationPlayerTest {
	@Test
    void allowedMove() {
		
        def player = new SimulationPlayer([Card.TWO, Card.TWO, Card.FIVE] as Card[], "Stand", 100)
		player.setRules(new BasicRules())
		
		CardHand cards = new CardHand()
		cards.addCard(Card.TWO)
		cards.addCard(Card.FIVE)
		
		def move = player.move(cards, Card.TWO, [Move.Double, Move.Hit, Move.Stand, Move.Split] as Set)
		def expected = Move.Stand
		assertEquals(expected, move)
		
		def basicPlayer = new BasicStrategyPlayer(100)
		basicPlayer.setRules(new BasicRules())
		assertEquals(Move.Hit, basicPlayer.move(cards, Card.TWO, [Move.Double, Move.Hit, Move.Stand, Move.Split] as Set))
    }
}

