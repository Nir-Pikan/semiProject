package clientGui;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Timestamp;
import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import clientIO.ConnectionInterface;
import entities.ParkEntry;
import entities.ParkEntry.EntryType;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXMLLoader;
import modules.ServerRequest;

class EntryReportControllerTest {

	private EntryReportController entryReportController;

	private ParkEntry[] checkEntries=null;
	private ParkEntry[] entries=null;
	private Timestamp[] dates=null;
	private ConnectionInterface connection=null;
	private Timestamp fromDate=null;
	private Timestamp toDate=null;
	private static String titlePopUp = null;
	private static String headerPopUp = null;
	private static String bodyPopUp = null;
	private JFXPanel panel = new JFXPanel(); // for javaFX thread creation 
	private double[] expectedAvgStayArray;
	private int[] expectedTotalPeopleType;
	private int[][] expectedSumPeople;
	
	/**
	 * creating stub connection for reciving fixed results
	 *
	 */
	private class ConnectionSutb implements ConnectionInterface {

		@Override
		public String sendRequestAndResponse(ServerRequest request) {
			return ServerRequest.gson.toJson(checkEntries, ParkEntry[].class);
		}

	}

	/**
	 * seting stub for pop up 
	 */
	private static void setPopUp() {
		PopUp.myPop = new PopUp.IPopUp() {

			@Override
			public void showInformation(String title, String header, String body) {
				titlePopUp = title;
				headerPopUp = header;
				bodyPopUp = body;
			}

			@Override
			public void showError(String title, String header, String body) {
			}

			@Override
			public boolean ask(String title, String header, String body) {
				return false;
			}
		};
	}

	@BeforeEach
	void setUp() throws Exception {

		// set stub server connection
		connection = new ConnectionSutb();

		//getting my window to test fow fxml entities
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(EntryReportController.class.getResource("EntryReport.fxml"));
		loader.load();

		//initiliaze stub connection
		entryReportController = loader.getController();
		entryReportController.setServerConnection(connection);

		// set relevent dates
		LocalDate localDate = LocalDate.of(2021, 1, 1);
		fromDate = Timestamp.valueOf(localDate.atStartOfDay());
		toDate = Timestamp.valueOf(localDate.atStartOfDay().plusDays(1));
		dates = new Timestamp[2];
		dates[0] = fromDate;
		dates[1] = toDate;
		entries = new ParkEntry[4];
		
		// set parkeEteries for checking
		entries[0] = new ParkEntry(EntryType.Personal, "1", "park1", fromDate,
				Timestamp.valueOf(fromDate.toLocalDateTime().plusHours(1)), 1, 0, false, 10);
		entries[1] = new ParkEntry(EntryType.Subscriber, "2", "park1", fromDate,
				Timestamp.valueOf(fromDate.toLocalDateTime().plusHours(2)), 1, 1, false, 20);
		entries[2] = new ParkEntry(EntryType.Group, "3", "park1", fromDate,
				Timestamp.valueOf(fromDate.toLocalDateTime().plusHours(3)), 10, 10, false, 100);

		entries[3] = new ParkEntry(EntryType.PrivateGroup, "4", "park1", fromDate,
				Timestamp.valueOf(fromDate.toLocalDateTime().plusHours(4)), 10, 5, false, 200);

		//initiliaze relevent arrays
		expectedAvgStayArray = new double[ParkEntry.EntryType.values().length];
		expectedTotalPeopleType = new int[ParkEntry.EntryType.values().length];
		for (int i = 0; i < 4; i++) {
			expectedAvgStayArray[i] = 0;
			expectedTotalPeopleType[i] = 0;
		}
		expectedSumPeople = new int[ParkEntry.EntryType.values().length][24];
		for (int i = 0; i < ParkEntry.EntryType.values().length; i++) {
			for (int j = 0; j < 24; j++) {
				expectedSumPeople[i][j] = 0;
			}
		}

	}

	/**
	 * <pre>
	 * test for getting currect calculation for avarage time of stay for each type
	 * input: parkName:Gold, parkID:park1, dates:{1.1.2021,2.1.2021}
	 * expected: expectedAvgStayArray[i]=entryReportController.avgStayArray[i] for each i
	 * </pre>
	 */
	@Test
	void testAvgStayCurrectParkEntries() {
		expectedAvgStayArray[0] = 1;
		expectedAvgStayArray[1] = 2;
		expectedAvgStayArray[2] = 3;
		expectedAvgStayArray[3] = 4;
		checkEntries = entries;
		entryReportController.initReport("Gold", "park1", dates);
		assertArrayEquals(expectedAvgStayArray, entryReportController.avgStayArray);
	}

