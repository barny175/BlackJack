/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack.simulation;

import blackjack.engine.*;
import blackjack.engine.rules.BasicDoubleRules;
import blackjack.engine.rules.BasicRules;
import blackjack.simulation.player.SimulationPlayer;
import com.google.common.collect.Lists;
import java.util.List;
import java.util.Set;

/**
 * runs simulation for given player first two cards and dealer's card
 *
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
	private boolean resplitAces = true;
	private final Card firstCard;
	private final Card secondCard;
	private final Card dealersCard;
	private String bestMove = null;
	private Float bestScore = null;
	private Boolean insuranceAllowed = Boolean.FALSE;
	private Card[] missingCards;

	public String getBestMove() {
		return bestMove + (this.insuranceAllowed ? "Insurance" : "");
	}

	public float getBestScore() {
		return bestScore;
	}

	public float getWinPerGame() {
		return bestScore;
	}

	public void setResplitAces(boolean resplitAces) {
		this.resplitAces = resplitAces;
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

	public void setMissingCards(Card... missingCards) {
		this.missingCards = missingCards;
	}
	
	public void setPeek(boolean peek) {
		this.peek = peek;
	}

	public void setRules(BasicRules rules) {
		this.rules = rules;
	}

	public BasicStrategySimulation(Card firstCard, Card secondCard, Card dealersCard) {
		this.firstCard = firstCard;
		this.secondCard = secondCard;
		this.dealersCard = dealersCard;
	}

	public void run() throws IllegalMoveException {
		List<Boolean> insuranceValues = Lists.newArrayList(Boolean.FALSE);
		if (dealersCard == Card.ACE) {
			insuranceValues.add(Boolean.TRUE);
		}

		for (Boolean insurance : insuranceValues) {
			for (String move : allMoves) {
				if (!isMoveAllowed(rules, firstCard, secondCard, move, insurance)) {
					continue;
				}

				final SimulationCardShuffler cardShuffler = new SimulationCardShuffler(decks)
						.withDealersCard(dealersCard)
						.withPlayerCards(firstCard, secondCard)
						.withCardsMissing(missingCards);

				final SimulationPlayer player = new SimulationPlayer.SimulationPlayerBuilder()
						.playersCard(firstCard, secondCard)
						.dealersCard(dealersCard)
						.move(move)
						.withMoney(initialMoney)
						.insurance(insurance)
						.build();

				player.setBet(this.bet);

				Simulation simulation = new Simulation();
				simulation.setBet(bet);
				simulation.setCardShuffler(cardShuffler);
				simulation.setPlayer(player);
				simulation.setDoubleAfterSplit(doubleAfterSplit);
				simulation.setDoubleRules(doubleRules);
				simulation.setGames(games);
				simulation.setPeek(peek);
				simulation.setRules(rules);
				simulation.setResplitAces(resplitAces);
				simulation.run();

				final float result = simulation.getWinPerGame();

				if (bestScore == null || result > bestScore) {
					bestMove = move;
					bestScore = result;
					insuranceAllowed = insurance;
				}
			}
		}
	}

	private boolean isMoveAllowed(BasicRules rules, Card firstCard, Card secondCard, String move, Boolean insurance) {
		if ("Split".equals(move) && (firstCard != secondCard || insurance)) {
			return false;
		}

		Game game = new Game(null, null);
		game.setGameState(GameState.FirstDeal);
		game.addPlayerCard(firstCard);
		game.addPlayerCard(secondCard);
		Set<Move> allowedMoves = rules.getAllowedMoves(game);

		Move m = Move.valueOf(move);
		return allowedMoves.contains(m);
	}
}
