/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack.engine.shufflers;

import blackjack.engine.CardShuffler;

/**
 *
 * @author mbarnas
 */
public class TwoThirdsShuffler extends CardShuffler {

	public TwoThirdsShuffler(int decks) {
		super(decks);
	}

	@Override
	public void shuffle() {
		if (this.used.size() >= this.allCards * 2 / 3)
			super.shuffle();
	}
}
