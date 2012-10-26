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
    private int aceCount;
    private int hardSum;

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

    public void reset() {
        this.cards.clear();
        aceCount = 0;
        hardSum = 0;
    }

    public boolean isBlackJack() {
        if (softSum() == BLACKJACK && cards.size() == 2) {
            return true;
        }

        return false;
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
    
    public Card get(int index) {
        return cards.get(index);
    }
}
