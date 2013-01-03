/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack.simulation;

import blackjack.engine.*;
import blackjack.engine.rules.BasicDoubleRules;
import blackjack.engine.rules.BasicRules;
import blackjack.engine.rules.BasicSplitRules;
import blackjack.simulation.player.SimulationPlayer;
import java.util.Set;

/**
 * runs simulation for given player first two cards and dealer's card
 * @author mbarnas
 */
public class BasicStrategySimulation {

	private final String[] allMoves = {"Hit", "Stand", "Split", "Double"};

	private int games = 100000;
	private int initialMoney = 100000;
	private int bet = 2;
	private int decks = 1;
	private boolean doubleAfterSplit = true;
	private DoubleRules doubleRules = new BasicDoubleRules();
	protected BasicRules rules = new BasicRules();
	private boolean peek = true;
	private SplitRules splitRules = new BasicSplitRules();
	
	private final Card firstCard;
	private final Card secondCard;
	private final Card dealersCard;
	
	private String bestMove = null;
	private int bestScore = 0;
	private int numberOfGames = 0;
	
	private Engine engine;
	
	public String getBestMove() {
		return bestMove;
	}

	public int getBestScore() {
		return bestScore;
	}
	
	public float getWinPerGame() {
		return  ((float) (bestScore - initialMoney)) / numberOfGames / bet;
	}

	public void setBet(int bet) {
		this.bet = bet;
	}

	public void setGames(int games) {
		this.games = games;
	}

	public void setDecks(int decks) {
		this.decks = decks;
	}

	public void setDoubleAfterSplit(boolean doubleAfterSplit) {
		this.doubleAfterSplit = doubleAfterSplit;
	}

	public void setDoubleRules(DoubleRules doubleRules) {
		this.doubleRules = doubleRules;
	}

	public void setInitialMoney(int initialMoney) {
		this.initialMoney = initialMoney;
	}

	public void setPeek(boolean peek) {
		this.peek = peek;
	}

	public void setRules(BasicRules rules) {
		this.rules = rules;
	}

	public void setSplitRules(SplitRules splitRules) {
		this.splitRules = splitRules;
	}

	public BasicStrategySimulation(Card firstCard, Card secondCard, Card dealersCard) {
		this.firstCard = firstCard;
		this.secondCard = secondCard;
		this.dealersCard = dealersCard;
	}

	public void run() throws IllegalMoveException {
		for (String move : allMoves) {
			if (!isMoveAllowed(rules, firstCard, secondCard, move)) {
				continue;
			}

			final SimulationCardShuffler cardShuffler = new SimulationCardShuffler(decks).withDealersCard(dealersCard).withPlayerCards(firstCard, secondCard);

			final SimulationPlayer player = new SimulationPlayer.SimulationPlayerBuilder().playersCard(firstCard, secondCard).dealersCard(dealersCard).move(move).withMoney(initialMoney).build();
			player.setBet(this.bet);
			player.setRules(rules);

			prepareEngine(cardShuffler, player);

			int i = 0;
			for (; i < games; i++) {
				this.engine.newGame();
				this.engine.start();

				if (player.getMoney() < 10) {
					break;
				}
			}

			final int result = player.getMoney();

			if (result < 10) {
				if (this.bestMove == null) {
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
	}

	private void prepareEngine(final SimulationCardShuffler cardShuffler, final SimulationPlayer player) {
		this.engine = new Engine(rules, cardShuffler);
		this.engine.setPeek(this.peek);
		this.engine.setDoubleAfterSplit(this.doubleAfterSplit);
		this.engine.setDoubleRules(this.doubleRules);
		this.engine.setSplitRules(this.splitRules);
		this.engine.addPlayer(player);
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
}