	/**
	 * <pre>
	 * test for getting currect calculation for total sum of people stay for each type
	 * input: parkName:Gold, parkID:park1, dates:{1.1.2021,2.1.2021}
	 * expected: expectedTotalPeopleType[i]=entryReportController.totalPeopleType[i] for each i
	 * </pre>
	 */
	@Test
	void testTotalPeopleOfTypeCurrectParkEntries() {
		expectedTotalPeopleType[0] = 1;
		expectedTotalPeopleType[1] = 1;
		expectedTotalPeopleType[2] = 10;
		expectedTotalPeopleType[3] = 10;
		checkEntries = entries;
		entryReportController.initReport("Gold", "park1", dates);
		assertArrayEquals(expectedTotalPeopleType, entryReportController.totalPeopleType);
	}

	/**
	 * <pre>
	 * test if all entries was recived
	 * input: parkName:Gold, parkID:park1, dates:{1.1.2021,2.1.2021}
	 * expected: all entries exist
	 * </pre>
	 */
	@Test
	void testGotAllParkEntries() {
		checkEntries = entries;
		entryReportController.initReport("Gold", "park1", dates);
		assertEquals(entries.length, entryReportController.entries.length);
		assertArrayEquals(entries, entryReportController.entries);
	}

	/**
	 * <pre>
	 * test if all parameters is set to 0 and the entries are empty
	 * input: parkName:Gold, parkID:park1, dates:{1.1.2021,2.1.2021}
	 * expected: entries to be empty and all values is 0
	 * </pre>
	 */
	@Test
	void testNoEntriesInDates() {
		checkEntries = new ParkEntry[0];
		entryReportController.initReport("Gold", "park1", dates);
		assertEquals(0, entryReportController.entries.length);
		assertArrayEquals(expectedTotalPeopleType, entryReportController.totalPeopleType);
		assertArrayEquals(expectedAvgStayArray, entryReportController.avgStayArray);
		assertArrayEquals(new ParkEntry[0], entryReportController.entries);

	}

	/**
	 * <pre>
	 * test only one type enters
	 * input: parkName:Gold, parkID:park1, dates:{1.1.2021,2.1.2021}
	 * expected: entries to be empty and all values is 0 execept one Type
	 * </pre>
	 */
	@Test
	void testOnlyOneTypeEnters() {
		checkEntries = new ParkEntry[1];
		checkEntries[0] = new ParkEntry(EntryType.Personal, "1", "park1", fromDate,
				Timestamp.valueOf(fromDate.toLocalDateTime().plusHours(1)), 100, 0, false, 10);
		entryReportController.initReport("Gold", "park1", dates);
		expectedTotalPeopleType[0] = 100;
		expectedAvgStayArray[0] = 1;
		assertEquals(1, entryReportController.entries.length);
		assertArrayEquals(expectedTotalPeopleType, entryReportController.totalPeopleType);
		assertArrayEquals(expectedAvgStayArray, entryReportController.avgStayArray);
		assertArrayEquals(checkEntries, entryReportController.entries);
	}

	/**
	 * <pre>
	 * test entries return from server are null
	 * input: parkName:Gold, parkID:park1, dates:{1.1.2021,2.1.2021}
	 * expected: should show popup
	 * </pre>
	 */
	@Test
	void testEnriesEqualsNull() {
		setPopUp();
		checkEntries = null;
		entryReportController.initReport("Gold", "park1", dates);
		assertEquals("Server didnt found entries", titlePopUp);
		assertEquals("Server Failure:Server didnt found entries", headerPopUp);
		assertEquals("Server didnt found entries", bodyPopUp);
	}
	
	/**
	 * <pre>
	 * sum of people devided by hours are equal
	 * input: parkName:Gold, parkID:park1, dates:{1.1.2021,2.1.2021}
	 * expected: expectedSumPeople array equals SumPeople in controller
	 * </pre>
	 */
	@Test
	void testValidSum() {
		int[][] expectedSumPeople = new int[ParkEntry.EntryType.values().length][24];
		for (int i = 0; i < ParkEntry.EntryType.values().length; i++) {
			for (int j = 0; j < 24; j++) {
				expectedSumPeople[i][j] = 0;
			}
		}
		checkEntries = entries;
		entryReportController.initReport("Gold", "park1", dates);
		expectedSumPeople[0][fromDate.toLocalDateTime().getHour()] = 1;
		expectedSumPeople[1][fromDate.toLocalDateTime().getHour()] = 1;
		expectedSumPeople[2][fromDate.toLocalDateTime().getHour()] = 10;
		expectedSumPeople[3][fromDate.toLocalDateTime().getHour()] = 10;

		assertArrayEquals(expectedSumPeople, entryReportController.sumPeople);
	}
	

}
