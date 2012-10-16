/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack.simulation.player;

import blackjack.engine.Card;
import blackjack.engine.Move;

/**
 *
 * @author mbarnas
 */
public class BasicStrategyPlayer extends BasePlayer {

    public BasicStrategyPlayer(int money) {
        super(money);
    }

    @Override
    public Move move() {
        if (cards.isBlackJack()) {
            return Move.Stand;
        }

        final int hardSum = cards.hardSum();
        
        int aces = cards.aces();
        if (aces == 0)
            return noAceRules(hardSum);
        
        final int sumNoAces = hardSum - aces * Card.ACE.getValue();
        if (sumNoAces < 7)
            return Move.Hit;
        else if (sumNoAces == 7) {
            if (this.currentGame.getDealerUpCard().getSoftValue() > 8)
                return Move.Stand;
            else
                return Move.Hit;
        }
        return Move.Stand;
    }

    private Move noAceRules(final int hardSum) {
        switch (this.currentGame.getDealerUpCard()) {
            case TWO:
            case THREE:
                if (hardSum < 13) {
                    return Move.Hit;
                }
                break;
            case FOUR:
            case FIVE:
            case SIX:
                if (hardSum < 12) {
                    return Move.Hit;
                }
                break;
            default:
                return Move.Hit;
        }
        return Move.Stand;
    }

	@Override
	public String getName() {
		return "Basic Strategy";
	}
}
