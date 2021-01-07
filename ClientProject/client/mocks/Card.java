package mocks;

public class Card {

    public String cardNum;
    public int numberOfPeople;
    public int numberOfSubscribers;
    
	public Card(String card,int numberOfPeople,int numberOfSubscribers) {
		this.cardNum = card;
		this.numberOfPeople=numberOfPeople;
		this.numberOfSubscribers=numberOfSubscribers;
	}

	public int getNumberOfSubscribers() {
		return numberOfSubscribers;
	}

	public void setNumberOfSubscribers(int numberOfSubscribers) {
		this.numberOfSubscribers = numberOfSubscribers;
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
