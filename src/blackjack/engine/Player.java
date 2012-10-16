/*
 * (C) 2012 Kerio Technologies s.r.o.
 */
package blackjack.engine;

import java.util.List;

/**
 *
 * @author mbarnas
 */
public interface Player extends CardHolder {
    int getBet();
    
    void addMoney(int amount);
    
    void result(GameResult result);

    void setGame(Game game);
}