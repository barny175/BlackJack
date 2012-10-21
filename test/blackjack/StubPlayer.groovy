/*
 * (C) 2012 Kerio Technologies s.r.o.
 */

package blackjack

import blackjack.engine.*
import java.util.List

/**
 *
 * @author mbarnas
 */
class StubPlayer implements Player {
    def cards = new CardHand()
    GameResult res
    int money
    def move = [Move.Hit]
    
    StubPlayer(int startMoney) {
        money = startMoney
    }
    
    void addCard(Card card) {
        cards.addCard(card)
    }
    
    CardHand getCards() {
        return cards
    }
    
    int bet() {
        return 10
    }
    
    void addMoney(int amount) {
        money += amount
    }
    
    void result(GameResult res) {
        this.res = res
    }

    void setMoves(m) {
        move = m
    }
    
    Move move() {
        return move.remove(0)
    }
    
    int getMoney() {
        return money
    }
    
    void reset() {
        cards.reset()
    }
    
    void setGame(Game game) {
        
    }
}
