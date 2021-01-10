package serverLogic;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import org.junit.Rule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.stubbing.OngoingStubbing;

import db.DbController;
import entities.Park;
import serverLogic.ParkController;

class ParkControllerTest {
	@Mock
	DbController db;

	@Mock
	ResultSet rsPark;

	@Mock
	ResultSet rsChange;

	@Mock
	PreparedStatement ps;

	@Rule
	public MockitoRule mockitoRule = MockitoJUnit.rule();

	private ParkController parkC;
	private Park demo;

	@BeforeEach
	void setUp() throws Exception {
		db = mock(DbController.class);
		parkC = new ParkController(db);
		demo = new Park("id", "name", 100, "manId", 2, 3.0, 10, 8, 12);
	}
	

	/**
	 * <pre>
	 * test for getting maxPreOrder
	 * input:park with maxPreOrder Of 2
	 * expected:parkC.getMaxPreOrder("id")==2
	 * </pre>
	 */
	@Test
	void testGetMaxPreOrderExistingPark() {
		getPark(demo);
		assertTrue(parkC.getMaxPreOrder("id") == demo.maxPreOrders);
	}

	/**
	 * <pre>
	 * test for getting parameter with park not exists
	 * input:no park
	 * expected:parkC.getMaxPreOrder("2")==-1
	 * </pre>
	 */
	@Test
	void testGetMaxPreOrderNotExistingPark() {
		rsPark = mock(ResultSet.class);
		try {
			when(rsPark.next()).thenReturn(false);
		} catch (SQLException e) {
		}
		when(db.sendQuery("SELECT * FROM park WHERE parkId = 2")).thenReturn(rsPark);
		assertTrue(parkC.getMaxPreOrder("2") == -1);
	}

	/**
	 * <pre>
	 * test for getting maxCapacity
	 * input:park with maxCapacity Of 100
	 * expected:parkC.getMaxCapacity("id")==100
	 * </pre>
	 */
	@Test
	void testGetMaxCapacityExistingPark() {
		getPark(demo);
		assertTrue(parkC.getMaxCapacity("id") == demo.maxCapacity);
	}

	/**
	 * <pre>
	 * test for getting parameter with park not exists
	 * input:no park
	 * expected:parkC.getMaxPreOrder("2")==-1
	 * </pre>
	 */
	@Test
	void testGetMaxCapacityNotExistingPark() {
		rsPark = mock(ResultSet.class);
		try {
			when(rsPark.next()).thenReturn(false);
		} catch (SQLException e) {
		}
		when(db.sendQuery("SELECT * FROM park WHERE parkId = 2")).thenReturn(rsPark);
		assertTrue(parkC.getMaxCapacity("2") == -1);
	}

	/**
	 * <pre>
	 * test for getting AVGvisitTime
	 * input:park with AVGvisitTime Of 3.0
	 * expected:parkC.getAVGvisitTime("id")==3.0
	 * </pre>
	 */
	@Test
	void testGetAVGvisitTimeExistingPark() {
		getPark(demo);
		assertTrue(parkC.getAVGvisitTime("id") == demo.avgVisitTime);
	}

	/**
	 * <pre>
	 * test for getting parameter with park not exists
	 * input:no park
	 * expected:parkC.getAVGvisitTime("2")==-1
	 * </pre>
	 */
	@Test
	void testGetAVGvisitTimeNotExistingPark() {
		rsPark = mock(ResultSet.class);
		try {
			when(rsPark.next()).thenReturn(false);
		} catch (SQLException e) {
		}
		when(db.sendQuery("SELECT * FROM park WHERE parkId = 2")).thenReturn(rsPark);
		assertTrue(parkC.getAVGvisitTime("2") == -1);
	}

	/**
	 * <pre>
	 * test UpdateNumberOfCurrentVisitor with Positive number
	 * input:park with CurrentVisitor Of 10
	 * expected:parkC change in the db the value to 20(10+10)
	 * </pre>
	 */
	@Test
	void testUpdateNumberOfCurrentVisitorPos() {
		getPark(demo);
		ps = mock(PreparedStatement.class);

		when(db.getPreparedStatement("UPDATE park SET currentNumOfVisitors = ? WHERE parkId = ?")).thenReturn(ps);

		try {
			when(ps.executeUpdate()).thenReturn(1);

			// Execution
			parkC.updateNumberOfCurrentVisitor("id", 10);

			// Assert
			verify(ps).setInt(1, demo.currentNumOfVisitors + 10);
			verify(ps).setString(2, demo.parkID);
		} catch (SQLException e) {
			fail();
		}
	}

	/**
	 * <pre>
	 * test UpdateNumberOfCurrentVisitor with Negative number, not go to 0
	 * (number of visitor is bigger than the value to decrease)
	 * input:park with CurrentVisitor Of 10 ,and delta of -9
	 * expected:parkC change in the db the value to 1(10-9)
	 * </pre>
	 */
	@Test
	void testUpdateNumberOfCurrentVisitorNegNot0() {
		getPark(demo);
		ps = mock(PreparedStatement.class);

		when(db.getPreparedStatement("UPDATE park SET currentNumOfVisitors = ? WHERE parkId = ?")).thenReturn(ps);

		try {
			when(ps.executeUpdate()).thenReturn(1);

			// Execution
			parkC.updateNumberOfCurrentVisitor("id", -9);

			// Assert
			verify(ps).setInt(1, demo.currentNumOfVisitors - 9);
			verify(ps).setString(2, demo.parkID);
		} catch (SQLException e) {
			fail();
		}
	}

