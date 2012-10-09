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

    private Move playerMove;
    private Move dealerMove;

    public GameResult getGameState() {
        return gameState;
    }
    
    public Engine(CardSource cardSource) {
        this.cardSource = cardSource;
    }

    public void addPlayer(Player player) {
        this.player = player;
    }

    public void start() {
        bets();
        firstDeal();
    }

    public void continueGame() {
        this.playerMove = player.move();
        if (this.playerMove == Move.Hit) {
            deal(player);
        }
        
        this.dealerMove = dealer.move();
        if (this.dealerMove== Move.Hit) {
            deal(dealer);
        }
        
        checkGameState();
    }

    protected void firstDeal() {
        deal(player);
        deal(dealer);

        dealAll();
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

    private void dealAll() {
        deal(player);
        deal(dealer);

        checkGameState();
    }

    private void checkGameState() {
        BlackJackSum.Sum sumPlayer = BlackJackSum.sum(player.getCards());
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
            if (sumPlayer.sum > sumDealer.sum)
                this.gameState = GameResult.PlayerWin;
            else if (sumPlayer.sum == sumDealer.sum)
                this.gameState = GameResult.Push;
            else
                this.gameState = GameResult.DealerWin;
        }
        
        payPrizes(gameState);

        player.result(this.gameState);
    }
}
