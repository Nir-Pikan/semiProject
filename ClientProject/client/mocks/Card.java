package mocks;

public class Card {

    public String cardNum;
    public int numberOfPeople;
    
	public Card(String card,int numberOfPeople) {
		this.cardNum = card;
		this.numberOfPeople=numberOfPeople;
	}

	public int getNumberOfPeople() {
		return numberOfPeople;
	}
	public void setNumberOfPeople(int numberOfPeople) {
		this.numberOfPeople=numberOfPeople;
	}
	public String getCard() {
		return cardNum;
	}

	public void setCard(String card) {
		this.cardNum = card;
	}

}
