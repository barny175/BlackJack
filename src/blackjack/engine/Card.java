/*
 * (C) 2012 Kerio Technologies s.r.o.
 */
package blackjack.engine;

/**
 *
 * @author mbarnas
 */
public enum Card {
    ACE(1, 11),
    TWO(2),
    THREE(3),
    FOUR(4), 
    FIVE(5),
    SIX(6),
    SEVEN(7),
    EIGHT(8),
    NINE(9),
    TEN(10),
    JACK(10), 
    QUEEN(10),
    KING(10);
    
    private final int value;
    private final int softValue;

    private Card(int value) {
        this.value = value;
        this.softValue = value;
    }
    
    private Card(int value, int softVal) {
        this.value = value;
        this.softValue = softVal;
    }
    
    public int getValue() {
        return this.value;
    }

    public int getSoftValue() {
        return softValue;
    }
}
