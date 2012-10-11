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
	private Engine engine;
	private BlackJackPlayer player;
	
	public static void main(String[] args) {
		new Simulation(new StandPlayer(10000)).run();
		new Simulation(new HitPlayer(10000)).run();
		new Simulation(new OneHitPlayer(10000)).run();
	}

	public Simulation(BlackJackPlayer player) {
		this.engine = new Engine(new CardShuffler(1));
		this.player = player;
		this.engine.addPlayer(player);
	}
	
	public void run() {
		for (int i = 0; i < 1000; i++) {
			this.engine.start();
			
			while (this.engine.getGameState() == GameResult.Continuing) {
				this.engine.nextDraw();
			}
			
			if (this.player.getMoney() < 10)
				break;
		}
		
		System.out.println("Final result: " + this.player.getMoney());
	}

	public HitPlayer getPlayer() {
		return new HitPlayer(10000);
	}
}
