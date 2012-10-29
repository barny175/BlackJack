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
    
    void gameEnded(Game game);

    Move move(CardHand cards, Card dealerUpCard);
}
