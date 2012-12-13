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

    private List<Card> cards = new ArrayList<Card>(6);
    private int aceCount;
    private int hardSum;
	private boolean splitted = false;

	public boolean isSplitted() {
		return splitted;
	}

    public void addCard(Card card) {
        this.cards.add(card);
        if (card == Card.ACE)
            aceCount++;
        
        hardSum += card.getValue();
    }

    public int hardSum() {
        return hardSum;
    }

    public int softSum() {
        int sum = 0;

        for (Card c : cards) {
            if (c != Card.ACE) {
                sum += c.getValue();
            }
        }

        if (aceCount > 0) {
            sum += (aceCount - 1) * Card.ACE.getValue();
            if (sum + Card.ACE.getSoftValue() <= 21) {
                sum += Card.ACE.getSoftValue();
            } else {
                sum += Card.ACE.getValue();
            }
        }

        return sum;
    }

    public List<Card> getCards() {
        return cards;
    }
	
	public Card get(int index) {
		return cards.get(index);
	}

    public void reset() {
        this.cards.clear();
        aceCount = 0;
        hardSum = 0;
    }

    private boolean isBlackJack() {
        return (softSum() == Rules.BLACKJACK && cards.size() == 2);
    }

    @Override
    public String toString() {
        return String.format("[%d, %b]", softSum(), isBlackJack());
    }

    public int aces() {
        return aceCount; 
    }

    public int count() {
        return cards.size();
    }
	
	CardHand[] split() {
		if (this.cards.size() != 2)
			throw new RuntimeException("Illegal split.");
		
		CardHand[] splitted = new CardHand[2];
		final CardHand ch = new CardHand();
		ch.addCard(this.cards.get(0));
		ch.splitted = true;
		splitted[0] = ch;
		
		final CardHand ch2 = new CardHand();
		ch2.addCard(this.cards.get(1));
		ch2.splitted = true;
		splitted[1] = ch2;
		
		return splitted;
	}

	void setSplitted(boolean b) {
		this.splitted = b;
	}
}