	/**
	 * <pre>
	 * test UpdateNumberOfCurrentVisitor with Positive number, go above Capacity
	 * (number of visitor is bigger than the value to decrease)
	 * input:park with CurrentVisitor Of 10 ,and delta of 100
	 * expected:parkC change in the db the value to 1(10+100)
	 * </pre>
	 */
	@Test
	void testUpdateNumberOfCurrentVisitorPosAbove() {
		getPark(demo);
		ps = mock(PreparedStatement.class);

		when(db.getPreparedStatement("UPDATE park SET currentNumOfVisitors = ? WHERE parkId = ?")).thenReturn(ps);

		try {
			when(ps.executeUpdate()).thenReturn(1);

			// Execution
			parkC.updateNumberOfCurrentVisitor("id", 100);
			// Assert
			fail();
		} catch (IllegalArgumentException e) {

		} catch (SQLException e) {
			fail();
		}
	}

	/**
	 * <pre>
	 * test UpdateNumberOfCurrentVisitor with Negative number, go under 0
	 * (number of visitor is bigger than the value to decrease)
	 * input:park with CurrentVisitor Of 10 ,and delta of -20
	 * expected:parkC change in the db the value to 1(10-9)
	 * </pre>
	 */
	@Test
	void testUpdateNumberOfCurrentVisitorNegUndr0() {
		getPark(demo);
		ps = mock(PreparedStatement.class);

		when(db.getPreparedStatement("UPDATE park SET currentNumOfVisitors = ? WHERE parkId = ?")).thenReturn(ps);

		try {
			when(ps.executeUpdate()).thenReturn(1);

			// Execution
			parkC.updateNumberOfCurrentVisitor("id", -20);

			// Assert
			fail();
		} catch (IllegalArgumentException e) {

		} catch (SQLException e) {
			fail();
		}
	}

	/**
	 * <pre>
	 * test CheckAvailabilityForNumberOfVisitors
	 * input:park with CurrentVisitor Of 10 ,and capacity of 100
	 * expected:checking 20 visitor should work(10+20)<100
	 * 			checking 950 visitor should not work(10+95)>100
	 * </pre>
	 */
	@Test
	void testCheckAvailabilityForNumberOfVisitors() {
		getPark(demo);

		assertTrue(parkC.checkAvailabilityForNumberOfVisitors("id", 20));
		assertFalse(parkC.checkAvailabilityForNumberOfVisitors("id", 95));
	}

	/** set rs to return the wanted Park */
	ResultSet getPark(Park p) {
		rsPark = mock(ResultSet.class);
		try {
			when(rsPark.next()).thenReturn(true);
			when(rsPark.getString(1)).thenReturn(p.parkID);
			when(rsPark.getString(2)).thenReturn(p.parkName);
			when(rsPark.getInt(3)).thenReturn(p.maxCapacity);
			when(rsPark.getString(4)).thenReturn(p.managerID);
			when(rsPark.getInt(5)).thenReturn(p.maxPreOrders);
			when(rsPark.getDouble(6)).thenReturn(p.avgVisitTime);
			when(rsPark.getInt(7)).thenReturn(p.currentNumOfVisitors);
			when(rsPark.getInt(8)).thenReturn(p.openTime);
			when(rsPark.getInt(9)).thenReturn(p.closeTime);
		} catch (SQLException e) {
		}
		when(db.sendQuery("SELECT * FROM park WHERE parkId = " + p.parkID)).thenReturn(rsPark);
		return rsPark;
	}

	ResultSet getParkList(Collection<Park> col) {
		rsPark = mock(ResultSet.class);
		try {
			OngoingStubbing<Boolean> next = when(rsPark.next());
			OngoingStubbing<String> parkID = when(rsPark.getString(1));
			OngoingStubbing<String> parkName = when(rsPark.getString(2));
			OngoingStubbing<Integer> maxCapacity = when(rsPark.getInt(3));
			OngoingStubbing<String> managerId = when(rsPark.getString(4));
			OngoingStubbing<Integer> maxPreOrders = when(rsPark.getInt(5));
			OngoingStubbing<Double> avgVisitTime = when(rsPark.getDouble(6));
			OngoingStubbing<Integer> currentNumOfVisitors = when(rsPark.getInt(7));
			OngoingStubbing<Integer> openTime = when(rsPark.getInt(8));
			OngoingStubbing<Integer> closeTime = when(rsPark.getInt(9));
			// then=======

			for (Park p : col) {
				next = next.thenReturn(true);
				parkID = parkID.thenReturn(p.parkID);
				parkName = parkName.thenReturn(p.parkName);
				maxCapacity = maxCapacity.thenReturn(p.maxCapacity);
				managerId = managerId.thenReturn(p.managerID);
				maxPreOrders = maxPreOrders.thenReturn(p.maxPreOrders);
				avgVisitTime = avgVisitTime.thenReturn(p.avgVisitTime);
				currentNumOfVisitors = currentNumOfVisitors.thenReturn(p.currentNumOfVisitors);
				openTime = openTime.thenReturn(p.openTime);
				closeTime = closeTime.thenReturn(p.closeTime);
			}
			next.thenReturn(false);
		} catch (SQLException e) {
		}
		when(db.sendQuery("SELECT * FROM park ")).thenReturn(rsPark);
		return rsPark;
	}

}
