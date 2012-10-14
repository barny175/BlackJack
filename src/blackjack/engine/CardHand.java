/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack.engine;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mbarnas
 */
public class CardHand {
    public static final int BLACKJACK = 21;
	private List<Card> cards = new ArrayList<Card>(6);
	
	public void addCard(Card card) {
		this.cards.add(card);
	}
	
	public int hardSum() {
		int sum = 0;
		for (Card c : cards) {
			sum += c.getValue();
		}
		
		return sum;
	}
	
	public int softSum() {
		int sum = 0;
		boolean ace = false;
		for (Card c : cards) {
			if (c == Card.ACE && !ace) {
				sum += 11;
				ace = true;
			}
			else {
				sum += c.getValue();
			}
		}
		
		return sum;
	}

    public List<Card> getCards() {
        return cards;
    }

    public void reset() {
        this.cards.clear();
    }
    
    public boolean isBlackJack() {
        if (softSum() == BLACKJACK && cards.size() == 2)
            return true;
        
        return false;
    }
}
