/*
 * (C) 2012 Kerio Technologies s.r.o.
 */
package blackjack.engine;

import blackjack.engine.Rules.Action;
import blackjack.engine.rules.BasicRules;
import com.google.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 *
 * @author mbarnas
 */
public class Engine {

    private Player player;
    private Dealer dealer = new Dealer();
    private CardSource cardSource;
    private Card dealerUpCard;
    private LinkedList<Game> games;
    private CardHand dealerCards;
    private List<Game> endedGames;
    private Rules rules;

    @Inject
    public Engine(Rules rules, CardSource cardSource) {
        this.cardSource = cardSource;
        this.rules = rules;
    }

    public Card getDealerUpCard() {
        return this.dealerUpCard;
    }

    public void addPlayer(Player player) {
        this.player = player;
    }

    public Game newGame() {
        cardSource.shuffle();

        final Game game = new Game(this, player);

        this.games = new LinkedList<Game>();
        this.games.add(game);

        dealerCards = new CardHand();

        this.endedGames = new LinkedList<Game>();
        
        bet(game);

        firstDeal(game);

        return game;
    }

    public void start() throws IllegalMoveException {
        int continuingGames = 0;
        Game game = this.games.poll();
        while (game != null) {
            if (game.gameState() == GameState.AfterSplit) {
                dealToPlayer(game);
                game.setGameState(GameState.FirstDeal);
            }
                
            checkBlackJack(game);
            
            if (game.gameState() == GameState.FirstDeal) {
                playersGame(game);

                if (game.gameState() == GameState.PlayersGame && !game.isSplitted()) {
                    continuingGames++;
                }
            }
            
            if (!game.isSplitted())
                this.endedGames.add(game);
                    
            game = this.games.poll();
        }

        if (continuingGames > 0) {
            dealersGame();
        }

        for (Game g : this.endedGames) {
            checkGameState(g);
            endGame(g);
        }
    }

    private void checkIfMoveIsAllowed(Game game, Move move) throws IllegalMoveException {
        Set<Move> allowedMoves = rules.getAllowedMoves(game);
        if (!allowedMoves.contains(move))
            throw new IllegalMoveException();
    }

    private void dealersGame() {
        Move dealerMove;
        do {
            dealerMove = dealer.move(this.getDealerCards());
            if (dealerMove == Move.Hit) {
                dealToDealer();

                if (checkBusted(getDealerCards())) {
                    break;
                }
            }
        } while (dealerMove != Move.Stand);
    }

    private void playersGame(Game game) throws IllegalMoveException {
        Move move;
        do {
            move = player.move(game.playerCards(), this.getDealerUpCard());
            
            checkIfMoveIsAllowed(game, move);
            
            switch (move) {
                case Double:
                case Hit:                
                    dealToPlayer(game);

                    if (move == Move.Double) {
                        game.setBet(game.getBet() * 2);
                    }
                    if (checkBusted(game.playerCards())) {
                        game.setGameResult(GameResult.PlayerBusted);
                        return;
                    }
                    break;
                case Split:
                    List<Game> split = game.split();
                    for (Game g : split) {
                        this.games.addFirst(g);
                    }
                    break;
            }
            game.setGameState(GameState.PlayersGame);
        } while (move == Move.Hit);
    }

    protected void firstDeal(Game game) {
		assert game.gameState() == GameState.Started;
		
        dealToPlayer(game);

        dealerUpCard = dealToDealer();

        dealToPlayer(game);

        dealToDealer();
		
		game.setGameState(GameState.FirstDeal);
    }

    private void dealToPlayer(Game game) {
        game.addPlayerCard(getNextCard());
    }

    private Card dealToDealer() {
        Card card = getNextCard();
        dealerCards.addCard(card);
        return card;
    }

    protected Card getNextCard() {
        Card card = cardSource.next();
        if (card == null) {
            throw new LastCardException();
        }

        return card;
    }

    private void bet(Game game) {
        game.setBet(player.bet());
    }

    private void payPrizes(Game game) {
        switch (game.gameResult()) {
            case PlayerBlackJack:
                game.getPlayer().addMoney(game.getBet() * 3 / 2);
                break;
            case Push:
                break;
            case PlayerWin:
                game.getPlayer().addMoney(game.getBet());
                break;
            case PlayerBusted:
            case DealerWin:
                game.getPlayer().addMoney(-1 * game.getBet());
                break;
        }
    }

    private void checkBlackJack(Game game) {
		assert game.gameState() == GameState.FirstDeal;
		
        CardHand sumPlayer = game.playerCards();
        CardHand sumDealer = dealerCards;

        if (rules.isBlackJack(sumPlayer)) {
            if (rules.isBlackJack(sumDealer)) {
                setGameResult(game, GameResult.Push);
            } else {
                setGameResult(game, GameResult.PlayerBlackJack);
            }
        } else {
            if (rules.isBlackJack(sumDealer)) {
                setGameResult(game, GameResult.DealerWin);
            }
        }
    }

    private void setGameResult(blackjack.engine.Game game, GameResult result) {
        game.setGameResult(result);
        game.setGameState(GameState.End);
    }

    private boolean checkBusted(CardHand cards) {
        if (cards.softSum() > 21) {
            return true;
        }

        return false;
    }

    private void checkGameState(Game game) {
        CardHand playerCards = game.playerCards();

        if (game.gameState() != GameState.PlayersGame) {
            return;
        }

        if (checkBusted(playerCards)) {
            setGameResult(game, GameResult.PlayerBusted);
            return;
        }

        if (checkBusted(dealerCards)) {
            setGameResult(game, GameResult.PlayerWin);
            return;
        }

        if (playerCards.softSum() > dealerCards.softSum()) {
            setGameResult(game, GameResult.PlayerWin);
        } else if (playerCards.softSum() == dealerCards.softSum()) {
            setGameResult(game, GameResult.Push);
        } else {
            setGameResult(game, GameResult.DealerWin);
        }
    }

    private void endGame(Game game) {
        payPrizes(game);

        player.gameEnded(game);
    }

    public CardHand getDealerCards() {
        return dealerCards;
    }
}
