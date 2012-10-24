/*
 * (C) 2012 Kerio Technologies s.r.o.
 */
package blackjack.engine;

/**
 *
 * @author mbarnas
 */
public interface Player extends GameParticipant {
    int bet();

    void addMoney(int amount);
    
    void result(GameState result);

    void addGame(Game game);
}
