/*
 * (C) 2012 Kerio Technologies s.r.o.
 */
package blackjack.engine;

/**
 *
 * @author mbarnas
 */
public interface Game {
    Card dealerUpCard();
	
	CardHand playerCards();
	
	GameState gameState();
	
	void setGameState(GameState gameState);

	Player getPlayer();
}
