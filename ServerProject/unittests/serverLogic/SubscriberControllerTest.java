package serverLogic;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import db.DbController;
import entities.Subscriber;
import entities.Subscriber.CardType;
import modules.ServerRequest;
import modules.ServerRequest.Manager;

class SubscriberControllerTest {

	@Mock
	ResultSet rsSub;

	@Mock
	ResultSet rsCredit;

	@Mock
	DbController db;

	SubscriberController subController;

	@BeforeEach
	void setUp() throws Exception {
		db = mock(DbController.class);
		subController = new SubscriberController(db);
	}

	/**
	 * <pre>
	 * test getting Subscriber data from subscriber without credit card
	 * input: subscriber without a credit card
	 * expected: the request returns JSON representation of the Subscriber, and no request for credit card
	 * </pre>
	 */
	@Test
	void testGetSubscriberWithOutCreditCard() {
		Subscriber expected = new Subscriber("S111111111", "111111111", "john", "dou", "0000000000", "john@doe.com", 1,
				Subscriber.Type.SUBSCRIBER);
		getSub(expected);

		String returned = subController
				.handleRequest(new ServerRequest(Manager.Subscriber, "GetSubscriberData", "S111111111"));
		Subscriber actual = ServerRequest.gson.fromJson(returned, Subscriber.class);
		assertEquals(expected, actual);
		verify(db, times(1)).sendQuery(anyString());
		verifyZeroInteractions(rsCredit);

	}

	/**
	 * <pre>
	 * test getting Subscriber data from subscriber with credit card data
	 * input: subscriber with a credit card
	 * expected: the request returns JSON representation of the Subscriber with the Credit card, and two request from the db(one for subscriber and one for credit card)
	 * </pre>
	 */
	@Test
	void testGetSubscriberWithCreditCard() {
		Subscriber expected = new Subscriber("S111111111", "111111111", "john", "dou", "0000000000", "john@doe.com", 1,
				Subscriber.Type.SUBSCRIBER);
		expected.SetCreditCard("1234-5678-9123-4567", "111111111", "john dou", "123", "10", "20", CardType.MASTERCARD);
		getSub(expected);

		String returned = subController
				.handleRequest(new ServerRequest(Manager.Subscriber, "GetSubscriberData", "S111111111"));
		Subscriber actual = ServerRequest.gson.fromJson(returned, Subscriber.class);
		assertEquals(expected, actual);
		verify(db, times(2)).sendQuery(anyString());
	}

	/**
	 * <pre>
	 * test ask for non existing subscriber
	 * input: subscriber with a credit card
	 * expected: the request returns JSON representation of the Subscriber with the Credit card, and two request from the db(one for subscriber and one for credit card)
	 * </pre>
	 */
	@Test
	void testGetSubscriberNoData() {
		String expected = "Subscriber S111111111 was not found";
		rsSub = mock(ResultSet.class);
		try {
			when(rsSub.next()).thenReturn(false);
			when(db.sendQuery(anyString())).thenReturn(rsSub);
			String actual = subController
					.handleRequest(new ServerRequest(Manager.Subscriber, "GetSubscriberData", "S111111111"));
			assertEquals(expected, actual);
			verify(db, times(1)).sendQuery(anyString());
			verify(rsSub, times(1)).next();
			verify(rsSub, times(1)).close();
			verifyNoMoreInteractions(rsSub);
		} catch (SQLException e) {
			e.printStackTrace();
			fail(e);
		}

	}

	/** set rs to return the wanted subscriber */
	ResultSet getSub(Subscriber s) {
		rsSub = mock(ResultSet.class);
		try {
			when(rsSub.next()).thenReturn(true);
			when(rsSub.getString(1)).thenReturn(s.subscriberID);
			when(rsSub.getString(2)).thenReturn(s.personalID);
			when(rsSub.getString(3)).thenReturn(s.firstName);
			when(rsSub.getString(4)).thenReturn(s.lastName);
			when(rsSub.getString(5)).thenReturn(s.phone);
			when(rsSub.getString(6)).thenReturn(s.email);
			when(rsSub.getString(7)).thenReturn(s.creditCardNumber);
			when(rsSub.getInt(8)).thenReturn(s.familySize);
			when(rsSub.getString(9)).thenReturn(s.type.toString());
			when(db.sendQuery("SELECT * FROM subscribers WHERE subscriberID = \"" + s.subscriberID + "\";"))
					.thenReturn(rsSub);
			rsCredit = mock(ResultSet.class);
			if (s.creditCardNumber != null) {
				when(rsCredit.next()).thenReturn(true);
				when(rsCredit.getString(1)).thenReturn(s.creditCard.creditCardNumber);
				when(rsCredit.getString(2)).thenReturn(s.creditCard.ownerID);
				when(rsCredit.getString(3)).thenReturn(s.creditCard.nameOnCard);
				when(rsCredit.getString(4)).thenReturn(s.creditCard.cvv);
				when(rsCredit.getString(5)).thenReturn(s.creditCard.expirationDateMonth);
				when(rsCredit.getString(6)).thenReturn(s.creditCard.expirationDateYear);
				when(rsCredit.getString(7)).thenReturn(s.creditCard.cardType.toString());
				when(db.sendQuery("SELECT * FROM creditcards WHERE creditCardNumber = " + s.creditCardNumber + ";"))
						.thenReturn(rsCredit);
			}

		} catch (SQLException e) {
		}

		return rsSub;
	}

}
