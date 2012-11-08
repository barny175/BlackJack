/*
 * (C) 2012 Kerio Technologies s.r.o.
 */
package blackjack;

import blackjack.engine.*;
import com.google.inject.Guice;
import com.google.inject.Injector;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author mbarnas
 */
public class BlackJack {

	public static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
	private static Engine engine;
//	private static Engine engine = new Engine(new SimulationCardShuffler(1, Card.ACE, Card.TEN, Card.TEN));

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) throws IOException {
		Injector injector = Guice.createInjector(new BasicModule());
		engine = injector.getInstance(Engine.class);

		Player player = injector.getInstance(TrainingPlayer.class);
		engine.addPlayer(player);
		char c = 'y';
		while (c == 'y') {
			engine.newGame();
			try {
				engine.start();
			} catch (IllegalMoveException ex) {
				println("Illegal move.");
			}

			c = getChoice("Another round", "yn");
		}
	}

	public static void printCards(Game game) {
		print("Player cards: ");
		printCards(game.playerCards());
		printDealerCards();
	}

	private static void printDealerCards() {
		print("Dealer cards: ");
		printCards(engine.getDealerCards());
	}

	public static char getChoice(String message, String choices) throws IOException {
		do {
			print(message);
			print(" ");
			for (int i = 0; i < choices.length(); i++) {
				print(choices.charAt(i) + "/");
			}
			print(": ");

			String line = reader.readLine();
			char c = line.isEmpty() ? choices.charAt(0) : (char) line.charAt(0);
			for (char x : choices.toCharArray()) {
				if (x == c) {
					return c;
				}
			}
			println("Wrong choice!");
		} while (true);
	}

	public static void println(String message) {
		System.out.println(message);
	}

	public static void print(String message) {
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
