/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack.engine.shufflers;

import blackjack.engine.CardShuffler;
import com.google.inject.Inject;
import com.google.inject.name.Named;

/**
 *
 * @author mbarnas
 */
public class TwoThirdsShuffler extends CardShuffler {
    @Inject
	public TwoThirdsShuffler(@Named(DECKS) int decks, long randSeed) {
		super(decks, randSeed);
	}

	@Override
	public void newGame() {
		if (cardsDrawn() >= this.allCards * 2 / 3)
			super.shuffle();
	}
}
