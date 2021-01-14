package serverLogic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import db.DbController;
import entities.Permission;
import entities.Permissions;
import entities.Worker;
import modules.ServerRequest;
import modules.ServerRequest.Manager;

class WorkerControllerTest {
	@Mock
	DbController db;

	@Mock
	ResultSet rsWorker;

	WorkerController wCont;

	Worker testWorker;

	@BeforeEach
	void setUp() throws Exception {
		db = mock(DbController.class);
		wCont = new WorkerController(db);
		Permissions perm = new Permissions("park");
		perm.AddPermission(new Permission("stam"));
		perm.AddPermission(new Permission("stam2"));
		testWorker = new Worker("user", "john", "dou", "111111111", "john@doe.com", "worker", "Passw0rd1", false, perm);
	}

	/**
	 * <pre>
	 * test connection if username or password wrong
	 * input: testWorker, connection with wrong password and one connection with wrong username
	 * expected: the request returns null in both cases, and no update happened(for setting worker as connected)
	 * </pre>
	 */
	@Test
	void testLoginWrongUserNameOrPassword() {
		getWorker(testWorker);
		ResultSet rsWorkerNotFound = mock(ResultSet.class);
		try {
			when(rsWorkerNotFound.next()).thenReturn(false);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		when(db.sendQuery("select * from workers where userName=\""+testWorker.getUserName()+"\" AND password=\"bla\"")).thenReturn(rsWorkerNotFound);
		when(db.sendQuery("select * from workers where userName=\"bla\" AND password=\""+testWorker.getPassword()+"\"")).thenReturn(rsWorkerNotFound);
		String actual = wCont.handleRequest(new ServerRequest(Manager.Worker, "LogInWorker", "[\"bla\",\""+testWorker.getPassword()+"\"]"));
		assertEquals("null", actual);
		String actual2 = wCont.handleRequest(new ServerRequest(Manager.Worker, "LogInWorker", "[\""+testWorker.getUserName()+"\",\"bla\"]"));
		assertEquals("null", actual2);
		verify(db,never()).sendUpdate(anyString());//NO connection updates happened
	}

	/**
	 * <pre>
	 * test connection when the user logged in
	 * input: testWorker set to be connected, connection with good credentials 
	 * expected: the request returns "user already logged in", and no update happened(for setting worker as connected)
	 * </pre>
	 */
	@Test
	void testLoginUserLogedIn() {
		testWorker.setIsLogged(true);
		getWorker(testWorker);
		String actual = wCont.handleRequest(new ServerRequest(Manager.Worker, "LogInWorker", "[\""+testWorker.getUserName()+"\",\""+testWorker.getPassword()+"\"]"));
		assertEquals("user already logged in", actual);
		verify(db,never()).sendUpdate(anyString());//NO connection updates happened
	}
	
	/**
	 * <pre>
	 * test connection when the user not already logged in
	 * input: testWorker, connection with good credentials 
	 * expected: the request returns JSON representation of the worker, with update happened(for setting worker as connected),
	 * 		and with isLogged as true
	 * </pre>
	 */
	@Test
	void testLoginSuccess() {
		getWorker(testWorker);
		
		String response = wCont.handleRequest(new ServerRequest(Manager.Worker, "LogInWorker", "[\"user\",\"Passw0rd1\"]"));
		Worker actual = ServerRequest.gson.fromJson(response, Worker.class);
		assertEquals(testWorker, actual);
		assertTrue(actual.getIsLogged());
		verify(db,times(1)).sendUpdate(anyString());
	}
	
	/**
	 * helper method to set the rsWorker to return the worker
	 * @param worker
	 */
	private void getWorker(Worker worker) {
		rsWorker = mock(ResultSet.class);
		try {
			when(rsWorker.next()).thenReturn(true);
			when(rsWorker.getString(1)).thenReturn(worker.getFirstName());
			when(rsWorker.getString(2)).thenReturn(worker.getLastName());
			when(rsWorker.getString(3)).thenReturn(worker.getWorkerID());
			when(rsWorker.getString(4)).thenReturn(worker.getEmail());
			when(rsWorker.getString(5)).thenReturn(worker.getUserName());
			when(rsWorker.getString(6)).thenReturn(worker.getWorkerType());
			when(rsWorker.getString(7)).thenReturn(worker.getPassword());
			when(rsWorker.getString(8)).thenReturn(WorkerController.ParseIsLoginBoolToString(worker.getIsLogged()));
			when(rsWorker.getString(9)).thenReturn(WorkerController.ParsePermissionsToString(worker.getPermissions()));
			when(db.sendQuery("select * from workers where userName=\"" + worker.getUserName() + "\" AND password=\""
					+ worker.getPassword() + "\"")).thenReturn(rsWorker);
			when(db.sendUpdate("UPDATE workers SET isLogged=\"" + WorkerController.ParseIsLoginBoolToString(true)
			+ "\" WHERE UserName=\"" + testWorker.getUserName() + "\"")).thenReturn(true);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
