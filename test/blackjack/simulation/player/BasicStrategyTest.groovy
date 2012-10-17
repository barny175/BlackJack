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
class BasicStrategyTest {
    def strategy = [[7, "H", "H", "H", "H", "H", "H", "H", "H", "H", "H"], 
        [8, "H", "H", "H", "H", "H", "H", "H", "H", "H", "H"],
        [9, "H", "D", "D", "D", "D", "H", "H", "H", "H", "H"],
        [10, "D", "D", "D", "D", "D", "D", "D", "D", "H", "H"],
        [11, "D", "D", "D", "D", "D", "D", "D", "D", "D", "H"],
        [12, "H", "H", "S", "S", "S", "H", "H", "H", "H", "H"],
        [13, "S", "S", "S", "S", "S", "H", "H", "H", "H", "H"],
        [14, "S", "S", "S", "S", "S", "H", "H", "H", "H", "H"],
        [15, "S", "S", "S", "S", "S", "H", "H", "H", "H", "H"],
        [16, "S", "S", "S", "S", "S", "H", "H", "H", "H", "H"],
        [17, "S", "S", "S", "S", "S", "S", "S", "S", "S", "S"],
        [A,2, "H", "H", "H", "D", "D", "H", "H", "H", "H", "H"],
        [A,3, "H", "H", "H", "D", "D", "H", "H", "H", "H", "H"],
        [A,4, "H", "H", "D", "D", "D", "H", "H", "H", "H", "H"],
        [A,5, "H", "H", "D", "D", "D", "H", "H", "H", "H", "H"],
        [A,6, "H", "D", "D", "D", "D", "H", "H", "H", "H", "H"],
        [A,7, "S", "DS", "DS", "DS", "DS", "S", "S", "H", "H", "H"],
        [A,8, "S", "S", "S", "S", "S", "S", "S", "S", "S", "S"],
        [A,9, "S", "S", "S", "S", "S", "S", "S", "S", "S", "S"],
//        [2,2, "P", "P", "P", "P", "P", "P", "H", "H", "H", "H"],
//        [3,3, "P", "P", "P", "P", "P", "P", "H", "H", "H", "H"],
//        [4,4, "H", "H", "H", "P", "P", "H", "H", "H", "H", "H"],
//        [5,5, "D", "D", "D", "D", "D", "D", "D", "D", "H", "H"],
//        [6,6, "P", "P", "P", "P", "P", "H", "H", "H", "H", "H"],
//        [7,7, "P", "P", "P", "P", "P", "P", "H", "H", "H", "H"],
//        [8,8, "P", "P", "P", "P", "P", "P", "P", "P", "P", "P"],
//        [9,9, "P", "P", "P", "P", "P", "S", "P", "P", "S", "S"],
//        [T,T, "S", "S", "S", "S", "S", "S", "S", "S", "S", "S"],
//        [A,A, "P", "P", "P", "P", "P", "P", "P", "P", "P", "P"]
    ]
    
    @Test
    void basicStrategyTest() {
        for (Card c : Card.ACE..Card.KING) {
            for (Card c2: Card.ACE..Card.KING) {
                player = new BasicStrategyPlayer(1000)
                player.addCard()
                
                for (Card dealerCard : Card.ACE..Card.KING) {
                    def game = mock(Game.class)
                    when(game.getDealerUpCard()).thenReturn(dealerCard)
                    
                    def player = new BasicStrategyPlayer(10)
                    player.setGame(game)
                    
                    def move = player.move()
                    assertEquals("", getBasicStrategyMove(player.getCards, dealerCard), move) // todo
                }
            }
        }
    }
    
    private Move getBasicStrategyMove(playerCards, dealerCard) {
        int hardSum = playerCards.hardSum()
        def row = strategy.find {it[0] == hardSum}
        row[(dealerCard.ordinal() - 1) % Card.values[].length]
    }
}

