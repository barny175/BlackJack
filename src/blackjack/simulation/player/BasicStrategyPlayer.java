/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack.simulation.player;

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
