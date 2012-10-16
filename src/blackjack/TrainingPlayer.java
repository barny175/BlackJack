/*
 * (C) 2012 Kerio Technologies s.r.o.
 */
package blackjack;

import blackjack.engine.*;
import blackjack.simulation.player.BasicStrategyPlayer;

/**
 *
 * @author mbarnas
 */
public class TrainingPlayer implements Player {
    private CmdLinePlayer cmdLinePlayer;
    private BasicStrategyPlayer basicStrategyPlayer;

    public TrainingPlayer(int amount) {
        cmdLinePlayer = new CmdLinePlayer(amount);
        basicStrategyPlayer = new BasicStrategyPlayer(amount);
    }

    
    @Override
    public int getBet() {
        return cmdLinePlayer.getBet();
    }

    @Override
    public void addMoney(int amount) {
        cmdLinePlayer.addMoney(amount);
    }

    @Override
    public void result(GameResult result) {
        cmdLinePlayer.result(result);
    }

    @Override
    public void setGame(Game game) {
        basicStrategyPlayer.setGame(game);
        cmdLinePlayer.setGame(game);
    }

    @Override
    public void addCard(Card card) {
        cmdLinePlayer.addCard(card);
        basicStrategyPlayer.addCard(card);
    }

    @Override
    public void reset() {
        cmdLinePlayer.reset();
        basicStrategyPlayer.reset();
    }

    @Override
    public Move move() {
        Move move = cmdLinePlayer.move();
        Move basicStrMove = basicStrategyPlayer.move();
        if (basicStrMove != move) {
            BlackJack.println("Basic strategy suggests " + basicStrMove + "!!!");
        }
        return move;
    }

    @Override
    public CardHand getCards() {
        return cmdLinePlayer.getCards();
    }
}
