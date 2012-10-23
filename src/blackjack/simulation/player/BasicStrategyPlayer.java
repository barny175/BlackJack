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

    private final Object[][] strategy = new Object[][]{
		{7, "Hit", "Hit", "Hit", "Hit", "Hit", "Hit", "Hit", "Hit", "Hit", "Hit"},
        {8, "Hit", "Hit", "Hit", "Hit", "Hit", "Hit", "Hit", "Hit", "Hit", "Hit"},
        {9, "Hit", "Double", "Double", "Double", "Double", "Hit", "Hit", "Hit", "Hit", "Hit"},
        {10, "Double", "Double", "Double", "Double", "Double", "Double", "Double", "Double", "Hit", "Hit"},
        {11, "Double", "Double", "Double", "Double", "Double", "Double", "Double", "Double", "Double", "Hit"},
        {12, "Hit", "Hit", "Stand", "Stand", "Stand", "Hit", "Hit", "Hit", "Hit", "Hit"},
        {13, "Stand", "Stand", "Stand", "Stand", "Stand", "Hit", "Hit", "Hit", "Hit", "Hit"},
        {14, "Stand", "Stand", "Stand", "Stand", "Stand", "Hit", "Hit", "Hit", "Hit", "Hit"},
        {15, "Stand", "Stand", "Stand", "Stand", "Stand", "Hit", "Hit", "Hit", "Hit", "Hit"},
        {16, "Stand", "Stand", "Stand", "Stand", "Stand", "Hit", "Hit", "Hit", "Hit", "Hit"},
        {17, "Stand", "Stand", "Stand", "Stand", "Stand", "Stand", "Stand", "Stand", "Stand", "Stand"},
        {"A2", "Hit", "Hit", "Hit", "Double", "Double", "Hit", "Hit", "Hit", "Hit", "Hit"},
        {"A3", "Hit", "Hit", "Hit", "Double", "Double", "Hit", "Hit", "Hit", "Hit", "Hit"},
        {"A4", "Hit", "Hit", "Double", "Double", "Double", "Hit", "Hit", "Hit", "Hit", "Hit"},
        {"A5", "Hit", "Hit", "Double", "Double", "Double", "Hit", "Hit", "Hit", "Hit", "Hit"},
        {"A6", "Hit", "Double", "Double", "Double", "Double", "Hit", "Hit", "Hit", "Hit", "Hit"},
        {"A7", "Stand", "Double", "Double", "Double", "Double", "Stand", "Stand", "Hit", "Hit", "Hit"},
        {"A8", "Stand", "Stand", "Stand", "Stand", "Stand", "Stand", "Stand", "Stand", "Stand", "Stand"},
        {"A9", "Stand", "Stand", "Stand", "Stand", "Stand", "Stand", "Stand", "Stand", "Stand", "Stand"},
        //        {2,2, "Split", "Split", "Split", "Split", "Split", "Split", "Hit", "Hit", "Hit", "Hit"},
        //        {3,3, "Split", "Split", "Split", "Split", "Split", "Split", "Hit", "Hit", "Hit", "Hit"},
        //        {4,4, "Hit", "Hit", "Hit", "Split", "Split", "Hit", "Hit", "Hit", "Hit", "Hit"},
        //        {5,5, "Double", "Double", "Double", "Double", "Double", "Double", "Double", "Double", "Hit", "Hit"},
        //        {6,6, "Split", "Split", "Split", "Split", "Split", "Hit", "Hit", "Hit", "Hit", "Hit"},
        //        {7,7, "Split", "Split", "Split", "Split", "Split", "Split", "Hit", "Hit", "Hit", "Hit"},
        //        {8,8, "Split", "Split", "Split", "Split", "Split", "Split", "Split", "Split", "Split", "Split"},
        //        {9,9, "Split", "Split", "Split", "Split", "Split", "Stand", "Split", "Split", "Stand", "Stand"},
        //        {T,T, "Stand", "Stand", "Stand", "Stand", "Stand", "Stand", "Stand", "Stand", "Stand", "Stand"},
        {"AA", "Split", "Split", "Split", "Split", "Split", "Split", "Split", "Split", "Split", "Split"}};

    public BasicStrategyPlayer(int money) {
        super(money);
    }

    @Override
    public Move move() {
        if (cards.isBlackJack()) {
            return Move.Stand;
        }

        int aces = cards.aces();
        if (aces == 0) {
            return noAceRules();
        } else {
            return aceRules();
        }
    }

    @Override
    public String getName() {
        return "Basic Strategy";
    }

    private Move noAceRules() {
        Integer hardSum = this.cards.hardSum();

        if (hardSum < 7) {
            return Move.Hit;
        }
        if (hardSum > 17) {
            return Move.Stand;
        }

        return findMove(hardSum);
    }

    private Move aceRules() {
        Integer sum = this.cards.softSum() - Card.ACE.getValue();
		
		if (sum > 18)
			return noAceRules();

        String toFind;
        if (sum == 1) {
            toFind = "AA";
        } else {
            toFind = "A" + sum;
        }

        return findMove(toFind);
    }

    private Move findMove(Object toFind) {
        int movePos = this.currentGame.getDealerUpCard().getValue() - 1;
        int row = getRowPos(toFind);

        return Move.valueOf((String) strategy[row][movePos == 0 ? (strategy[row].length - 1) : movePos]);

    }
    
    private int getRowPos(Object firstCol) {
        int row = -1;
        for (int i = 0; i < strategy.length; i++) {
            if (strategy[i][0].equals(firstCol)) {
                row = i;
            }
        }
        return row;
    }
}
