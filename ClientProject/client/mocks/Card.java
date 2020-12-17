package mocks;

public class Card {

    public String cardNum;
    
	public Card(String card) {
		super();
		this.cardNum = card;
	}

	public String getCard() {
		return cardNum;
	}

	public void setCard(String card) {
		this.cardNum = card;
	}

}
