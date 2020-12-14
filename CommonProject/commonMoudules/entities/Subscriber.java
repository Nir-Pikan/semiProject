package entities;

/**
 * public class for Subscribers*/
public class Subscriber {
	
	public enum Type{SUBSCRIBER,GUIDE};
	public enum CardType{VISA,MASTERCARD,AMERICANEXPRESS};
	public String subscriberID,personalID,firstName,lastName,phone,email;
	public String creditCardNumber;
	public int familySize;
	public Type type;
	public CreditCard creditCard;
	
	public Subscriber(String subscriberID, String personalID, String firstName, String lastName,
			String phone,String email, int familySize,Type type) {
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
	 *Creates a credit card for the subscriber
	 *@param creditCardNumber the credit card number
	 *@param ownerID the card's owner ID
	 *@param nameOnCard the card's owner first and last name
	 *@param cvv the card's cvv number
	 *@param expirationDateMonth the card's month expiration date
	 *@param expirationDateYear the card's year expiration date
	 *@param cardType the card's type (Visa\Master Card\American Express)*/
	public void SetCreditCard(String creditCardNumber,String ownerID,String nameOnCard,String cvv,
			String expirationDateMonth,String expirationDateYear,CardType cardType) {
		this.creditCardNumber = creditCardNumber;
		
		this.creditCard = new CreditCard(creditCardNumber,ownerID,nameOnCard,cvv,
				expirationDateMonth,expirationDateYear,cardType);
	}
	
	/**
	 * public class for Credit cards*/
	public class CreditCard{
		
		public String creditCardNumber;
		public String ownerID;
		public String nameOnCard;
		public String cvv;
		public String expirationDateMonth;
		public String expirationDateYear;
		public CardType cardType;
		
		public CreditCard(String creditCardNumber,String ownerID,String nameOnCard,String cvv,
				String expirationDateMonth,String expirationDateYear,CardType cardType) {
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
