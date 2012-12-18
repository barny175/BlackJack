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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author mbarnas
 */
public class AutomatedStrategyFinder {

	public static final int GAMES = 100000;
	public static final int initialMoney = 500000;
	private static final float BET = 10;
	private Engine engine;
	protected final BasicRules rules = new BasicRules();
	private final String[] allMoves = {"Hit", "Stand", "Split", "Double", "DoubleStand"};
	private static Logger logger = LoggerFactory.getLogger(AutomatedStrategyFinder.class);

	public static void main(String[] args) {
		new AutomatedStrategyFinder().run();
	}

	public void run() {
		final Card firstCard = Card.TWO;
		final Card secondCard = Card.FOUR;
		for (Card thirdCard : EnumSet.range(Card.TWO, Card.TWO)) {
			for (Card dealersCard : EnumSet.range(Card.ACE, Card.KING)) {
				String bestMove = null;
				int bestScore = 0;
				int numberOfGames = 0;

				for (String move : allMoves) {
					if (!isMoveAllowed(firstCard, secondCard, move)) {
						continue;
					}

					final SimulationCardShuffler cardShuffler = new SimulationCardShuffler(6, new Card[] {firstCard, dealersCard, secondCard, null, thirdCard});

					final SimulationPlayer player = new SimulationPlayer.SimulationPlayerBuilder()
							.playersCard(new Card[] {firstCard, secondCard, thirdCard})
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
							logger.error("Illegal move.");
						}

						if (player.getMoney() < 10) {
							break;
						}
					}

					final int result = player.getMoney();
					if (result > 10) {
						logger.debug(String.format("%-20s: %d (%d%%, %f%%)", player.getName(), result, result * 100 / initialMoney,
								((float) (initialMoney - result)) / i / 10));
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
							numberOfGames = i;
						}
					}
				}
				logger.info("Player: {}, {}, {}, dealer: {} - {}. Score {} ({}%, {})",
						firstCard, secondCard, thirdCard, dealersCard, bestMove, bestScore, bestScore * 100 / initialMoney,
						((float) (bestScore - initialMoney)) / numberOfGames / BET);
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
