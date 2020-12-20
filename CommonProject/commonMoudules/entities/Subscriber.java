package entities;

/**
 * a class containing Subscriber's data
 * <p>
 * a Subscriber may contain one {@link CreditCard}
 */
public class Subscriber {

	public enum Type {
		SUBSCRIBER, GUIDE
	};

	public enum CardType {
		VISA, MASTERCARD, AMERICANEXPRESS
	};

	public String subscriberID, personalID, firstName, lastName, phone, email;
	public String creditCardNumber;
	public int familySize;
	public Type type;
	public CreditCard creditCard;

	/**
	 * Creates a {@link Subscriber}
	 * 
	 * @param subscriberID String
	 * @param personalID   String
	 * @param firstName    String
	 * @param lastName     String
	 * @param phone        String
	 * @param email        String
	 * @param familySize   int
	 * @param type         Type{SUBSCRIBER,GUIDE}
	 **/
	public Subscriber(String subscriberID, String personalID, String firstName, String lastName, String phone,
			String email, int familySize, Type type) {
		super();
		this.subscriberID = subscriberID;
		this.personalID = personalID;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phone = phone;
		this.email = email;
		this.creditCardNumber = null;
		this.familySize = familySize;
		this.type = type;
		this.creditCard = null;
	}

	/**
	 * Creates a {@link CreditCard} for the {@link Subscriber}
	 * 
	 * @param creditCardNumber    String
	 * @param ownerID             String
	 * @param nameOnCard          String
	 * @param cvv                 String
	 * @param expirationDateMonth String
	 * @param expirationDateYear  String
	 * @param cardType            CardType{VISA,MASTERCARD,AMERICANEXPRESS}
	 **/
	public void SetCreditCard(String creditCardNumber, String ownerID, String nameOnCard, String cvv,
			String expirationDateMonth, String expirationDateYear, CardType cardType) {
		this.creditCardNumber = creditCardNumber;

		this.creditCard = new CreditCard(creditCardNumber, ownerID, nameOnCard, cvv, expirationDateMonth,
				expirationDateYear, cardType);
	}

	/**
	 * a class containing Credit card's data
	 * <p>
	 * a Credit card may be used by multiple {@link Subscriber}s
	 */
	public class CreditCard {

		public String creditCardNumber;
		public String ownerID;
		public String nameOnCard;
		public String cvv;
		public String expirationDateMonth;
		public String expirationDateYear;
		public CardType cardType;

		/**
		 * Creates a {@link CreditCard}
		 * 
		 * @param creditCardNumber    String
		 * @param ownerID             String
		 * @param nameOnCard          String
		 * @param cvv                 String
		 * @param expirationDateMonth String
		 * @param expirationDateYear  String
		 * @param cardType            CardType{VISA,MASTERCARD,AMERICANEXPRESS}
		 **/
		public CreditCard(String creditCardNumber, String ownerID, String nameOnCard, String cvv,
				String expirationDateMonth, String expirationDateYear, CardType cardType) {
			this.creditCardNumber = creditCardNumber;
			this.ownerID = ownerID;
			this.nameOnCard = nameOnCard;
			this.cvv = cvv;
			this.expirationDateMonth = expirationDateMonth;
			this.expirationDateYear = expirationDateYear;
			this.cardType = cardType;
		}
	}
}
