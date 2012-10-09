/*
 * (C) 2012 Kerio Technologies s.r.o.
 */
package blackjack.engine;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 *
 * @author mbarnas
 */
public class CardShuffler implements CardSource {
    private int decks;
    private Random rand;
    private final int CARDS_IN_DECK = 13;
    private Set<Integer> used;
    
    public CardShuffler(int decks) {
        this.decks = decks;
        
        this.rand = new Random(System.currentTimeMillis());
        this.used = new HashSet<Integer>(decks * CARDS_IN_DECK);
    }
    
    @Override
    public Card next() {
        final int allCards = 4 * CARDS_IN_DECK * decks;
        
        if (used.size() == allCards)
            return null;
        
        do {
            int nextInt = this.rand.nextInt(allCards);
            if (used.contains(nextInt))
                continue;
            
            int card = nextInt % CARDS_IN_DECK;
            used.add(nextInt);
            return numToCard(card);
        } while (true);
    }

    private Card numToCard(int card) {
        switch (card) {
            case 0:
                return Card.ACE;
            case 10:
                return Card.JACK;
            case 11:
                return Card.QUEEN;
            case 12:
                return Card.KING;
            case 9:
                return Card.TEN;
            default:
                return Card.values()[card];
        }
                
    }
}
