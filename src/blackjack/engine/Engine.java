/*
 * (C) 2012 Kerio Technologies s.r.o.
 */
package blackjack.engine;

import com.google.common.collect.Sets;
import com.google.inject.Inject;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 *
 * @author mbarnas
 */
public class Engine {

	public static final Set<Move> allMoves = Sets.newEnumSet(EnumSet.allOf(Move.class), Move.class);
	
    private Player player;
    private Dealer dealer = new Dealer();
    private CardSource cardSource;
    private Card dealerUpCard;
    private LinkedList<Game> games;
    private CardHand dealerCards;
    private List<Game> endedGames;
	private boolean peek = true;
	private boolean doubleAfterSplit = true;
	private DoubleOn doubleRules;
	private boolean resplitAces = true;

	@Inject
	public void setDoubleRules(DoubleOn doubleRules) {
		this.doubleRules = doubleRules;
	}

    @Inject
    public Engine(CardSource cardSource) {
        this.cardSource = cardSource;
    }

	public void setEuropean() {
		this.setPeek(false);
	}

	public void setResplitAces(boolean resplitAces) {
		this.resplitAces = resplitAces;
	}

	@Inject
	public void setPeek(@Peek boolean peek) {
		this.peek = peek;
	}

	@Inject
	public void setDoubleAfterSplit(@DoubleAfterSplit boolean doubleAfterSplit) {
		this.doubleAfterSplit = doubleAfterSplit;
	}

    public Card getDealerUpCard() {
        return this.dealerUpCard;
    }

    public void addPlayer(Player player) {
        this.player = player;
    }

    public void registerShufflingObserver(ShuffleObserver observer) {
        this.cardSource.registerObserver(observer);
    }
    
    public Game newGame() {
        cardSource.newGame();

        final Game game = new Game(this, player);
		
		player.newGame(game);

        this.games = new LinkedList<Game>();
        this.games.add(game);

        dealerCards = new CardHand();

        this.endedGames = new LinkedList<Game>();

        bet(game);

        firstDeal(game);

        return game;
    }

    public void start() throws IllegalMoveException {
        Game game = this.games.poll();
        boolean dealerDraws = false;
        while (game != null) {
            switch (game.gameState()) {
                case AfterSplit:
                    dealToPlayer(game);
                    
					if (game.playerCards().getCards().get(0) == Card.ACE)
						game.setGameState(GameState.DealersGame);
					else
						game.setGameState(GameState.FirstDeal);
					
                    break;
                case FirstDeal:
					if (dealerUpCard == Card.ACE) {
						game.setInsuranceBet(game.getPlayer().insuranceBet());
					}
					
                    if (game.playerCards().isBlackJack()
                            || (this.peek && dealerCards.isBlackJack())) {
                        game.setGameState(GameState.CheckState);
                        continue;
                    }

                    playersGame(game);
                    break;
                case Splitted:
                    game.setGameState(GameState.End);
                    game = this.games.poll();
                    break;
                case DealersGame:
                    dealerDraws = true;
                case CheckState:
                    this.endedGames.add(game);
                    game = this.games.poll();
                    break;
            }
        }

        if (dealerDraws) {
            dealersGame();
        }

        for (Game g : this.endedGames) {
            checkGameState(g);
            endGame(g);
        }
    }

    private void dealersGame() {
		if (!this.peek)
			dealToDealer();
		
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
            Set<Move> allowedMoves = getAllowedMoves(game);
            
            move = player.move(game.playerCards(), this.getDealerUpCard(), allowedMoves);

            game.setGameState(GameState.PlayersGame);

            if (!allowedMoves.contains(move)) {
                throw new IllegalMoveException();
            }

            switch (move) {
                case Double:
                    game.setBet(game.getBet() * 2);
                    game.setGameState(GameState.DealersGame);
                case Hit:
                    dealToPlayer(game);

                    if (checkBusted(game.playerCards())) {
                        game.setGameState(GameState.DealersGame);
                        return;
                    }
                    break;
                case Split:
                    List<Game> split = game.split();
                    for (Game g : split) {
                        this.games.addFirst(g);
                    }
                    break;
				default:
					game.setGameState(GameState.DealersGame);
                    break;
            }
        } while (move == Move.Hit);
    }

	private Set<Move> getAllowedMoves(Game game) {
		Set<Move> allowedMoves = EnumSet.copyOf(allMoves);

		if (!isSplitPossible(game)) {
			allowedMoves.remove(Move.Split);
		}
		
		if (!doubleRules.isDoublePossible(game) || 
			(game.isSplitted() && ! this.doubleAfterSplit))
			allowedMoves.remove(Move.Double);
		
		if (game.getInsuranceBet() != 0)
			allowedMoves.remove(Move.Split);
	
		return allowedMoves;
	}

	private boolean isSplitPossible(Game game) {
		if (game.gameState() != GameState.FirstDeal) {
            return false;
        }
		
		final CardHand playerCards = game.playerCards();
		if (playerCards.count() != 2 || playerCards.get(0).getValue() != playerCards.get(1).getValue()) {
			return false;
		}
		
		if (!resplitAces &&
				(game.isSplitted() && game.playerCards().count() == 1 && game.playerCards().getCards().get(0) == Card.ACE)) {
			return false;
		}
		
		return true;
	}
	
    protected void firstDeal(Game game) {
        assert game.gameState() == GameState.Started;

        dealToPlayer(game);

        dealerUpCard = dealToDealer();

        dealToPlayer(game);

		if (this.peek)
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
		final Player player = game.getPlayer();
        switch (game.gameResult()) {
            case PlayerBlackJack:
                player.addMoney(game.getBet() * 3 / 2);
                break;
            case Push:
                break;
            case PlayerWin:
                player.addMoney(game.getBet());
                break;
            case PlayerBusted:
            case DealerWin:
                player.addMoney(-1 * game.getBet());
                break;
        }
		
		if (game.getInsuranceBet() != 0) {
			if (game.isInsuranceWon())
				player.addMoney(game.getInsuranceBet() * 2);
			else
				player.addMoney(game.getInsuranceBet() * -1);
		}
    }

    private void checkBlackJack(Game game) {
        CardHand sumPlayer = game.playerCards();
        CardHand sumDealer = dealerCards;

        if (sumPlayer.isBlackJack()) {
            if (sumDealer.isBlackJack()) {
                setGameResult(game, GameResult.Push);
            } else {
                setGameResult(game, GameResult.PlayerBlackJack);
            }
        } else {
            if (sumDealer.isBlackJack()) {
                setGameResult(game, GameResult.DealerWin);
				if (game.getInsuranceBet() != 0)
					game.setInsuranceWon(true);
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

        checkBlackJack(game);

        if (game.gameState() == GameState.End) {
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
