/*
 * (C) 2012 Kerio Technologies s.r.o.
 */
package blackjack;

import blackjack.engine.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author mbarnas
 */
public class BlackJack {
    public static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    
    private static Engine engine = new Engine(new CardShuffler(1));
//	private static Engine engine = new Engine(new SimulationCardShuffler(1, Card.ACE, Card.TEN, Card.TEN));

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        Player player = new TrainingPlayer(100);
        engine.addPlayer(player);
        char c = 'y';
        while (c == 'y') {
            Game game = engine.newGame();
            
            engine.start();
            
            printCards(game);
            println("Result: " + engine.getGameState());
            
            c = getChoice("Another round", "yn");
        }
    }

    private static void printCards(Game game) {
        print("Player cards: ");
        printCards(game.playerCards());
        printDealerCards();
    }

    private static void printDealerCards() {
        print("Dealer cards: ");
        printCards(engine.getDealerCards());
    }

    public static char getChoice(String message, String choices) throws IOException {
        print(message);
        print(" ");
		for (int i = 0; i < choices.length(); i++) {
			print(choices.charAt(i) + "/");
		}
        print(": ");
        
        char c = 0;
        do {
            String line = reader.readLine();
            c = line.isEmpty() ? choices.charAt(0) : (char) line.charAt(0);
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
    
    public static void printCards(CardHand cards) {
        for (Card c : cards.getCards()) {
            print(c + " ");
        }
		print(" [" + cards.softSum() + "]");
        System.out.println();
    }
}
