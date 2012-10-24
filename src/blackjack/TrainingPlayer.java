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
    public int bet() {
        return cmdLinePlayer.bet();
    }

    @Override
    public void addMoney(int amount) {
        cmdLinePlayer.addMoney(amount);
    }

    @Override
    public void result(GameState result) {
        cmdLinePlayer.result(result);
    }

    @Override
    public void addGame(Game game) {
        basicStrategyPlayer.addGame(game);
        cmdLinePlayer.addGame(game);
    }

    @Override
    public Move move(CardHand cards) {
        Move move = cmdLinePlayer.move(cards);
        Move basicStrMove = basicStrategyPlayer.move(cards);
        if (basicStrMove != move) {
            BlackJack.println("Basic strategy suggests " + basicStrMove + "!!!");
        }
        return move;
    }
}
