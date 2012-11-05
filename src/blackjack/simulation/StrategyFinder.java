/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack.simulation;

import blackjack.engine.*;
import blackjack.engine.rules.BasicRules;
import blackjack.simulation.player.BasePlayer;
import blackjack.simulation.player.BasicStrategyPlayer;
import blackjack.simulation.player.HitPlayer;
import blackjack.simulation.player.OneHitPlayer;
import blackjack.simulation.player.StandPlayer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mbarnas
 */
public class StrategyFinder {

	private final Engine engine;
	private final BasePlayer player;
	public static final int initialMoney = 20000;

	public static void main(String[] args) {
		new StrategyFinder(new StandPlayer(initialMoney)).run();
		new StrategyFinder(new HitPlayer(initialMoney)).run();
		new StrategyFinder(new OneHitPlayer(initialMoney)).run();
		new StrategyFinder(new BasicStrategyPlayer(initialMoney)).run();
	}

	public StrategyFinder(BasePlayer player) {
		this.engine = new Engine(new BasicRules(), new SimulationCardShuffler(6, Card.TWO, Card.TWO, Card.TWO));
		this.player = player;
		this.engine.addPlayer(player);

	}

	public void run() {
		for (int i = 0; i < 10000; i++) {
			Game game = this.engine.newGame();

			if (game.gameState() == GameState.Continuing) {
                try {
                    this.engine.start();
                } catch (IllegalMoveException ex) {
                    System.out.print("Illegal move.");
                }
			}

			if (this.player.getMoney() < 10) {
				System.out.print("Short of money after " + i + " rounds. ");
				break;
			}
		}

		final int result = this.player.getMoney();
		System.out.println("Final result for " + player.getName() + ": " + result + " (" + result * 100 / initialMoney + "%)");
	}
}
