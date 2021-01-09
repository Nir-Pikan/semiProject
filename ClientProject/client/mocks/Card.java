package mocks;

/** a class representing a Card for the {@link CardReaderSystem} */
public class Card {

	public String cardNum;
	public int numberOfPeople;
	public int numberOfSubscribers;

	/**
	 * creates a {@link Card}
	 * 
	 * @param card                the {@link Card}'s number
	 * @param numberOfPeople      number of people entering using the {@link Card}
	 * @param numberOfSubscribers number of {@link Subscriber}s entering using the
	 *                            {@link Card}
	 */
	public Card(String card, int numberOfPeople, int numberOfSubscribers) {
		this.cardNum = card;
		this.numberOfPeople = numberOfPeople;
		this.numberOfSubscribers = numberOfSubscribers;
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
		this.numberOfPeople = numberOfPeople;
	}

	public String getCard() {
		return cardNum;
	}

	public void setCard(String card) {
		this.cardNum = card;
	}

}
