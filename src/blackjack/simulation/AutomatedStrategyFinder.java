/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack.simulation;

import blackjack.engine.Card;
import blackjack.engine.Engine;
import blackjack.engine.IllegalMoveException;
import blackjack.engine.rules.BasicRules;
import blackjack.simulation.player.SimulationPlayer;
import java.util.EnumSet;
import java.util.logging.Logger;

/**
 *
 * @author mbarnas
 */
public class AutomatedStrategyFinder {

	public static final int GAMES = 100000;
	public static final int initialMoney = 500000;
	private Engine engine;
	protected final BasicRules rules = new BasicRules();
	private final String[] allMoves = {"Hit", "Stand", "Split", "Double", "DoubleStand"};
	private static Logger logger = Logger.getLogger("");

	public static void main(String[] args) {
		new AutomatedStrategyFinder().run();
	}

	public void run() {
		final Card firstCard = Card.TWO;
		final Card secondCard = Card.FIVE;
		for (Card dealersCard : EnumSet.range(Card.ACE, Card.KING)) {
			String bestMove = null;
			int bestScore = 0;
			int numberOfGames = 0;

			for (String move : allMoves) {
				if (!isMoveAllowed(firstCard, secondCard, move)) {
					continue;
				}

				final SimulationCardShuffler cardShuffler = new SimulationCardShuffler(6).withPlayerCards(firstCard, secondCard).withDealersCard(dealersCard);

				final SimulationPlayer player = new SimulationPlayer.SimulationPlayerBuilder()
						.playersCard(firstCard, secondCard)
						.dealersCard(dealersCard)
						.move(move)
						.withMoney(initialMoney)
						.build();

				player.setRules(rules);

				this.engine = new Engine(rules, cardShuffler);
				this.engine.addPlayer(player);

				int i = 0;
				for (; i < GAMES; i++) {
					this.engine.newGame();
					try {
						this.engine.start();
					} catch (IllegalMoveException ex) {
						System.out.print("Illegal move.");
					}

					if (player.getMoney() < 10) {
						logger.finer(String.format("%-20s: short of money after %d rounds. ", player.getName(), i));
						break;
					}
				}

				final int result = player.getMoney();
				if (result > 10) {
					logger.finer(String.format("%-20s: %d (%d%%, %f%%)", player.getName(), result, result * 100 / initialMoney,
							((float) (initialMoney - result)) / GAMES * 10));
				}

				if (result < 10) {
					if (bestMove == null) {
						numberOfGames = i;
						bestMove = move;
					} else {
						if (bestScore == 0 && i > numberOfGames) {
							bestMove = move;
							numberOfGames = i;
						}
					}
				} else {
					if (result > bestScore) {
						bestMove = move;
						bestScore = result;
					}
				}
			}

			if (bestScore == 0) {
				System.out.println(String.format("Best move for player's cards [%s, %s] and dealer's card %s is %s. Short of money after %d games.",
						firstCard, secondCard, dealersCard, bestMove, numberOfGames));
			} else {
				System.out.println(String.format("Best move for player's cards [%s, %s] and dealer's card %s is %s. Score %d (%d%%, %f%%)",
					firstCard, secondCard, dealersCard, bestMove, bestScore, bestScore * 100 / initialMoney,
					((float) (initialMoney - bestScore)) / GAMES * 10));
			}
		}
	}

	private boolean isMoveAllowed(Card firstCard, Card secondCard, String move) {
		if (firstCard != secondCard && "Split".equals(move)) {
			return false;
		}

		return true;
	}
}
