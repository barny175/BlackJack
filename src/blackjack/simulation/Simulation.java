/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack.simulation;

import blackjack.simulation.player.*;
import blackjack.engine.CardShuffler;
import blackjack.engine.Engine;
import blackjack.engine.GameResult;

/**
 *
 * @author mbarnas
 */
public class Simulation {
    public static final int initialMoney = 20000;
	private Engine engine;
	private BasePlayer player;
	
	public static void main(String[] args) {
		new Simulation(new StandPlayer(initialMoney)).run();
		new Simulation(new HitPlayer(initialMoney)).run();
		new Simulation(new OneHitPlayer(initialMoney)).run();
        new Simulation(new SimplePlayer(initialMoney)).run();
		new Simulation(new BasicStrategyPlayer(initialMoney)).run();
	}

	public Simulation(BasePlayer player) {
		this.engine = new Engine(new CardShuffler(1));
		this.player = player;
		this.engine.addPlayer(player);
	}
	
	public void run() {
		for (int i = 0; i < 10000; i++) {
			this.engine.newGame();
			
			while (this.engine.getGameState() == GameResult.Continuing) {
				this.engine.start();
			}
			
			if (this.player.getMoney() < 10) {
                System.out.println("Short of money after " + i + " rounds.");
				break;
            }
		}
        
        final int result = this.player.getMoney();
		System.out.println("Final result: " + result + " (" + result * 100 / initialMoney + "%)");
	}
}
