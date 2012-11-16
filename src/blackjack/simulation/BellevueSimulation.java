/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack.simulation;

import blackjack.engine.*;
import blackjack.engine.rules.BellevueRules;
import blackjack.engine.shufflers.TwoThirdsShuffler;
import blackjack.simulation.player.*;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Names;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mbarnas
 */
public class BellevueSimulation {

	public static final int GAMES = 100000;
	public static final int initialMoney = 100000;
	private Engine engine;
	private BasePlayer player;
	private static Injector injector;

	private static class BellevueSimulationModule extends AbstractModule {

		@Override
		protected void configure() {
			bind(Rules.class).to(BellevueRules.class);
			bind(CardSource.class).to(TwoThirdsShuffler.class);
			bind(Integer.class).toInstance(6);
            bind(Long.class).toInstance(6211L);
			bind(Integer.class).annotatedWith(Names.named(BasicStrategyPlayer.DEPOSIT)).toInstance(initialMoney);
            try {
                bind(PrintWriter.class).toInstance(new PrintWriter("equity.csv"));
            } catch (FileNotFoundException ex) {
                Logger.getLogger(BellevueSimulation.class.getName()).log(Level.SEVERE, null, ex);
		}
            bind(Player.class).annotatedWith(Names.named("DECORATED_PLAYER")).to(BellevuePlayer.class);
	}
    }

	public static void main(String[] args) {
		injector = Guice.createInjector(new BellevueSimulationModule());
        new BellevueSimulation(injector.getInstance(EquityCurvePlayer.class)).run();
	}

	public BellevueSimulation(BasePlayer player) {
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
				System.out.println("Illegal move.");
			}

			if (this.player.getMoney() < 10) {
				System.out.println(String.format("%-20s: short of money after %d rounds. ", player.getName(), i));
				break;
			}
		}

		final int result = this.player.getMoney();
		if (result > 10) {
			System.out.println(String.format("%-20s: %d (%d%%)", player.getName(), result, result * 100 / initialMoney));
		}
	}
}
