/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack;

import blackjack.engine.Card;
import blackjack.engine.CardSource;
import blackjack.engine.DoubleOn;
import blackjack.engine.IllegalMoveException;
import blackjack.simulation.Simulation;
import blackjack.simulation.SimulationCardShuffler;
import blackjack.simulation.player.BasePlayer;
import blackjack.simulation.player.BasicStrategyPlayer;
import blackjack.simulation.player.SimpleCountingPlayer;
import com.google.common.base.Preconditions;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mbarnas
 */
public class SimulationRunner {
	public static void main(String[] args) {
		try {
			String propFile = "simulation.properties";
			
			if (args.length == 1) {
				propFile = args[0];
			}
			SimulationRunner simulationRunner = new SimulationRunner();
			simulationRunner.loadProperties(new FileInputStream(propFile));
			simulationRunner.run();
		} catch (IOException ex) {
			//todo
		}
	}
	
	private int decks;
	private boolean doubleAfterSplit;
	private int games;
	private int initialMoney;
	private boolean peek;
	private int bet;
	private boolean resplitAces;
	private DoubleOn doubleOn;
	
	private void run() {
		try {
			//Injector injector = Guice.createInjector(new SimulationModule(100000, new SimulationCardShuffler(1)));
			
			final CardSource shuffler = new SimulationCardShuffler(1).withCardsMissing(Card.TEN, Card.TEN, Card.TEN, Card.TEN);
			
			final BasicStrategyPlayer basicPlayer = new BasicStrategyPlayer(initialMoney);
			
			Simulation simulation = createSimulation(shuffler, basicPlayer);
			simulation.run();
			System.out.println(String.format("Score for %s is %d (%.1f)", basicPlayer.getName(), simulation.getBestScore(), simulation.getWinPerGame() * 100));
			
			final SimpleCountingPlayer player = new SimpleCountingPlayer(new BasicStrategyPlayer(initialMoney), initialMoney, decks);
			player.setBet(2);
			
			simulation = createSimulation(shuffler, player);
			simulation.run();
			System.out.println("Score for "+ player.getName() + " is " + simulation.getBestScore() + " (" + simulation.getWinPerGame() + ")");
		} catch (IllegalMoveException ex) {
			Logger.getLogger(Simulation.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	private static void usage() {
		System.out.println("Usage: java -cp BlackJack.jar blackjack.SimulationRunner simulation.properties");
	}

	private void loadProperties(FileInputStream propertyFile) throws IOException {
		Properties properties = new Properties();
		properties.load(propertyFile);
		
		Preconditions.checkNotNull(properties.getProperty("decks"));
		this.decks = Integer.parseInt(properties.getProperty("decks"));
		
		Preconditions.checkNotNull(properties.getProperty("doubleAfterSplit"));
		this.doubleAfterSplit = Boolean.parseBoolean(properties.getProperty("doubleAfterSplit"));
		
		Preconditions.checkNotNull(properties.getProperty("peek"));
		this.peek = Boolean.parseBoolean(properties.getProperty("peek"));

		Preconditions.checkNotNull(properties.getProperty("resplitAces"));
		this.resplitAces = Boolean.parseBoolean(properties.getProperty("resplitAces"));
		
		Preconditions.checkNotNull(properties.getProperty("doubleOn"));
		this.doubleOn = DoubleOn.valueOf(properties.getProperty("doubleOn"));
		
		this.games = Integer.parseInt(properties.getProperty("games", "100000"));
		
		this.initialMoney = Integer.parseInt(properties.getProperty("initialMoney", "100000"));
		
		this.bet = Integer.parseInt(properties.getProperty("bet", "2"));
	}

	private Simulation createSimulation(final CardSource shuffler, final BasePlayer basicPlayer) {
		Simulation simulation = new Simulation();
		simulation.setDoubleAfterSplit(doubleAfterSplit);
		simulation.setGames(games);
		simulation.setInitialMoney(initialMoney);
		simulation.setResplitAces(resplitAces);
		simulation.setPeek(peek);
		simulation.setBet(bet);
		simulation.setCardShuffler(shuffler);
		simulation.setPlayer(basicPlayer);
		simulation.setCardShuffler(shuffler);
		simulation.setDoubleRules(doubleOn);
		return simulation;
	}
	
}
