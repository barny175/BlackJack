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
	private final Card dealersCard;
	private final String move;

	public SimulationPlayer(Card[] firstCards, String move, int money) {
		super(money);

		assert firstCards.length == 3;

		this.dealersCard = firstCards[1];
		
		this.firstTwoCards.addCard(firstCards[0]);
		this.firstTwoCards.addCard(firstCards[2]);
		
		this.move = move;
	}

	private int getRow(CardHand cards) {
		if (cards.aces() == 0) {
			final Card card = cards.getCards().get(0);
			if (card.getValue() == cards.getCards().get(1).getValue()) {
				if (card.getValue() == Card.TEN.getValue()) {
					return getRowPos("TT");
				} else {
					final int twoSameDigits = card.getValue() * 10 + card.getValue();
					return getRowPos(twoSameDigits);
				}
			} else {
				Integer hardSum = cards.hardSum();
		        return getRowPos(hardSum);
			}
		} else {
			Integer sum = cards.hardSum() - Card.ACE.getValue();

			String toFind;
			if (sum == 1) {
				toFind = "AA";
			} else {
				toFind = "A" + sum;
			}
			return getRowPos(toFind);
		}
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
}
