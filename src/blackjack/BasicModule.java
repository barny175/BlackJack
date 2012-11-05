/*
 * (C) 2012 Kerio Technologies s.r.o.
 */
package blackjack;

import blackjack.engine.CardShuffler;
import blackjack.engine.CardSource;
import blackjack.engine.Player;
import blackjack.engine.Rules;
import blackjack.engine.rules.BasicRules;
import blackjack.simulation.player.BasicStrategyPlayer;
import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

/**
 *
 * @author mbarnas
 */
class BasicModule extends AbstractModule {
	public static final String DEPOSIT = "deposit";

    @Override
    protected void configure() {
        bind(Rules.class).to(BasicRules.class);
        bind(CardSource.class).to(CardShuffler.class);
		bind(Integer.class).annotatedWith(Names.named(DEPOSIT)).toInstance(100);
		bind(Player.class).to(BasicStrategyPlayer.class);
    }
}
