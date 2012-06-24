package com.dl.ygo;

public class Player {
	public int getLP() {
		return LP;
	}

	public void setLP(int lP) {
		LP = lP;
	}

	public boolean isTurn() {
		return isTurn;
	}

	public void setTurn(boolean isTurn) {
		this.isTurn = isTurn;
	}

	public CardGroup getCardsHand() {
		return cardsHand;
	}

	public void setCardsHand(CardGroup cardsHand) {
		this.cardsHand = cardsHand;
	}

	public CardGroup getCardsST() {
		return cardsST;
	}

	public void setCardsST(CardGroup cardsST) {
		this.cardsST = cardsST;
	}

	public CardGroup getCardsMZONE() {
		return cardsMZONE;
	}

	public void setCardsMZONE(CardGroup cardsMZONE) {
		this.cardsMZONE = cardsMZONE;
	}

	public CardGroup getCardsDeck() {
		return cardsDeck;
	}

	public void setCardsDeck(CardGroup cardsDeck) {
		this.cardsDeck = cardsDeck;
	}

	public CardGroup getCardsGrave() {
		return cardsGrave;
	}

	public void setCardsGrave(CardGroup cardsGrave) {
		this.cardsGrave = cardsGrave;
	}

	public CardGroup getCardsExtra() {
		return cardsExtra;
	}

	public void setCardsExtra(CardGroup cardsExtra) {
		this.cardsExtra = cardsExtra;
	}

	public CardGroup getCardsBanish() {
		return cardsBanish;
	}

	public void setCardsBanish(CardGroup cardsBanish) {
		this.cardsBanish = cardsBanish;
	}

	private int LP;
	//轮到
	private boolean isTurn;
	private CardGroup cardsHand;
	private CardGroup cardsST;
	private CardGroup cardsMZONE;
	private CardGroup cardsDeck;
	private CardGroup cardsGrave;
	private CardGroup cardsExtra;
	private CardGroup cardsBanish;
	
	public Player(int lp,boolean isTurn,CardGroup hand
			,CardGroup stzone,CardGroup mzone,CardGroup deck
			,CardGroup grave,CardGroup extra,CardGroup banish)
	{
		this.LP = lp;
		this.isTurn = isTurn;
		this.cardsBanish = banish;
		this.cardsDeck = deck;
		this.cardsExtra = extra;
		this.cardsGrave = grave;
		this.cardsHand = hand;
		this.cardsMZONE = mzone;
		this.cardsST = stzone;
	}
}
