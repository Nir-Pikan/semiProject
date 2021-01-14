package serverLogic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import db.DbController;
import entities.ParkEntry;
import entities.ParkEntry.EntryType;
import modules.ServerRequest;
import modules.ServerRequest.Manager;

class EntryControllerTest {

	@Mock
	ResultSet result;
	
	@Mock
	PreparedStatement preStmt;
	
	@Mock
	DbController db;
	
	EntryController entryController;
	
	@BeforeEach
	void setUp() throws Exception {
		db = mock(DbController.class);
		entryController = new EntryController(db);
	}

	/**
	 * <pre>
	 * test sending request with null as the time range
	 * input: request with null as data
	 * expected: the request returns "Error: There is no 2 times search between ", and no db interaction
	 * </pre>
	 */
	@Test
	void testNullDates() {
		String expected = "Error: There is no 2 times search between ";
		String actual = entryController.handleRequest(new ServerRequest(Manager.Entry,"getEntriesByDate",null));
		assertEquals(expected, actual);
		verifyZeroInteractions(db);
	}
	
	/**
	 * <pre>
	 * test sending request with wrong time range, more then two or less then two
	 * input: request with one timestamp as data and one with three
	 * expected: the request returns "Error: There is no 2 times search between " in both requests, and no db interaction
	 * </pre>
	 */
	@Test
	void testNotTwoTimes() {
		String expected = "Error: There is no 2 times search between ";
		//TEST only one
		Timestamp[] arr = new Timestamp[] {Timestamp.valueOf(LocalDateTime.now())};//only one
		String actual = entryController.handleRequest(new ServerRequest(Manager.Entry,"getEntriesByDate",ServerRequest.gson.toJson(arr,Timestamp[].class)));
		assertEquals(expected, actual);
		verifyZeroInteractions(db);
		//TEST more then 2
		LocalDateTime now = LocalDateTime.now();
		Timestamp[] arr2 = new Timestamp[] {Timestamp.valueOf(now),Timestamp.valueOf(now),Timestamp.valueOf(now)};
		String actual2 = entryController.handleRequest(new ServerRequest(Manager.Entry,"getEntriesByDate",ServerRequest.gson.toJson(arr2,Timestamp[].class)));
		assertEquals(expected, actual2);
		verifyZeroInteractions(db);
	}

	/**
	 * <pre>
	 * test sending request with wrong time range, the first time is after the second
	 * input: request with one timestamp as data and one with three
	 * expected: the request returns "Error: start time is later than end time ", and no db interaction
	 * </pre>
	 */
	@Test
	void testReverseOrderTimes() {
		String expected = "Error: start time is later than end time ";
		LocalDateTime now = LocalDateTime.now();
		Timestamp[] arr = new Timestamp[] {Timestamp.valueOf(now.plusDays(1)),Timestamp.valueOf(now)};
		String actual = entryController.handleRequest(new ServerRequest(Manager.Entry,"getEntriesByDate",ServerRequest.gson.toJson(arr,Timestamp[].class)));
		assertEquals(expected, actual);
		verifyZeroInteractions(db);
			}
	
	/**
	 * <pre>
	 * test getting Entries data
	 * input: good time range to select and good response
	 * expected: the request returns JSON representation of the Entries
	 * </pre>
	 */
	@Test
	void testGetEntrySuccess() {
		LocalDateTime now = LocalDateTime.now();
		List<ParkEntry> list = new ArrayList<>();
		list.add(new ParkEntry(EntryType.Personal, "564564", "park", Timestamp.valueOf(now), null, 4, 4, false, 20, 20));
		list.add(new ParkEntry(EntryType.Personal, "78978", "park", Timestamp.valueOf(now), null, 4, 4, false, 20, 20));
		list.add(new ParkEntry(EntryType.Personal, "13112", "park", Timestamp.valueOf(now), null, 4, 4, true, 20, 20));
		list.add(new ParkEntry(EntryType.Group, "52644535564", "park", Timestamp.valueOf(now), null, 4, 1, false, 20, 20));
		list.add(new ParkEntry(EntryType.PrivateGroup, "522222", "park", Timestamp.valueOf(now), null, 4, 4, false, 20, 20));
		getEntryList(list);
		Timestamp[] arr = new Timestamp[] {Timestamp.valueOf(now),Timestamp.valueOf(now.plusDays(1))};
		String actual = entryController.handleRequest(new ServerRequest(Manager.Entry,"getEntriesByDate",ServerRequest.gson.toJson(arr,Timestamp[].class)));
		String expected = ServerRequest.gson.toJson(list.toArray(new ParkEntry[0]),ParkEntry[].class);
		assertEquals(expected, actual);
		try {
			verify(preStmt,times(1)).setTimestamp(1, Timestamp.valueOf(now.withNano(0)));
			verify(preStmt,times(1)).setTimestamp(2, Timestamp.valueOf(now.plusDays(1).withNano(0)));
		} catch (SQLException e) {
			e.printStackTrace();
		}
			}
	
