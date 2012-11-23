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
public class EveryGameCardShuffler extends CardShuffler {

	@Override
	public void newGame() {
		this.shuffle();
	}		
}
