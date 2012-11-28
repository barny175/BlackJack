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
import com.google.inject.Inject;
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
	private final Card[] firstCards;
	private int cardsUsed;

	public SimulationCardShuffler(@Named(CardShuffler.DECKS) int decks, Card... firstCards) {
		this.decks = decks;
		this.firstCards = firstCards;
		this.allCards = 4 * CARDS_IN_DECK * decks;
		this.rand = new Random(System.currentTimeMillis());
		this.used = new HashSet<Integer>(decks * CARDS_IN_DECK);
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
			this.used.add(c.ordinal());
			return c;
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
		cardsUsed = 0;
		notifyObservers();
	}
}
