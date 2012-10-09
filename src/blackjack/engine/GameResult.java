/*
 * (C) 2012 Kerio Technologies s.r.o.
 */
package blackjack.engine;

/**
 *
 * @author mbarnas
 */
public enum GameResult {
    Continuing,
    PlayerBusted,
    PlayerWin,
    DealerWin,
    PlayerBlackJack,
    Push;    
}
