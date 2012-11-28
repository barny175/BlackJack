/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack.simulation;

import blackjack.engine.*;
import blackjack.engine.shufflers.EveryGameCardShuffler;
import blackjack.simulation.player.*;
import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 *
 * @author mbarnas
 */
public class StrategyFinder {

	public static final int GAMES = 1000000;
	public static final int initialMoney = 500000;
	private Engine engine;
	private BasePlayer player;
	private static Injector injector;

	public static void main(String[] args) {
		injector = Guice.createInjector(new SimulationModule(initialMoney, new SimulationCardShuffler(1, Card.TWO, Card.TWO, Card.TWO)));
		new StrategyFinder(injector.getInstance(BasicStrategyPlayer.class)).run();
		new StrategyFinder(injector.getInstance(SimpleCountingPlayer.class)).run();
	}

	public StrategyFinder(BasePlayer player) {
		this.engine = injector.getInstance(Engine.class);
		this.player = player;
		this.engine.addPlayer(player);
	}

	public void run() {
		for (int i = 0; i < GAMES; i++) {
			this.engine.newGame();
			try {
				this.engine.start();
			} catch (IllegalMoveException ex) {
				System.out.print("Illegal move.");
			}

			if (this.player.getMoney() < 10) {
				System.out.println(String.format("%-20s: short of money after %d rounds. ", player.getName(), i));
				break;
			}
		}

		final int result = this.player.getMoney();
		if (result > 10) {
			System.out.println(String.format("%-20s: %d (%d%%, %f%%)", player.getName(), result, result * 100 / initialMoney,
					((float) (initialMoney - result)) / GAMES * 10));
		}
	}
}
