/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack.simulation;

import blackjack.engine.*;
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
	private final Card firstCard;
	private final Card secondCard;
	private final Card dealersCard;
	private String bestMove = null;
	private Float bestScore = null;
	private Boolean insuranceAllowed = Boolean.FALSE;
	private Card[] missingCards;
	private Rules rules = new Rules();

	public String getBestMove() {
		return bestMove + (this.insuranceAllowed ? "Insurance" : "");
	}

	public float getBestScore() {
		return bestScore;
	}

	public float getWinPerGame() {
		return bestScore;
	}

	public void setRules(Rules rules) {
		this.rules = rules;
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

	public void setInitialMoney(int initialMoney) {
		this.initialMoney = initialMoney;
	}

	public void setMissingCards(Card... missingCards) {
		this.missingCards = missingCards;
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
				if (!isMoveAllowed(firstCard, secondCard, move, insurance)) {
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

				Simulation simulation = new Simulation(rules);
				simulation.setBet(bet);
				simulation.setCardShuffler(cardShuffler);
				simulation.setPlayer(player);
				simulation.setGames(games);
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

	private boolean isMoveAllowed(Card firstCard, Card secondCard, String move, Boolean insurance) {
		if ("Split".equals(move) && (firstCard != secondCard || insurance)) {
			return false;
		}

		Game game = new Game(null, null);
		game.setGameState(GameState.FirstDeal);
		game.addPlayerCard(firstCard);
		game.addPlayerCard(secondCard);
		
		Set<Move> allowedMoves = rules.getAllowedMoves(game);
		if (!allowedMoves.contains(Move.Double) && "Double".equals(move))
			return false;

		Move m = Move.valueOf(move);
		return allowedMoves.contains(m);
	}
}
