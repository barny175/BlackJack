/*
 * (C) 2012 Kerio Technologies s.r.o.
 */
package blackjack.engine;

/**
 *
 * @author mbarnas
 */
public enum GameState {
    Continuing,
    PlayerBusted,
    PlayerWin,
    DealerWin,
    PlayerBlackJack,
    Push;    
}
