/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack.simulation;

import blackjack.engine.Card;
import blackjack.engine.CardShuffler;
import java.util.*;

/**
 *
 * @author mbarnas
 */
public class SimulationCardShuffler extends CardShuffler {
	private final List<Card> firstCards;
    private int usedCards = 0;

	public SimulationCardShuffler(int decks, Card... firstCards) {
		this.firstCards = new ArrayList<Card>(Arrays.asList(firstCards));
	}
	
    @Override
    public Card next() {
		if (usedCards < firstCards.size()) {
            Card c = firstCards.get(usedCards++);
			return c;
		}
        Card next = null;
        do {
            next = super.next();
            if (firstCards.contains(next))
                firstCards.remove(next);
                continue;
        } while (next == null);
            
        return next;
    }
    
    @Override
    public void newGame() {
    }
}
