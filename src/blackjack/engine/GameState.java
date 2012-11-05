/*
 * (C) 2012 Kerio Technologies s.r.o.
 */
package blackjack.engine;

/**
 *
 * @author mbarnas
 */
public enum GameState {
	Started,
    FirstDeal,
    Continuing,
    PlayerFinished,
	DealerFinished,
	End;
}
