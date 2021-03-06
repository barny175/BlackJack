/*
 * (C) 2012 Kerio Technologies s.r.o.
 */
package blackjack.engine;

import com.google.inject.ImplementedBy;

/**
 *
 * @author mbarnas
 */
@ImplementedBy(CardShuffler.class)
public interface CardSource {
    Card next() throws LastCardException;

    void newGame();
    
    void registerObserver(ShuffleObserver observer);
    
}
