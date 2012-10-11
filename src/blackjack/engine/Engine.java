/*
 * (C) 2012 Kerio Technologies s.r.o.
 */
package blackjack.engine;

import java.util.List;

/**
 *
 * @author mbarnas
 */
public class Engine {

    private Player player;
    private Dealer dealer = new Dealer();
    private CardSource cardSource;
    private int bet;
    private GameResult gameState = GameResult.Continuing;
    private boolean isFirstDraw = true;
    private Move playerMove;
    private Move dealerMove;

    public GameResult getGameState() {
        return gameState;
    }

    public Engine(CardSource cardSource) {
        this.cardSource = cardSource;
    }

    public List<Card> getDealerCards() {
        return this.dealer.getCards();
    }

    public void addPlayer(Player player) {
        this.player = player;
    }

    public void start() {
        reset();
        
        bets();

        firstDeal();
    }

    private void reset() {
        cardSource.shuffle();
        
        resetPlayers();
        
        this.dealerMove = null;
        this.playerMove = null;
        this.gameState = GameResult.Continuing;
        
        isFirstDraw = true;
    }

    public void nextDraw() {
        if (this.playerMove != Move.Stand) {
            this.playerMove = player.move();
            if (this.playerMove == Move.Hit) {
                deal(player);
            }
        }
        
        if (isFirstDraw) {
            revealDealer();
        }

        this.dealerMove = dealer.move();
        if (this.dealerMove == Move.Hit) {
            deal(dealer);
        }

        checkGameState();
    }

    protected void firstDeal() {
        deal(player);
        deal(dealer);

        deal(player);
        deal(dealer);

        checkGameState();
    }

    protected void deal(CardHolder cardHolder) {
        Card card = cardSource.next();
        if (card == null) {
            throw new LastCardException();
        }

        cardHolder.addCard(card);
    }

    private void bets() {
        bet = player.getBet();
    }

    private void payPrizes(GameResult res) {
        switch (res) {
            case PlayerBlackJack:
                this.player.addMoney(bet + bet * 3 / 2);
                break;
            case Push:
                this.player.addMoney(bet);
                break;
            case PlayerWin:
                this.player.addMoney(2 * bet);
                break;
        }
    }

    private void checkGameState() {
        BlackJackSum.Sum sumPlayer = BlackJackSum.sum(player.getCards());

        if (isFirstDraw && sumPlayer.isBlackJack) {
            revealDealer();
        }

        BlackJackSum.Sum sumDealer = BlackJackSum.sum(dealer.getCards());

        if (sumPlayer.sum > 21) {
            gameState = GameResult.PlayerBusted;
        } else {
            if (sumPlayer.isBlackJack) {
                if (sumDealer.isBlackJack) {
                    this.gameState = GameResult.Push;
                } else {
                    this.gameState = GameResult.PlayerBlackJack;
                }
            } else {
                if (sumDealer.sum > 21) {
                    this.gameState = GameResult.PlayerWin;
                }
            }
        }

        if (this.playerMove == Move.Stand && this.dealerMove == Move.Stand) {
            if (sumPlayer.sum > sumDealer.sum) {
                this.gameState = GameResult.PlayerWin;
            } else if (sumPlayer.sum == sumDealer.sum) {
                this.gameState = GameResult.Push;
            } else {
                this.gameState = GameResult.DealerWin;
            }
        }

        payPrizes(gameState);

        player.result(this.gameState);
    }

    private void revealDealer() {
        this.dealer.revealHoleCard();
        isFirstDraw = false;
    }

    private void resetPlayers() {
        dealer.resetCards();
        player.resetCards();
    }
}
