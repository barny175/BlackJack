/*
 * (C) 2012 Kerio Technologies s.r.o.
 */
package blackjack.engine;

/**
 *
 * @author mbarnas
 */
public interface ShuffleObserver {
    void shuffling();
	
	void cardDrawn(Card card);
}
