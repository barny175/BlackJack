/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack.simulation;

import blackjack.engine.Engine;
import blackjack.engine.shufflers.TwoThirdsShuffler;
import blackjack.simulation.player.*;

/**
 *
 * @author mbarnas
 */
public class Simulation {
    public static final int GAMES = 10000;
    public static final int initialMoney = 500000;
	private Engine engine;
	private BasePlayer player;
	
	public static void main(String[] args) {
		new Simulation(new StandPlayer(initialMoney)).run();
//		new Simulation(new HitPlayer(initialMoney)).run();
		new Simulation(new DealersStrategyPlayer(initialMoney)).run();
		new Simulation(new OneHitPlayer(initialMoney)).run();
        new Simulation(new SimplePlayer(initialMoney)).run();
		new Simulation(new BasicStrategyPlayer(initialMoney)).run();
	}

	public Simulation(BasePlayer player) {
		this.engine = new Engine(new TwoThirdsShuffler(6));
		this.player = player;
		this.engine.addPlayer(player);
	}
	
	public void run() {
		for (int i = 0; i < GAMES; i++) {
			this.engine.newGame();

			this.engine.start();
			
			if (this.player.getMoney() < 10) {
                System.out.println(String.format("%-20s: short of money after %d rounds. ", player.getName(), i));
				break;
            }
		}
        
        final int result = this.player.getMoney();
		if (result > 10)
			System.out.println(String.format("%-20s: %d (%d%%)", player.getName(), result, result * 100 / initialMoney));
	}
}
