/*
 * (C) 2012 Kerio Technologies s.r.o.
 */
package blackjack;

import blackjack.engine.Card;
import blackjack.engine.CardShuffler;
import blackjack.engine.Engine;
import blackjack.engine.GameResult;
import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 *
 * @author mbarnas
 */
public class BlackJack {
    public static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    
    private static Engine engine = new Engine(new CardShuffler(1));

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        CmdLinePlayer cmdLinePlayer = new CmdLinePlayer(100);
        engine.addPlayer(cmdLinePlayer);
        char c = 'y';
        while (c == 'y') {
            engine.newGame();
            
            engine.start();
            
            printCards(cmdLinePlayer);
            println("Result: " + engine.getGameState());
            println("Player credit: " + cmdLinePlayer.getMoney());
            
            c = getChoice("Another round", "yn");
        }
    }

    private static void printCards(CmdLinePlayer cmdLinePlayer) {
        print("Player cards: ");
        printCards(cmdLinePlayer.getCards());
        printDealerCards();
    }

    private static void printDealerCards() {
        println("Dealer cards: " + engine.getDealerCards());
    }

    public static char getChoice(String message, String choices) throws IOException {
        print(message);
        print(" ");
        print(choices);
        print(": ");
        
        char c = 0;
        do {
            String line = reader.readLine();
            c = (char) line.charAt(0);
            for (char x : choices.toCharArray()) {
                if (x == c)
                    return c;
            }
        } while(c == 0);
        
        return c;
    }

    public static void println(String message) {
        System.out.println(message);
    }
    
    private static void print(String message) {
        System.out.print(message);
    }
    
    public static void printCards(List<Card> cards) {
        for (Card c : cards) {
            print(c + " ");
        }
        System.out.println();
    }
}
