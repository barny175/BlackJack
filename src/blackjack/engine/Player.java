/*
 * (C) 2012 Kerio Technologies s.r.o.
 */
package blackjack.engine;

/**
 *
 * @author mbarnas
 */
public interface Player {
    int bet();

    void addMoney(int amount);
    
    void result(GameState result);

    Move move(CardHand cards, Card dealerUpCard);
}
