/*
 * (C) 2012 Kerio Technologies s.r.o.
 */
package blackjack.engine;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mbarnas
 */
public class Game {

    private int bet;
    private final Player player;
    private final CardHand playerCards = new CardHand();
    private GameState gameState = GameState.Started;
    private final Engine engine;
    private boolean splitted = false;
    private GameResult result;

    public Game (Engine engine, Player player) {
        this.player = player;
        this.engine = engine;
    }

    public GameResult getGameResult() {
        return result;
    }

    public void setGameResult(GameResult result) {
        this.result = result;
    }

	public Card dealerUpCard() {
        return engine.getDealerUpCard();
    }

    public CardHand playerCards() {
        return playerCards;
    }

    public GameState gameState() {
        return gameState;
    }

    public Player getPlayer() {
        return player;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public void addPlayerCard(Card c) {
        playerCards.addCard(c);
    }

    public void setBet(int bet) {
        this.bet = bet;
    }

    public int getBet() {
        return bet;
    }
    
    public List<Game> split() {
        if (playerCards.count() != 2)
            throw new RuntimeException("Split is not possible.");
		
		List<Game> games = new ArrayList<Game>(2);
        Game game1 = new Game(engine, player);
        game1.setBet(bet);
        game1.addPlayerCard(this.playerCards().getCards().get(0));
		game1.playerCards().setSplitted(true);
        games.add(game1);
        
        Game game2 = new Game(engine, player);
        game2.setBet(bet);
        game2.addPlayerCard(this.playerCards().getCards().get(1));
		game2.playerCards().setSplitted(true);
        games.add(game2);

        this.splitted = true;
        
        return games;
    }

    public boolean isSplitted() {
        return splitted;
    }
}
