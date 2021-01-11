package entities;

/**
 * a class containing Subscriber's data <br>
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

	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((creditCard == null) ? 0 : creditCard.hashCode());
		result = prime * result + ((creditCardNumber == null) ? 0 : creditCardNumber.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + familySize;
		result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result + ((personalID == null) ? 0 : personalID.hashCode());
		result = prime * result + ((phone == null) ? 0 : phone.hashCode());
		result = prime * result + ((subscriberID == null) ? 0 : subscriberID.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Subscriber other = (Subscriber) obj;
		if (creditCard == null) {
			if (other.creditCard != null)
				return false;
		} else if (!creditCard.equals(other.creditCard))
			return false;
		if (creditCardNumber == null) {
			if (other.creditCardNumber != null)
				return false;
		} else if (!creditCardNumber.equals(other.creditCardNumber))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (familySize != other.familySize)
			return false;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		if (personalID == null) {
			if (other.personalID != null)
				return false;
		} else if (!personalID.equals(other.personalID))
			return false;
		if (phone == null) {
			if (other.phone != null)
				return false;
		} else if (!phone.equals(other.phone))
			return false;
		if (subscriberID == null) {
			if (other.subscriberID != null)
				return false;
		} else if (!subscriberID.equals(other.subscriberID))
			return false;
		if (type != other.type)
			return false;
		return true;
	}



	@Override
	public String toString() {
		return "Subscriber [subscriberID=" + subscriberID + ", personalID=" + personalID + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", phone=" + phone + ", email=" + email + ", creditCardNumber="
				+ creditCardNumber + ", familySize=" + familySize + ", type=" + type + ", creditCard=" + creditCard
				+ "]";
	}



	/**
	 * a class containing Credit card's data <br>
	 * a Credit card may be used by multiple {@link Subscriber}s
	 */
	public static class CreditCard {

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

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((cardType == null) ? 0 : cardType.hashCode());
			result = prime * result + ((creditCardNumber == null) ? 0 : creditCardNumber.hashCode());
			result = prime * result + ((cvv == null) ? 0 : cvv.hashCode());
			result = prime * result + ((expirationDateMonth == null) ? 0 : expirationDateMonth.hashCode());
			result = prime * result + ((expirationDateYear == null) ? 0 : expirationDateYear.hashCode());
			result = prime * result + ((nameOnCard == null) ? 0 : nameOnCard.hashCode());
			result = prime * result + ((ownerID == null) ? 0 : ownerID.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			CreditCard other = (CreditCard) obj;
			if (cardType != other.cardType)
				return false;
			if (creditCardNumber == null) {
				if (other.creditCardNumber != null)
					return false;
			} else if (!creditCardNumber.equals(other.creditCardNumber))
				return false;
			if (cvv == null) {
				if (other.cvv != null)
					return false;
			} else if (!cvv.equals(other.cvv))
				return false;
			if (expirationDateMonth == null) {
				if (other.expirationDateMonth != null)
					return false;
			} else if (!expirationDateMonth.equals(other.expirationDateMonth))
				return false;
			if (expirationDateYear == null) {
				if (other.expirationDateYear != null)
					return false;
			} else if (!expirationDateYear.equals(other.expirationDateYear))
				return false;
			if (nameOnCard == null) {
				if (other.nameOnCard != null)
					return false;
			} else if (!nameOnCard.equals(other.nameOnCard))
				return false;
			if (ownerID == null) {
				if (other.ownerID != null)
					return false;
			} else if (!ownerID.equals(other.ownerID))
				return false;
			return true;
		}

		@Override
		public String toString() {
			return "CreditCard [creditCardNumber=" + creditCardNumber + ", ownerID=" + ownerID + ", nameOnCard="
					+ nameOnCard + ", cvv=" + cvv + ", expirationDateMonth=" + expirationDateMonth
					+ ", expirationDateYear=" + expirationDateYear + ", cardType=" + cardType + "]";
		}

	}
}
