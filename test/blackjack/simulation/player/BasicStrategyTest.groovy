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
        ["A2", "H", "H", "H", "D", "D", "H", "H", "H", "H", "H"],
        ["A3", "H", "H", "H", "D", "D", "H", "H", "H", "H", "H"],
        ["A4", "H", "H", "D", "D", "D", "H", "H", "H", "H", "H"],
        ["A5", "H", "H", "D", "D", "D", "H", "H", "H", "H", "H"],
        ["A6", "H", "D", "D", "D", "D", "H", "H", "H", "H", "H"],
        ["A7", "S", "DS", "DS", "DS", "DS", "S", "S", "H", "H", "H"],
        ["A8", "S", "S", "S", "S", "S", "S", "S", "S", "S", "S"],
        ["A9", "S", "S", "S", "S", "S", "S", "S", "S", "S", "S"],
        ["22", "P", "P", "P", "P", "P", "P", "H", "H", "H", "H"],
        ["33", "P", "P", "P", "P", "P", "P", "H", "H", "H", "H"],
        ["44", "H", "H", "H", "P", "P", "H", "H", "H", "H", "H"],
        ["55", "D", "D", "D", "D", "D", "D", "D", "D", "H", "H"],
        ["66", "P", "P", "P", "P", "P", "H", "H", "H", "H", "H"],
        ["77", "P", "P", "P", "P", "P", "P", "H", "H", "H", "H"],
        ["88", "P", "P", "P", "P", "P", "P", "P", "P", "P", "P"],
        ["99", "P", "P", "P", "P", "P", "S", "P", "P", "S", "S"],
        ["TT", "S", "S", "S", "S", "S", "S", "S", "S", "S", "S"],
        ["AA", "P", "P", "P", "P", "P", "P", "P", "P", "P", "P"]
    ]
    
    @Test
    void basicStrategyTest() {
        for (Card c : Card.ACE..Card.KING) {
            for (Card c2: Card.TWO..Card.NINE) {
                def player = new BasicStrategyPlayer(100)
                player.setRules(new BasicRules())
                
                CardHand cards = new CardHand()
                cards.addCard(c)
                cards.addCard(c2)
                
                for (Card dealerCard : Card.ACE..Card.KING) {
                    def move = player.move(cards, dealerCard, [Move.Double, Move.Hit, Move.Stand, Move.Split] as Set)
                    def expected = getBasicStrategyMove(cards, dealerCard)
                    assertEquals("Player's cards: ${c}, ${c2}, dealers card ${dealerCard}", expected, move)
                }
            }
        }
    }

    @Test
    void threeCardTest() {
        def rules = new BasicRules()
        for (Card c : Card.ACE..Card.KING) {
            for (Card c2: Card.TWO..Card.NINE) {
                for (Card c3: Card.TWO..Card.NINE) {
                    def player = new BasicStrategyPlayer(100)
                    player.setRules(rules)
                
                    CardHand cards = new CardHand()
                    cards.addCard(c)
                    cards.addCard(c2)
                    cards.addCard(c3)
                
                    for (Card dealerCard : Card.ACE..Card.KING) {
                        def game = new Game(mock(Engine.class), player)
                        game.gameState = GameState.PlayersGame
                        def move = player.move(cards, dealerCard, rules.getAllowedMoves(game))
                        def expected = getBasicStrategyMove(cards, dealerCard)
                        assertEquals("Player's cards: ${c}, ${c2}, ${c3}, dealers card ${dealerCard}", expected, move)
                    }
                }
            }
        }
    }
    
    private Move getBasicStrategyMove(playerCards, dealerCard) {
        int hardSum = playerCards.hardSum()
        
        def toSearch
        
        if (playerCards.aces() == 0) {
            return noAce(playerCards, dealerCard)
        } else {
            def sum = hardSum - Card.ACE.getValue()
            if (sum >= 10)
                return noAce(playerCards, dealerCard)
            else { 
                if (sum == 1)
                sum = "A"
            
                toSearch = "A${sum}"
            }
        }
        
        def row = strategy.find { it[0] == toSearch }
        
        def movePos = dealerCard.getValue() - 1
        letterToMove(playerCards, row[movePos == 0 ? -1 : movePos])
    }
    
    private Move noAce(playerCards, dealerCard) {
        int hardSum = playerCards.hardSum()
        
        def toSearch
        
        if (playerCards.count() == 2 && playerCards.getCards().get(0).getValue() == playerCards.getCards().get(1).getValue()) {
            def card = playerCards.getCards().get(0).getValue()
            toSearch = card == Card.TEN.getValue() ? "TT": "${card}${card}"
        } else {            
            
            if (hardSum < 7)
            return Move.Hit
                
            if (hardSum > 17)
            return Move.Stand
                
            toSearch = hardSum
        }
        def row = strategy.find { it[0] == toSearch }
        
        def movePos = dealerCard.getValue() - 1
        letterToMove(playerCards, row[movePos == 0 ? -1 : movePos])
    }
    
    private Move letterToMove(CardHand playerCards, String letter) {
        switch (letter) {
            case "H":
            return Move.Hit
            case "D":
                if (playerCards.count() == 2)
                    return Move.Double
                else
                    return Move.Hit
            case "S":
            return Move.Stand
            case "DS":
                if (playerCards.count() == 2)
                    return Move.Double
                else
                    return Move.Stand
            case "P":
            return Move.Split
        }
    }
}

