/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack.simulation;

import blackjack.engine.*;
import java.util.EnumSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author mbarnas
 */
public class AutomatedStrategyFinder {

	private static Logger logger = LoggerFactory.getLogger(AutomatedStrategyFinder.class);
	private final int decks = 1;
	private final boolean doubleAfterSplit = true;
	private final DoubleOn doubleOn = DoubleOn.All;
	private final boolean resplitAces = true;
	private boolean peek = true;

	public static void main(String[] args) {
		new AutomatedStrategyFinder().run();
	}

	public void run() {
		Card[] dealerCards = new Card[]{Card.TWO, Card.THREE, Card.FOUR, Card.FIVE, Card.SIX, Card.SEVEN, Card.EIGHT, Card.NINE, Card.TEN, Card.ACE};

		for (Card firstCard : EnumSet.range(Card.ACE, Card.TEN)) {
			for (Card secondCard : EnumSet.range(Card.ACE, Card.TEN)) {
				for (Card dealersCard : dealerCards) {

					BasicStrategySimulation strategySimulation = new BasicStrategySimulation(firstCard, secondCard, dealersCard);
					strategySimulation.setRules(getRules());
					strategySimulation.setDecks(decks);
					try {
						strategySimulation.run();
					} catch (IllegalMoveException ex) {
						logger.error("Illegal move");
						continue;
					}

					reportResult(firstCard, secondCard, dealersCard,
							strategySimulation.getBestMove(), strategySimulation.getWinPerGame());
				}
			}
		}
		printResult();
	}

	private void reportResult(final Card firstCard, final Card secondCard, Card dealersCard, String bestMove, float winPerGame) {
		Object[] row = getRowPos(firstCard, secondCard);
		if (row != null) {
			row[dealersCard.getSoftValue() - 1] = String.format("%s (%4.2f%%)", bestMove, winPerGame);
		}
		logger.info(String.format("Player: %5s, %5s, dealer: %5s - %5s. Win per game %4.2f",
				firstCard, secondCard, dealersCard, bestMove, winPerGame));
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

	private void printResult() {
		printRules();
		printTable();		
	}

	private Rules getRules() {
		Rules rules = new Rules();
		rules.setPeek(this.peek);
		rules.setDoubleAfterSplit(this.doubleAfterSplit);
		rules.setDoubleRules(this.doubleOn);
		rules.setResplitAces(resplitAces);
		return rules;
	}

	private void printTable() {
		System.out.println("                TWO           THREE            FOUR            FIVE             SIX           SEVEN           EIGHT            NINE             TEN            ACE");

		for (Object[] row : this.strategy) {
			for (int i = 0; i < row.length; i++) {
				if (i == 0) {
					System.out.print(String.format("%3s", row[i]));
				} else {
					System.out.print(String.format("%16s", row[i]));
				}
			}
			System.out.println();
		}
	}

	private void printRules() {
		println("Decks: " + this.decks);
		println("Double on: " + this.doubleOn);
		println("Double after split: " + this.doubleAfterSplit);
		println("Dealer peeks: " + this.peek);
		println("Resplit aces: " + this.resplitAces);
	}
	
	private void println(String message) {
		System.out.println(message);
	}
}
