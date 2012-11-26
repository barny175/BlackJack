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
    public static final int CARDS_IN_DECK = 13;
    
    protected int decks;
    private Random rand;
    protected final int allCards;
    private List<ShuffleObserver> observers = Lists.newArrayList();
	private List<Card> cards;
	private int currentCard;

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
        
		shuffleCards();
    }

    public int cardsDrawn() {
        return currentCard;
    }
    
    @Override
    public void newGame() {
    }
    
    @Override
    public Card next() throws LastCardException {
        if (currentCard >= allCards)
            throw new LastCardException();
        
        return cards.get(currentCard++);
    }

    private Card numToCard(int card) {
        return Card.values()[card];
    }

    @Override
    public void registerObserver(ShuffleObserver observer) {
        this.observers.add(observer);
    }

    protected void notifyObservers() {
        for (ShuffleObserver o : this.observers) {
			o.shuffling();
		}
    }

	public void shuffle() {
		shuffleCards();
		notifyObservers();
	}

	private void shuffleCards() {
		this.cards = new ArrayList<Card>(allCards);
		
		for (int i = 0; i  < allCards;i++) {
            int card = i % CARDS_IN_DECK;
			this.cards.add(numToCard(card));
		}
		
		Collections.shuffle(cards, rand);
		this.currentCard = 0;
	}
}
