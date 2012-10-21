/*
 * (C) 2012 Kerio Technologies s.r.o.
 */
package blackjack;

import blackjack.engine.*;
import java.io.IOException;

/**
 *
 * @author mbarnas
 */
public class CmdLinePlayer implements Player{
    private int amount;
    private CardHand cards = new CardHand();
    private Game currentGame;

    public CmdLinePlayer(int amount) {
        this.amount = amount;
    }
    
    @Override
    public void reset() {
        this.cards.reset();
    }
    
    @Override
    public int bet() {
        int bet = 0;
        while (bet <= 0  || bet > amount) {
            try {
                System.out.print("Bet:");
                String s = BlackJack.reader.readLine();
                bet = s.isEmpty() ? 10 : Integer.parseInt(s);
            } catch (IOException ex) {
            } catch (NumberFormatException ex) {
                
            }
        }
    
        return bet;
    }

    @Override
    public void addMoney(int amount) {
        this.amount += amount;
    }

    @Override
    public void addCard(Card card) {
        this.cards.addCard(card);
    }

    @Override
    public void result(GameResult result) {
        BlackJack.println("Player credit: " + getMoney());
    }

    @Override
    public Move move() {
        BlackJack.printCards(getCards());
        BlackJack.println("Dealer card: " + this.currentGame.getDealerUpCard());
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

    @Override
    public void setGame(Game game) {
        this.currentGame = game;
    }

    @Override
    public CardHand getCards() {
        return cards;
    }
}
