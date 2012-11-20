/*
 * (C) 2012 Kerio Technologies s.r.o.
 */
package blackjack;

import blackjack.engine.*;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import java.util.Set;

/**
 *
 * @author mbarnas
 */
public class TrainingPlayer implements Player {
    private CmdLinePlayer cmdLinePlayer;
	@Inject
    private Player idealPlayer;

	@Inject
    public TrainingPlayer(Player player, @Named("deposit") int amount) {
        cmdLinePlayer = new CmdLinePlayer(amount);
        this.idealPlayer = player;
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
    public void gameEnded(Game game) {
        cmdLinePlayer.gameEnded(game);
    }

    @Override
    public Move move(CardHand cards, Card dealerUpCard, Set<Move> allowedMoves) {
        Move move = cmdLinePlayer.move(cards, dealerUpCard, allowedMoves);
        Move basicStrMove = idealPlayer.move(cards, dealerUpCard, allowedMoves);
        if (basicStrMove != move) {
            BlackJack.println("Basic strategy suggests " + basicStrMove + "!!!");
        }
        return move;
    }
    
    int getMoney() {
        return this.cmdLinePlayer.getMoney();
    }

	@Override
	public void newGame(Game game) {
		
	}
}
