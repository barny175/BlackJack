/*
 * (C) 2012 Kerio Technologies s.r.o.
 */
package blackjack;

import blackjack.engine.CardShuffler;
import blackjack.engine.CardSource;
import blackjack.engine.Rules;
import blackjack.engine.rules.BasicRules;
import com.google.inject.AbstractModule;

/**
 *
 * @author mbarnas
 */
class BasicModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(Rules.class).to(BasicRules.class);
        bind(CardSource.class).to(CardShuffler.class);
    }
    
}