	/**
	 * <pre>
	 * test getting Entries data with exception thrown
	 * input: good time range to select and exception when executed
	 * expected: the request returns "Error: could not get entires from DB "
	 * </pre>
	 */
	@Test
	void testGetEntryFailed() {
		LocalDateTime now = LocalDateTime.now();
		preStmt = mock(PreparedStatement.class);
		when(db.getPreparedStatement("select * from parkEntry  where timestamp(arriveTime) > timestamp( ? )  "
				+ " and  timestamp(arriveTime) < timestamp( ? ) ;")).thenReturn(preStmt);
	try {
		when(preStmt.executeQuery()).thenThrow(new SQLException());
	} catch (SQLException e1) {
		e1.printStackTrace();
	}
		Timestamp[] arr = new Timestamp[] {Timestamp.valueOf(now),Timestamp.valueOf(now.plusDays(1))};
		String actual = entryController.handleRequest(new ServerRequest(Manager.Entry,"getEntriesByDate",ServerRequest.gson.toJson(arr,Timestamp[].class)));
		String expected = "Error: could not get entires from DB ";
		assertEquals(expected, actual);
		try {
			verify(preStmt,times(1)).setTimestamp(1, Timestamp.valueOf(now.withNano(0)));
			verify(preStmt,times(1)).setTimestamp(2, Timestamp.valueOf(now.plusDays(1).withNano(0)));
		} catch (SQLException e) {
			e.printStackTrace();
		}
			}
	
	ParkEntry iteratedEntry;
	ResultSet getEntryList(Collection<ParkEntry> col) {
		result = mock(ResultSet.class);
		preStmt = mock(PreparedStatement.class);
		try {
			Iterator<ParkEntry> i = col.iterator();
			
			when(result.next()).thenAnswer((b)->{
					try {
						iteratedEntry = i.next();
						return true;
					} catch (NoSuchElementException e) {
						return false;
					}
				}
			);

			when(result.getString(1)).thenAnswer((b)->{return iteratedEntry.entryType.toString();});
			when(result.getString(2)).thenAnswer((b)->{return iteratedEntry.personID;});
			when(result.getString(3)).thenAnswer((b)->{return iteratedEntry.parkID;});
			when(result.getTimestamp(4)).thenAnswer((b)->{return iteratedEntry.arriveTime;});
			when(result.getTimestamp(5)).thenAnswer((b)->{return iteratedEntry.exitTime;});
			when(result.getInt(6)).thenAnswer((b)->{return iteratedEntry.numberOfVisitors;});
			when(result.getInt(7)).thenAnswer((b)->{return iteratedEntry.numberOfSubscribers;});
			when(result.getBoolean(8)).thenAnswer((b)->{return iteratedEntry.isCasual;});
			when(result.getFloat(9)).thenAnswer((b)->{return iteratedEntry.priceOfOrder;});
			when(result.getFloat(10)).thenAnswer((b)->{return iteratedEntry.priceOfEntry;});
			when(db.getPreparedStatement("select * from parkEntry  where timestamp(arriveTime) > timestamp( ? )  "
						+ " and  timestamp(arriveTime) < timestamp( ? ) ;")).thenReturn(preStmt);
			when(preStmt.executeQuery()).thenReturn(result);
		} catch (SQLException e) {
		}
		
		return result;
	}
}
