/*
 * (C) 2012 Kerio Technologies s.r.o.
 */
package blackjack.simulation.player;

import blackjack.engine.Card;
import blackjack.engine.CardHand;
import blackjack.engine.Move;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import java.util.Set;

/**
 *
 * @author mbarnas
 */
public class BellevuePlayer extends BasicStrategyPlayer {

    @Inject
    public BellevuePlayer(@Named(BasicStrategyPlayer.DEPOSIT) int money) {
        super(money, 6);
    }

    @Override
    public Move move(CardHand cards, Card dealerUpCard, Set<Move> allowedMoves) {
        return super.move(cards, dealerUpCard, allowedMoves);
    }

    @Override
    public String getName() {
        return "Bellevue Basic Strategy";
    }

    protected void initStrategy() {
        this.strategy = new Object[][]{
            {7, "Hit", "Hit", "Hit", "Hit", "Hit", "Hit", "Hit", "Hit", "Hit", "Hit"},
            {8, "Hit", "Hit", "Hit", "Hit", "Hit", "Hit", "Hit", "Hit", "Hit", "Hit"},
            {9, "Stand", "Double", "Double", "Double", "Double", "Hit", "Hit", "Hit", "Hit", "Hit"},
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
            {"A7", "Stand", "DoubleStand", "DoubleStand", "DoubleStand", "DoubleStand", "Stand", "Stand", "Hit", "Hit", "Hit"},
            {"A8", "Stand", "Stand", "Stand", "Stand", "Stand", "Stand", "Stand", "Stand", "Stand", "Stand"},
            {"A9", "Stand", "Stand", "Stand", "Stand", "Stand", "Stand", "Stand", "Stand", "Stand", "Stand"},
            {22, "Split", "Split", "Split", "Split", "Split", "Split", "Hit", "Hit", "Hit", "Hit"},
            {33, "Split", "Split", "Split", "Split", "Split", "Split", "Hit", "Hit", "Hit", "Hit"},
            {44, "Hit", "Hit", "Hit", "Split", "Split", "Hit", "Hit", "Hit", "Hit", "Hit"},
            {55, "Double", "Double", "Double", "Double", "Double", "Double", "Double", "Double", "Hit", "Hit"},
            {66, "Split", "Split", "Split", "Split", "Split", "Hit", "Hit", "Hit", "Hit", "Hit"},
            {77, "Split", "Split", "Split", "Split", "Split", "Split", "Hit", "Hit", "Hit", "Hit"},
            {88, "Split", "Split", "Split", "Split", "Split", "Split", "Split", "Split", "Split", "Split"},
            {99, "Split", "Split", "Split", "Split", "Split", "Stand", "Split", "Split", "Stand", "Stand"},
            {"TT", "Stand", "Stand", "Stand", "Stand", "Stand", "Stand", "Stand", "Stand", "Stand", "Stand"},
            {"AA", "Split", "Split", "Split", "Split", "Split", "Split", "Split", "Split", "Split", "Split"}};
    }
}
