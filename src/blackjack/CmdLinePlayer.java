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

    public CmdLinePlayer(int amount) {
        this.amount = amount;
    }
    
    @Override
    public int bet() {
        int bet = 10;
//        while (bet <= 0  || bet > amount) {
//            try {
//                System.out.print("Bet:");
//                String s = BlackJack.reader.readLine();
//                bet = s.isEmpty() ? 10 : Integer.parseInt(s);
//            } catch (IOException ex) {
//            } catch (NumberFormatException ex) {
//                
//            }
//        }
    
        return bet;
    }

    @Override
    public void addMoney(int amount) {
        this.amount += amount;
        BlackJack.println("Player won: " + amount);
    }

    @Override
    public void result(GameState result) {
    }

    @Override
    public Move move(CardHand cards, Card dealerUpCard) {
        BlackJack.printCards(cards);
        BlackJack.println("Dealer card: " + dealerUpCard);
        while (true) {
            try {
                char c = BlackJack.getChoice("Move", "hsdp");
                if (c == 's')
                    return Move.Stand;
                if (c == 'h')
                    return Move.Hit;
                if (c == 'p')
                    return Move.Split;
				if (c == 'd')
                    return Move.Double;
            } catch (IOException ex) {
            }
        }
    }

    int getMoney() {
        return this.amount;
    }
}
