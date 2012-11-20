/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack.simulation;

import blackjack.engine.CardShuffler;
import blackjack.engine.CardSource;
import blackjack.engine.Engine;
import blackjack.engine.IllegalMoveException;
import blackjack.engine.Rules;
import blackjack.engine.rules.BasicRules;
import blackjack.simulation.player.*;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Names;

/**
 *
 * @author mbarnas
 */
public class Simulation {

    public static final int GAMES = 1000000;
    public static final int initialMoney = 500000;
    private Engine engine;
    private BasePlayer player;
    private static Injector injector;

    private static class SimulationModule extends AbstractModule {

        @Override
        protected void configure() {
            bind(Rules.class).to(BasicRules.class);
            bind(CardSource.class).to(CardShuffler.class);
            bind(Integer.class).annotatedWith(Names.named(CardShuffler.DECKS)).toInstance(6);
            bind(Long.class).annotatedWith(Names.named(CardShuffler.SEED)).toInstance(3142L);
            bind(Integer.class).annotatedWith(Names.named(BasicStrategyPlayer.DEPOSIT)).toInstance(initialMoney);
			bind(BasePlayer.class).to(BasicStrategyPlayer.class);
        }
    }

    public static void main(String[] args) {
        injector = Guice.createInjector(new SimulationModule());
		new Simulation(injector.getInstance(BasicStrategyPlayer.class)).run();
		new Simulation(injector.getInstance(SimpleCountingPlayer.class)).run();
    }

    public Simulation(BasePlayer player) {
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
