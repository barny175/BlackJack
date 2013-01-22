/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack.engine;

import com.google.common.collect.Sets;
import com.google.inject.Inject;
import java.util.EnumSet;
import java.util.Set;

/**
 *
 * @author mbarnas
 */
public class Rules {

	public static final Set<Move> allMoves = Sets.newEnumSet(EnumSet.allOf(Move.class), Move.class);
	private boolean peek = true;
	private boolean doubleAfterSplit = true;
	private DoubleOn doubleRules = DoubleOn.All;
	private boolean resplitAces = true;

	@Inject
	public void setDoubleRules(DoubleOn doubleRules) {
		this.doubleRules = doubleRules;
	}

	public void setEuropean() {
		this.setPeek(false);
	}

	public void setResplitAces(boolean resplitAces) {
		this.resplitAces = resplitAces;
	}

	@Inject
	public void setPeek(@Peek boolean peek) {
		this.peek = peek;
	}

	public boolean getPeek() {
		return this.peek;
	}
	
	@Inject
	public void setDoubleAfterSplit(@DoubleAfterSplit boolean doubleAfterSplit) {
		this.doubleAfterSplit = doubleAfterSplit;
	}

	public Set<Move> getAllowedMoves(Game game) {
		Set<Move> allowedMoves = EnumSet.copyOf(allMoves);

		if (!isSplitPossible(game)) {
			allowedMoves.remove(Move.Split);
		}

		if (!doubleRules.isDoublePossible(game)
				|| (game.isSplitted() && !this.doubleAfterSplit)) {
			allowedMoves.remove(Move.Double);
		}

		if (game.getInsuranceBet() != 0) {
			allowedMoves.remove(Move.Split);
		}

		return allowedMoves;
	}

	public boolean isSplitPossible(Game game) {
		if (game.gameState() != GameState.FirstDeal) {
			return false;
		}

		final CardHand playerCards = game.playerCards();
		if (playerCards.count() != 2 || playerCards.get(0).getValue() != playerCards.get(1).getValue()) {
			return false;
		}

		if (!resplitAces
				&& (game.isSplitted() && game.playerCards().count() == 1 && game.playerCards().getCards().get(0) == Card.ACE)) {
			return false;
		}

		return true;
	}
}
