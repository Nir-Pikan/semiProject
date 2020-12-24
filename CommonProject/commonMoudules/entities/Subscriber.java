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
	 * @param subscriberID S + the subscriber's personal ID
	 * @param personalID   the subscriber's personal ID
	 * @param firstName    the subscriber's first name
	 * @param lastName     the subscriber's last name
	 * @param phone        the subscriber's phone number
	 * @param email        the subscriber's email address
	 * @param familySize   the subscriber's family size
	 * @param type         the type of subscriber. can be SUBSCRIBER/GUIDE
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
	 * @param creditCardNumber    the credit card's number
	 * @param ownerID             the credit card's owner ID
	 * @param nameOnCard          the name on the credit card
	 * @param cvv                 the credit card's cvv
	 * @param expirationDateMonth the credit card's month expiration date
	 * @param expirationDateYear  the credit card's year expiration date
	 * @param cardType            the type of credit card. can be
	 *                            VISA/MASTERCARD/AMERICANEXPRESS
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
		 * @param creditCardNumber    the credit card's number
		 * @param ownerID             the credit card's owner ID
		 * @param nameOnCard          the name on the credit card
		 * @param cvv                 the credit card's cvv
		 * @param expirationDateMonth the credit card's month expiration date
		 * @param expirationDateYear  the credit card's year expiration date
		 * @param cardType            the type of credit card. can be
		 *                            VISA/MASTERCARD/AMERICANEXPRESS
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
