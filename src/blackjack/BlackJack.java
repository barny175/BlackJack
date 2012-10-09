/*
 * (C) 2012 Kerio Technologies s.r.o.
 */
package blackjack;

import blackjack.engine.Card;
import blackjack.engine.CardShuffler;
import blackjack.engine.Engine;
import blackjack.engine.GameResult;
import java.util.List;

/**
 *
 * @author mbarnas
 */
public class BlackJack {

    private static Engine engine = new Engine(new CardShuffler(1));
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        CmdLinePlayer cmdLinePlayer = new CmdLinePlayer(100);
        engine.addPlayer(cmdLinePlayer);
        engine.start();
        while (engine.getGameState() == GameResult.Continuing) {
            printCards(cmdLinePlayer.getCards());
            engine.continueGame();
        }
    }

    private static void printCards(List<Card> cards) {
        for (Card c : cards)
            System.out.print(c + " ");
        System.out.println();
    }
}
