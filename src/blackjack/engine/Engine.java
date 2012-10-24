/*
 * (C) 2012 Kerio Technologies s.r.o.
 */
package blackjack.engine;

/**
 *
 * @author mbarnas
 */
public class Engine {

    private Player player;
    private Dealer dealer = new Dealer();
    private CardSource cardSource;
    private int bet;
	private Card dealerUpCard;
	private CurrentGame game;
	private CardHand dealerCards;

    public Engine(CardSource cardSource) {
        this.cardSource = cardSource;
    }

    public Card getDealerUpCard() {
        return this.dealerUpCard;
    }

    public void addPlayer(Player player) {
        this.player = player;
    }

    public CurrentGame newGame() {
		cardSource.shuffle();

        this.game = new CurrentGame(player);

        this.player.addGame(game);
		
		dealerCards = new CardHand();

        bets();

        firstDeal();
		
		return this.game;
    }
	
    public void start() {
        checkBlackJack(this.game);
        boolean busted = false;

        if (game.gameState() == GameState.Continuing) {
            Move move;
            do {
                move = player.move(game.playerCards());
                if (move == Move.Hit || move == Move.Double) {
                    deal(game.playerCards());

                    if (move == Move.Double) {
                        this.bet *= 2;
                    }
                    busted = checkBusted(game.playerCards());
                }
            } while (move == Move.Hit && !busted);

            if (!busted) {
                Move dealerMove;
                do {
                    dealerMove = dealer.move(this.getDealerCards());
                    if (dealerMove == Move.Hit) {
                        deal(getDealerCards());

                        if (checkBusted(getDealerCards())) {
                            break;
                        }
                    }
                } while (dealerMove != Move.Stand);
            }
            
            checkGameState(game);
        }
        
        endGame();
    }

    protected void firstDeal() {
        deal(game.playerCards());
        deal(dealerCards);

        deal(game.playerCards());
        dealerUpCard = deal(dealerCards);
    }

    protected Card deal(CardHand cards) {
        Card card = cardSource.next();
        if (card == null) {
            throw new LastCardException();
        }

        cards.addCard(card);
		
		return card;
    }

    private void bets() {
        bet = player.bet();
    }

    private void payPrizes(Game game) {
        switch (game.gameState()) {
            case PlayerBlackJack:
                game.getPlayer().addMoney(bet * 3 / 2);
                break;
            case Push:
                break;
            case PlayerWin:
                game.getPlayer().addMoney(bet);
                break;
            case PlayerBusted:
            case DealerWin:
                game.getPlayer().addMoney(-1 * bet);
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
        CardHand sumPlayer = game.playerCards();
        CardHand sumDealer = dealerCards;

        if (sumPlayer.softSum() > 21) {
            game.setGameState(GameState.PlayerBusted);
            return;
        }

        if (sumDealer.softSum() > 21) {
            game.setGameState(GameState.PlayerWin);
            return;
        }

        if (sumPlayer.softSum() > sumDealer.softSum()) {
            game.setGameState(GameState.PlayerWin);
        } else if (sumPlayer.softSum() == sumDealer.softSum()) {
            game.setGameState(GameState.Push);
        } else {
            game.setGameState(GameState.DealerWin);
        }
    }

    private void endGame() {
        payPrizes(this.game);

        player.result(game.gameState());
    }

    public CardHand getDealerCards() {
        return dealerCards;
    }

	public GameState getGameState() {
		return this.game.gameState();
	}

    private class CurrentGame implements Game {
		private Player player;
		private CardHand playerCards = new CardHand();
		private GameState gameState = GameState.Continuing;

		public CurrentGame(Player player) {
			this.player = player;
		}

        @Override
        public Card dealerUpCard() {
            return dealerUpCard;
        }

		@Override
		public CardHand playerCards() {
			return playerCards;
		}

		@Override
		public GameState gameState() {
			return gameState;
		}

		@Override
		public Player getPlayer() {
			return player;
		}

		@Override
		public void setGameState(GameState gameState) {
			this.gameState = gameState;
		}
    }
}
