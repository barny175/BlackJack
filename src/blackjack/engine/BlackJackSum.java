/*
 * (C) 2012 Kerio Technologies s.r.o.
 */
package blackjack.engine;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mbarnas
 */
public final class BlackJackSum {

    public static class Sum {
        public int sum;
        public boolean isBlackJack;

        public Sum(int sum, boolean isBlackJack) {
            this.sum = sum;
            this.isBlackJack = isBlackJack;
        }

        @Override
        public String toString() {
            return String.format("[%d, %b]", sum, isBlackJack);
        }
    }
    
    public static Sum sum(List<Card> cards) {
        int sum = innerSum(cards);
        
        if (sum == 21 && cards.size() == 2) 
            return new Sum(21, true);
        
        return new Sum(sum, false);
    }
    
    private static int innerSum(List<Card> cards) {
        int sumNoAces = 0;
        int aceCount = 0;

        for (Card c : cards) {
            if (c == Card.ACE)
                aceCount++;
            else
                sumNoAces += c.getValue();
        }
        
        return aceSum(sumNoAces, aceCount);
    }

    private static int aceSum(int sum, int aceCount) {
        if (aceCount-- == 0)
            return sum;
        
        int sum1 = aceSum(sum + 1, aceCount);
        int sum11 = aceSum(sum + 11, aceCount);
        
        if (sum11 > 21)
            return sum1;
        
        return sum11;
    }    
}
