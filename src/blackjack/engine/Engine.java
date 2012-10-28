/*
 * (C) 2012 Kerio Technologies s.r.o.
 */
package blackjack.engine;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

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

    public Engine(CardSource cardSource) {
        this.cardSource = cardSource;
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

    public void start() {
        int continuingGames = 0;
        Game game = this.games.poll();
        while (game != null) {
            checkBlackJack(game);

            if (game.gameState() == GameState.Continuing) {
                playersGame(game);

                if (game.gameState() == GameState.Continuing) {
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

    private void playersGame(Game game) {
        Move move;
        do {
            move = player.move(game.playerCards(), this.getDealerUpCard());
            switch (move) {
                case Hit:
                case Double:
                    dealToPlayer(game);

                    if (move == Move.Double) {
                        game.setBet(game.getBet() * 2);
                    }
                    if (checkBusted(game.playerCards())) {
                        game.setGameState(GameState.PlayerBusted);
                        return;
                    }
                    break;
                case Split:
                    List<Game> split = game.split();
                    for (Game g : split) {
                        dealToPlayer(g);
                        this.games.addFirst(g);
                    }
                    break;
            }
        } while (move == Move.Hit);
    }

    protected void firstDeal(Game game) {
        dealToPlayer(game);

        dealToDealer();

        dealToPlayer(game);

        dealToDealer();
    }

    private void dealToPlayer(Game game) {
        game.addPlayerCard(getNextCard());
    }

    private void dealToDealer() {
        Card card = getNextCard();
        if (dealerUpCard == null) {
            dealerUpCard = card;
        }

        dealerCards.addCard(card);
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
        switch (game.gameState()) {
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
        CardHand sumPlayer = game.playerCards();
        CardHand sumDealer = dealerCards;

        if (sumPlayer.isBlackJack()) {
            if (sumDealer.isBlackJack()) {
                game.setGameState(GameState.Push);
            } else {
                game.setGameState(GameState.PlayerBlackJack);
            }
        } else {
            if (sumDealer.isBlackJack()) {
                game.setGameState(GameState.DealerWin);
            }
        }
    }

    private boolean checkBusted(CardHand cards) {
        if (cards.softSum() > 21) {
            return true;
        }

        return false;
    }

    private void checkGameState(Game game) {
        CardHand playerCards = game.playerCards();

        if (game.gameState() != GameState.Continuing) {
            return;
        }

        if (checkBusted(playerCards)) {
            game.setGameState(GameState.PlayerBusted);
            return;
        }

        if (checkBusted(dealerCards)) {
            game.setGameState(GameState.PlayerWin);
            return;
        }

        if (playerCards.softSum() > dealerCards.softSum()) {
            game.setGameState(GameState.PlayerWin);
        } else if (playerCards.softSum() == dealerCards.softSum()) {
            game.setGameState(GameState.Push);
        } else {
            game.setGameState(GameState.DealerWin);
        }
    }

    private void endGame(Game game) {
        payPrizes(game);

        player.result(game.gameState());
    }

    public CardHand getDealerCards() {
        return dealerCards;
    }

    private void splitGame(Game game) {
        List<Game> splitGame = game.split();
    }

    private void replace(ListIterator<Game> it, List<Game> split) {
        it.add(split.get(0));
        it.previous();
        it.add(split.get(1));
        it.previous();
        it.previous();
        it.remove();
    }
}
