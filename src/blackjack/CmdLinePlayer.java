/*
 * (C) 2012 Kerio Technologies s.r.o.
 */
package blackjack;

import blackjack.engine.*;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import java.io.IOException;
import java.util.Set;

/**
 *
 * @author mbarnas
 */
public class CmdLinePlayer implements Player {

	private BiMap<Move, Character> possibleMoves = new ImmutableBiMap.Builder<Move, Character>()
			.put(Move.Stand, 's')
			.put(Move.Hit, 'h')
			.put(Move.Split, 'p')
			.put(Move.Double, 'd')
			.build();
	
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
    public void gameEnded(Game game) {
        BlackJack.println("Result: " + game.gameResult());
        BlackJack.printCards(game);
        BlackJack.println("Player's credit: " + this.getMoney());
        BlackJack.println("-----------------------------");
    }

    @Override
    public Move move(CardHand cards, Card dealerUpCard, Set<Move> allowedMoves) {
        BlackJack.printCards(cards);
        BlackJack.println("Dealer card: " + dealerUpCard);
        while (true) {
            try {
                char c = BlackJack.getChoice("Move", getAllovedMoves(allowedMoves));
                if (c == 's') {
                    return Move.Stand;
                }
                if (c == 'h') {
                    return Move.Hit;
                }
                if (c == 'p') {
                    return Move.Split;
                }
                if (c == 'd') {
                    return Move.Double;
                }
            } catch (IOException ex) {
            }
        }
    }

    int getMoney() {
        return this.amount;
    }

	private String getAllovedMoves(Set<Move> allowedMoves) {
		StringBuilder sb = new StringBuilder();
		for (Move m : allowedMoves) {
			sb.append(possibleMoves.get(m));
		}
		return sb.toString();
	}
}
