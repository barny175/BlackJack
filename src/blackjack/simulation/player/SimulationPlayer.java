/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack.simulation.player;

import blackjack.engine.Card;
import blackjack.engine.CardHand;
import blackjack.engine.Move;
import java.util.Set;

/**
 *
 * @author mbarnas
 */
public class SimulationPlayer extends BasicStrategyPlayer {

	private final CardHand firstTwoCards = new CardHand();
	private Card dealersCard;
	private String move;

	public static class SimulationPlayerBuilder {
		private String move;
		private Card dealersCard;
		private Card[] playersCards;
		private int money;
		
		public SimulationPlayerBuilder move(String move) {
			this.move = move;
			return this;
		}
		public SimulationPlayerBuilder dealersCard(Card card) {
			this.dealersCard = card;
			return this;
		}
		
		public SimulationPlayerBuilder playersCard(Card card1, Card card2) {
			this.playersCards = new Card[] {card1, card2};
			return this;
		}
		
		public SimulationPlayerBuilder withMoney(int money) {
			this.money = money;
			return this;
		}
				
		public SimulationPlayer build() {
			return new SimulationPlayer(this.playersCards, dealersCard, move, money);
		}
	}
	
	public SimulationPlayer(Card[] playersCards, Card dealersCard, String move, int money) {
		super(money);

		this.dealersCard = dealersCard;
		
		assert playersCards.length == 2;
		
		for (Card c : playersCards) {
			this.firstTwoCards.addCard(c);
		}
		
		this.move = move;
	}

	@Override
	public Move move(CardHand cards, Card dealerUpCard, Set<Move> allowedMoves) {
		Move myMove = stringToMove(this.move, allowedMoves);
		if (dealerUpCard == this.dealersCard && allowedMoves.contains(myMove)
				&& cards.hardSum() == this.firstTwoCards.hardSum()
				&& cards.softSum() == this.firstTwoCards.softSum()) {
			return myMove;
		}
		return super.move(cards, dealerUpCard, allowedMoves);
	}

	@Override
	public String getName() {
		return super.getName() + ", " + move + ", " + this.firstTwoCards + ",dealer's card: " + this.dealersCard;
	}
}
