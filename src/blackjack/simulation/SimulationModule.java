/*
 * (C) 2012 Kerio Technologies s.r.o.
 */
package blackjack.simulation;

import blackjack.engine.CardShuffler;
import blackjack.engine.CardSource;
import blackjack.engine.Rules;
import blackjack.engine.rules.BasicRules;
import blackjack.engine.shufflers.TwoThirdsShuffler;
import com.google.inject.AbstractModule;

/**
 *
 * @author mbarnas
 */
class SimulationModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(Rules.class).to(BasicRules.class);
        bind(CardSource.class).to(TwoThirdsShuffler.class);
        bind(Integer.class).toInstance(6);
    }
}
