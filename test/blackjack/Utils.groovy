/*
 * (C) 2012 Kerio Technologies s.r.o.
 */

package blackjack

import blackjack.engine.CardHand

/**
 *
 * @author mbarnas
 */
class Utils {
	public static CardHand getCardHand(def cards) {
        def ch = new CardHand()
        cards.each { ch.addCard(it) }
        return ch
    }
}

