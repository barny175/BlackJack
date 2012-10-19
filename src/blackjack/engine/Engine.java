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

	public GameResult getGameState() {
		return gameState;
	}

	public Engine(CardSource cardSource) {
		this.cardSource = cardSource;
	}

	public Card getDealerUpCard() {
		return this.dealer.getVisibleCard();
	}

	public void addPlayer(Player player) {
		this.player = player;
	}

	public void newGame() {
		reset();

		bets();

		firstDeal();
	}

	private void reset() {
		cardSource.shuffle();

		resetPlayers();

		this.gameState = GameResult.Continuing;

		this.player.setGame(new CurrentGame());
	}

	public void start() {
		checkBlackJack();

		if (this.gameState == GameResult.Continuing) {
			boolean busted = moveAndCheckBust(player);
			if (!busted) {
				moveAndCheckBust(dealer);
			}

			checkGameState();
		}

		endGame();
	}

	/**
	 *
	 * @param cardHolder
	 */
	private boolean moveAndCheckBust(CardHolder cardHolder) {
		Move move;
		do {
			move = cardHolder.move();
			if (move == Move.Hit) {
				deal(cardHolder);

				if (checkBusted(cardHolder.getCards())) {
					return true;
				}
			}
		} while (move != Move.Stand);

		return false;
	}

	protected void firstDeal() {
		deal(player);
		deal(dealer);

		deal(player);
		deal(dealer);
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

	private void payPrizes() {
		switch (this.gameState) {
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

	private void checkBlackJack() {
		CardHand sumPlayer = player.getCards();
		CardHand sumDealer = dealer.getCards();

		if (sumPlayer.isBlackJack()) {
			if (sumDealer.isBlackJack()) {
				this.gameState = GameResult.Push;
			} else {
				this.gameState = GameResult.PlayerBlackJack;
			}
		} else {
			if (sumDealer.isBlackJack()) {
				this.gameState = GameResult.DealerWin;
			}
		}
	}

	private boolean checkBusted(CardHand cards) {
		if (cards.softSum() > 21) {
			return true;
		}

		return false;
	}

	private void checkGameState() {
		CardHand sumPlayer = player.getCards();
		CardHand sumDealer = dealer.getCards();

		if (sumPlayer.softSum() > 21) {
			gameState = GameResult.PlayerBusted;
			return;
		}

		if (sumDealer.softSum() > 21) {
			this.gameState = GameResult.PlayerWin;
			return;
		}

		if (sumPlayer.softSum() > sumDealer.softSum()) {
			this.gameState = GameResult.PlayerWin;
		} else if (sumPlayer.softSum() == sumDealer.softSum()) {
			this.gameState = GameResult.Push;
		} else {
			this.gameState = GameResult.DealerWin;
		}
	}

	private void resetPlayers() {
		dealer.reset();
		player.reset();
	}

	private void endGame() {
		payPrizes();

		player.result(gameState);
	}

	public CardHand getDealerCards() {
		return dealer.getCards();
	}

	private class CurrentGame implements Game {

		@Override
		public Card getDealerUpCard() {
			return dealer.getVisibleCard();
		}
	}
}
