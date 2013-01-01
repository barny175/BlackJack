/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack.simulation;

import blackjack.engine.*;
import blackjack.engine.rules.*;
import blackjack.simulation.player.BasePlayer;
import blackjack.simulation.player.BasicStrategyPlayer;
import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

/**
 *
 * @author mbarnas
 */
public class SimulationModule extends AbstractModule {
	private final int initialMoney;
	private final CardSource cardSrc;

	public SimulationModule(int initialMoney, CardSource cardSrc) {
		this.initialMoney = initialMoney;
		this.cardSrc = cardSrc;
	}
	
	@Override
	protected void configure() {
		bind(Rules.class).to(BasicRules.class);
		bind(SplitRules.class).to(BasicSplitRules.class);
		bind(DoubleRules.class).to(BasicDoubleRules.class);
		bind(Boolean.class).annotatedWith(Peek.class).toInstance(Boolean.TRUE);
		bind(Boolean.class).annotatedWith(DoubleAfterSplit.class).toInstance(Boolean.TRUE);
		
		bind(CardSource.class).toInstance(cardSrc);
		bind(Integer.class).annotatedWith(Names.named(CardShuffler.DECKS)).toInstance(6);
//		bind(Long.class).annotatedWith(Names.named(CardShuffler.SEED)).toInstance(3112L);
		bind(Long.class).annotatedWith(Names.named(CardShuffler.SEED)).toInstance(System.currentTimeMillis());
		bind(Integer.class).annotatedWith(Names.named(BasicStrategyPlayer.DEPOSIT)).toInstance(initialMoney);
		bind(BasePlayer.class).to(BasicStrategyPlayer.class);
	}
}
