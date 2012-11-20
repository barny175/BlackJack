/*
 * (C) 2012 Kerio Technologies s.r.o.
 */
package blackjack.engine;

import java.util.Set;

/**
 *
 * @author mbarnas
 */
public interface Player {
	void newGame(Game game);
	
    int bet();

    void addMoney(int amount);
    
    void gameEnded(Game game);

    Move move(CardHand cards, Card dealerUpCard, Set<Move> allowedMoves);
}
