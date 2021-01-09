package serverLogic;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import db.DbController;
import entities.Subscriber;
import entities.Subscriber.CreditCard;
import entities.Subscriber.Type;
import modules.IController;
import modules.ServerRequest;

/** the subscriber controller class */
public class SubscriberController implements IController {

	DbController dbController;

	/** creates the {@link SubscriberController} */
	public SubscriberController() {
		dbController = DbController.getInstance();
		createTable();
	}

	/**
	 * Create Subscribers table in DB if not exists <br>
	 * table of {@link Subscriber}s
	 */
	private void createTable() {

		dbController.createTable("creditcards(creditCardNumber varchar(19),ownerID varchar(20),nameOnCard varchar(30),"
				+ "cvv varchar(4),edMonth varchar(2),edYear varchar(4),"
				+ "cardType ENUM('VISA','MASTERCARD','AMERICANEXPRESS')," + "primary key(creditCardNumber));");

		dbController.createTable("subscribers(subscriberID varchar(20),personalID varchar(20),firstName varchar(20),"
				+ "lastName varchar(20),phone varchar(10),email varchar(30),"
				+ "creditCardNumber varchar(19),familySize int,type ENUM('SUBSCRIBER','GUIDE'),"
				+ "primary key(subscriberID),foreign key(creditCardNumber) references creditcards(creditCardNumber));");

	}

	@Override
	public String handleRequest(ServerRequest request) {
		String job = request.job;
		String response = null;

		switch (job) {

		case "GetSubscriberData":
			Subscriber sub = GetSubscriberData(request.data);
			if (sub == null)
				response = "Subscriber " + request.data + " was not found";

			else
				response = ServerRequest.gson.toJson(sub);
			break;

		case "AddNewSubscriber":
			Subscriber sub2 = ServerRequest.gson.fromJson(request.data, Subscriber.class);

			// first check if subscriber already exists
			if (GetSubscriberData(sub2.subscriberID) != null) {
				response = "Subscriber already exists";
				break;
			}

			// try to add subscriber
			if (AddNewSubscriber(sub2))
				response = "Subscriber was added successfully";
			else
				response = "Failed to add Subscriber";
			break;

		default:
			response = "Error: No such job";
		}

		return response;
	}

	/**
	 * Ask DB for wanted subscriber data
	 * 
	 * @param subscriberID the wanted {@link Subscriber}
	 * @return object of wanted {@link Subscriber}<br>
	 *         null if failed
	 */
	private Subscriber GetSubscriberData(String subscriberID) {
		String statment = "SELECT * FROM subscribers WHERE subscriberID = \"" + subscriberID + "\";";
		ResultSet res = dbController.sendQuery(statment);

		if (res == null)
			return null;

		Subscriber s = null;
		try {
			if (res.next()) {
				// Print out the values
				s = new Subscriber(res.getString(1), res.getString(2), res.getString(3), res.getString(4),
						res.getString(5), res.getString(6), res.getInt(8), Type.valueOf(res.getString(9)));

				// if subscriber has a credit card get him from creditcards table as well
				if (s.creditCardNumber != null) {
					String statment2 = "SELECT * FROM creditcards WHERE creditCardNumber = " + s.creditCardNumber + ";";
					ResultSet res2 = dbController.sendQuery(statment2);

					if (res2 != null)
						try {
							if (res2.next()) {
								s.SetCreditCard(res2.getString(1), res2.getString(2), res2.getString(3),
										res2.getString(4), res2.getString(5), res2.getString(6),
										Subscriber.CardType.valueOf(res2.getString(7)));
							}
							res2.close();

						} catch (SQLException e) {
							e.printStackTrace();
						}
				}

			}
			res.close();
			return s;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Ask DB to add a new {@link Subscriber} to the Subscribers table
	 * 
	 * @param sub the {@link Subscriber} to add
	 * @return true if succeeded<br>
	 *         false otherwise
	 */
	private boolean AddNewSubscriber(Subscriber sub) {

		// if subscriber got a credit card add it to the credit cards table
		if (sub.creditCard != null) {
			PreparedStatement ps = dbController
					.getPreparedStatement("INSERT IGNORE INTO creditcards VALUES (?, ?, ?, ?, ?, ?, ?);");
			try {
				ps.setString(1, sub.creditCard.creditCardNumber);
				ps.setString(2, sub.creditCard.ownerID);
				ps.setString(3, sub.creditCard.nameOnCard);
				ps.setString(4, sub.creditCard.cvv);
				ps.setString(5, sub.creditCard.expirationDateMonth);
				ps.setString(6, sub.creditCard.expirationDateYear);
				ps.setString(7, sub.creditCard.cardType.toString());
				if (ps.executeUpdate() == 0) {
					String statment2 = "SELECT * FROM creditcards WHERE creditCardNumber = " + sub.creditCardNumber
							+ ";";
					ResultSet res2 = dbController.sendQuery(statment2);

					if (res2 != null)
						try {
							if (res2.next()) {
								CreditCard c = new CreditCard(res2.getString(1), res2.getString(2), res2.getString(3),
										res2.getString(4), res2.getString(5), res2.getString(6),
										Subscriber.CardType.valueOf(res2.getString(7)));
								if (!c.equals(sub.creditCard))
									return false;
							}
							res2.close();

						} catch (SQLException e) {
							e.printStackTrace();
						}
				}
			} catch (SQLException e) { // if adding the credit card failed
				e.printStackTrace();
			}
			System.out.println("Credit card added");
		}

		// add the subscriber to the subscribers table
		PreparedStatement ps = dbController
				.getPreparedStatement("INSERT INTO subscribers VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);");
		try {
			ps.setString(1, sub.subscriberID);
			ps.setString(2, sub.personalID);
			ps.setString(3, sub.firstName);
			ps.setString(4, sub.lastName);
			ps.setString(5, sub.phone);
			ps.setString(6, sub.email);
			ps.setString(7, sub.creditCardNumber);
			ps.setInt(8, sub.familySize);
			ps.setString(9, sub.type.toString());
			return ps.executeUpdate() == 1;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

}
