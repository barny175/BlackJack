/*
 * (C) 2012 Kerio Technologies s.r.o.
 */
package blackjack;

import blackjack.engine.Card;
import blackjack.engine.GameResult;
import blackjack.engine.Move;
import blackjack.engine.Player;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mbarnas
 */
public class CmdLinePlayer implements Player{
    private int amount;
    private List<Card> cards = new ArrayList<Card>();

    public CmdLinePlayer(int amount) {
        this.amount = amount;
    }
    
    @Override
    public void resetCards() {
        this.cards.clear();
    }
    
    @Override
    public int getBet() {
        int bet = 0;
        while (bet <= 0  || bet > amount) {
            try {
                System.out.print("Bet:");
                String s = BlackJack.reader.readLine();
                bet = Integer.parseInt(s);
            } catch (IOException ex) {
            } catch (NumberFormatException ex) {
                
            }
        }
    
        this.amount -= bet;
        return bet;
    }

    @Override
    public void addMoney(int amount) {
        this.amount += amount;
    }

    @Override
    public void addCard(Card card) {
        this.cards.add(card);
    }

    @Override
    public List<Card> getCards() {
        return this.cards;
    }

    @Override
    public void result(GameResult result) {
    }

    @Override
    public Move move() {
        while (true) {
            try {
                char c = BlackJack.getChoice("Move", "sh");
                if (c == 's')
                    return Move.Stand;
                if (c == 'h')
                    return Move.Hit;
            } catch (IOException ex) {
            }
        }
    }

    int getMoney() {
        return this.amount;
    }
    
}
