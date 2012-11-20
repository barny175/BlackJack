/*
 * (C) 2012 Kerio Technologies s.r.o.
 */
package blackjack;

import blackjack.engine.Card;
import blackjack.engine.CardSource;
import blackjack.engine.Engine;
import blackjack.engine.Player;
import blackjack.engine.Rules;
import blackjack.engine.rules.BasicRules;
import blackjack.simulation.SimulationCardShuffler;
import blackjack.simulation.player.BasicStrategyPlayer;
import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.name.Names;

/**
 *
 * @author mbarnas
 */
class BasicModule extends AbstractModule {

    @Override
    protected void configure() {
		bind(Engine.class).in(Scopes.SINGLETON);
		
        bind(Rules.class).to(BasicRules.class);
//        bind(CardSource.class).to(CardShuffler.class);
		bind(CardSource.class).toInstance(new SimulationCardShuffler(6, Card.TWO, Card.SEVEN, Card.TWO, Card.TEN, Card.TEN, Card.TEN));
		bind(Integer.class).annotatedWith(Names.named(BasicStrategyPlayer.DEPOSIT)).toInstance(100);
		bind(Player.class).to(BasicStrategyPlayer.class);
    }
}
