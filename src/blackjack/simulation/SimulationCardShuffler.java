/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack.simulation;

import blackjack.engine.Card;
import blackjack.engine.CardShuffler;
import blackjack.engine.CardSource;
import blackjack.engine.ShuffleObserver;
import com.google.common.collect.Lists;
import com.google.inject.name.Named;
import java.util.*;

/**
 *
 * @author mbarnas
 */
public class SimulationCardShuffler implements CardSource {

	protected int decks;
	private Random rand;
	public final int CARDS_IN_DECK = 13;
	protected Set<Integer> used;
	protected final int allCards;
	private List<ShuffleObserver> observers = Lists.newArrayList();
	private Card[] firstCards;
	private int cardsUsed;
	private Card[] missingCards;

	public SimulationCardShuffler(int decks) {
		this.decks = decks;
		this.allCards = 4 * CARDS_IN_DECK * decks;
		this.rand = new Random(System.currentTimeMillis());
		this.used = new HashSet<Integer>(decks * CARDS_IN_DECK);
		this.firstCards = new Card[3];
	}
	
	public SimulationCardShuffler(@Named(CardShuffler.DECKS) int decks, Card... firstCards) {
		this(decks);
		
		this.firstCards = firstCards;
	}
	
	public SimulationCardShuffler withPlayerCards(Card firstCard, Card secondCard) {
		this.firstCards[0] = firstCard;
		this.firstCards[2] = secondCard;
		return this;
	}

	public SimulationCardShuffler withDealersCard(Card dealersCard) {
		this.firstCards[1] = dealersCard;
		return this;
	}
	
	/**
	 * given cards are not in deck
	 * @param missingCards
	 * @return 
	 */
	public SimulationCardShuffler withCardsMissing(Card... missingCards) {
		this.missingCards = missingCards;
		
		useCards(missingCards);
		
		return this;
	}
	
	@Override
	public void newGame() {
		shuffle();
	}

	@Override
	public Card next() {
		if (used.size() == allCards) {
			return null;
		}

		if (cardsUsed < firstCards.length) {
			Card c = firstCards[cardsUsed++];
			if (c != null) {
				useCard(c);
				return c;
			}
		}

		do {
			int nextInt = this.rand.nextInt(allCards);
			if (used.contains(nextInt)) {
				continue;
			}

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
		for (ShuffleObserver o : this.observers) {
			o.shuffling();
		}
	}

	private void shuffle() {
		used.clear();
		useCards(missingCards);
		cardsUsed = 0;
		notifyObservers();
	}

	private void useCard(Card c) {
		for (int i = c.ordinal(); i < this.decks * 4 * Card.values().length; i += Card.values().length) {
			if (!this.used.contains(i)) {
				this.used.add(i);
				return;
			}
		}
		throw new IllegalStateException("Two many predefined cards.");
	}

	private void useCards(Card[] missingCards) {
		if (missingCards != null) {
			for (Card c : missingCards) {
				useCard(c);
			}
		}
	}
}
