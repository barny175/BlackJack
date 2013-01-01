/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack.simulation;

import blackjack.engine.*;
import blackjack.engine.rules.BasicRules;
import blackjack.engine.rules.BasicSplitRules;
import blackjack.engine.rules.BellevueDoubleRules;
import blackjack.engine.rules.BellevueRules;
import blackjack.simulation.player.SimulationPlayer;
import java.util.EnumSet;
import java.util.Set;
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
//	protected final BasicRules rules = new BasicRules();
	protected final BasicRules rules = new BellevueRules();
	private final String[] allMoves = {"Hit", "Stand", "Split", "Double"};
	private static Logger logger = LoggerFactory.getLogger(AutomatedStrategyFinder.class);
	private boolean european = true;

	public static void main(String[] args) {
		new AutomatedStrategyFinder().run();
	}

	public void run() {
		Card[] dealerCards = new Card[]{Card.TWO, Card.THREE, Card.FOUR, Card.FIVE, Card.SIX, Card.SEVEN, Card.EIGHT, Card.NINE, Card.TEN, Card.ACE};

		for (Card firstCard : EnumSet.range(Card.ACE, Card.TEN)) {
			for (Card secondCard : EnumSet.range(Card.ACE, Card.TEN)) {
				for (Card dealersCard : dealerCards) {
					String bestMove = null;
					int bestScore = 0;
					int numberOfGames = 0;

					for (String move : allMoves) {
						if (!isMoveAllowed(rules, firstCard, secondCard, move)) {
							continue;
						}

						final SimulationCardShuffler cardShuffler = new SimulationCardShuffler(6).withDealersCard(dealersCard).withPlayerCards(firstCard, secondCard);

						final SimulationPlayer player = new SimulationPlayer.SimulationPlayerBuilder().playersCard(firstCard, secondCard).dealersCard(dealersCard).move(move).withMoney(initialMoney).build();

						player.setRules(rules);
						
						prepareEngine(cardShuffler, player);

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

					reportResult(firstCard, secondCard, dealersCard, bestMove, bestScore * 100 / initialMoney, ((float) (bestScore - initialMoney)) / numberOfGames / BET);
				}
			}
		}
		printTable();
	}

	private void prepareEngine(final SimulationCardShuffler cardShuffler, final SimulationPlayer player) {
		this.engine = new Engine(rules, cardShuffler);
		this.engine.setPeek(!this.european);
		this.engine.setDoubleAfterSplit(false);
		this.engine.setDoubleRules(new BellevueDoubleRules());
		this.engine.setSplitRules(new BasicSplitRules());
		this.engine.addPlayer(player);
	}

	private void reportResult(final Card firstCard, final Card secondCard, Card dealersCard, String bestMove, int score, float winPerGame) {
		Object[] row = getRowPos(firstCard, secondCard);
		if (row != null) {
			row[dealersCard.getSoftValue() - 1] = String.format("%s (%s%%)", bestMove, score);
		}
		logger.info(String.format("Player: %5s, %5s, dealer: %5s - %5s. Score %3d%% (%4.2f)",
				firstCard, secondCard, dealersCard, bestMove, score, winPerGame));
	}

	private boolean isMoveAllowed(BasicRules rules, Card firstCard, Card secondCard, String move) {
		if (firstCard != secondCard && "Split".equals(move)) {
			return false;
		}

		Game game = new Game(engine, null);
		game.setGameState(GameState.FirstDeal);
		game.addPlayerCard(firstCard);
		game.addPlayerCard(secondCard);
		Set<Move> allowedMoves = rules.getAllowedMoves(game);

		Move m = Move.valueOf(move);
		return allowedMoves.contains(m);
	}
	private Object[][] strategy = new Object[][]{
		{7, null, null, null, null, null, null, null, null, null, null},
		{8, null, null, null, null, null, null, null, null, null, null},
		{9, null, null, null, null, null, null, null, null, null, null},
		{10, null, null, null, null, null, null, null, null, null, null},
		{11, null, null, null, null, null, null, null, null, null, null},
		{12, null, null, null, null, null, null, null, null, null, null},
		{13, null, null, null, null, null, null, null, null, null, null},
		{14, null, null, null, null, null, null, null, null, null, null},
		{15, null, null, null, null, null, null, null, null, null, null},
		{16, null, null, null, null, null, null, null, null, null, null},
		{17, null, null, null, null, null, null, null, null, null, null},
		{"A2", null, null, null, null, null, null, null, null, null, null},
		{"A3", null, null, null, null, null, null, null, null, null, null},
		{"A4", null, null, null, null, null, null, null, null, null, null},
		{"A5", null, null, null, null, null, null, null, null, null, null},
		{"A6", null, null, null, null, null, null, null, null, null, null},
		{"A7", null, null, null, null, null, null, null, null, null, null},
		{"A8", null, null, null, null, null, null, null, null, null, null},
		{"A9", null, null, null, null, null, null, null, null, null, null},
		{22, null, null, null, null, null, null, null, null, null, null},
		{33, null, null, null, null, null, null, null, null, null, null},
		{44, null, null, null, null, null, null, null, null, null, null},
		{55, null, null, null, null, null, null, null, null, null, null},
		{66, null, null, null, null, null, null, null, null, null, null},
		{77, null, null, null, null, null, null, null, null, null, null},
		{88, null, null, null, null, null, null, null, null, null, null},
		{99, null, null, null, null, null, null, null, null, null, null},
		{"TT", null, null, null, null, null, null, null, null, null, null},
		{"AA", null, null, null, null, null, null, null, null, null, null}};

	private Object[] getRowPos(Card firstCard, Card secondCard) {
		Object toFind = firstCard.getSoftValue() + secondCard.getSoftValue();
		if (firstCard.getValue() == secondCard.getValue()) {
			if (firstCard == Card.ACE) {
				toFind = "AA";
			} else if (firstCard.getValue() == 10) {
				toFind = "TT";
			} else {
				toFind = firstCard.getValue() * 10 + firstCard.getValue();
			}
		} else if (firstCard == Card.ACE || secondCard == Card.ACE) {
			toFind = "A" + Integer.toString((Integer) toFind - Card.ACE.getSoftValue());
		}

		for (Object[] row : this.strategy) {
			if (row[0].equals(toFind)) {
				return row;
			}
		}

		return null;
	}

	private void printTable() {
		for (Object[] row : this.strategy) {
			for (Object o : row) {
				System.out.print(String.format("%14s", o));
			}
			System.out.println();
		}
	}
}
