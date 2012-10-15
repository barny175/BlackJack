/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack.simulation;

import blackjack.engine.Card;
import blackjack.engine.CardShuffler;
import java.util.LinkedList;

/**
 *
 * @author mbarnas
 */
public class SimulationCardShuffler extends CardShuffler {
	private final Card[] firstCards;
	private int cardsUsed;

	public SimulationCardShuffler(int decks, Card... firstCards) {
		super(decks);
		this.firstCards = firstCards;
	}
	
	
    @Override
    public Card next() {
		if (cardsUsed < firstCards.length) {
			Card c = firstCards[cardsUsed++];
			this.used.add(c.ordinal());
			return c;
		}
		
		return super.next();
	}
}
