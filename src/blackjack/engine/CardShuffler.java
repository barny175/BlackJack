/*
 * (C) 2012 Kerio Technologies s.r.o.
 */
package blackjack.engine;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import java.util.*;

/**
 *
 * @author mbarnas
 */
public class CardShuffler implements CardSource {
	public static final String DECKS = "decks";
	public static final String SEED = "seed";
	
    protected int decks;
    private Random rand;
    public final int CARDS_IN_DECK = 13;
    protected Set<Integer> used;
	protected final int allCards;
    private List<ShuffleObserver> observers = Lists.newArrayList();

    public CardShuffler() {
        this(1);
    }
    
    public CardShuffler(@Named(DECKS) int decks) {
        this(decks, System.currentTimeMillis());
    }

	@Inject
    public CardShuffler(@Named(DECKS)int decks, @Named(SEED)long randSeed) {
        this.decks = decks;
        this.allCards = 4 * CARDS_IN_DECK * decks;
        this.rand = new Random(randSeed);
        this.used = new HashSet<Integer>(decks * CARDS_IN_DECK);
    }
    
    @Override
    public void newGame() {
    }
    
    @Override
    public Card next() {
        if (used.size() == allCards)
            shuffle();
        
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
        return Card.values()[card];
    }

    @Override
    public void registerObserver(ShuffleObserver observer) {
        this.observers.add(observer);
    }

    protected void notifyObservers() {
        for (ShuffleObserver o : this.observers)
            o.shuffling();
    }

	private void shuffle() {
		used.clear();
		notifyObservers();
	}
}
