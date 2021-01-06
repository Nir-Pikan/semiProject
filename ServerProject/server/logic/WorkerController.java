package logic;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import entities.Permission;
import entities.Permissions;
import entities.Worker;
import io.DbController;
import modules.IController;
import modules.ServerRequest;

/** the worker controller class */
public class WorkerController implements IController {
	DbController dbController;

	/** creates the {@link WorkerController} */
	public WorkerController() {
		dbController = DbController.getInstance();
		createTable();

		dbController.sendUpdate("UPDATE workers SET isLogged=\"NO\" WHERE username<>\"\"");// disconnect all clients
	}

	/**
	 * Create Worker table in DB if not exists
	 * <p>
	 * table of {@link Worker}s
	 */
	private void createTable() {
		dbController.createTable("workers(firstName varchar(20),lastName varchar(20),workerID varchar(12),"
				+ "email varchar(30),userName varchar(20),workerType varchar(20),password varchar(20),"
				+ "isLogged varchar(4),permissions varchar(200)," + "primary key(userName));");
	}

	/**
	 * Request: 1. (request.job = LogInWorker), (request.data = [userName ,
	 * password])as String[] Request: 2. (request.job = LogOutWorker), (request.data
	 * = userName)
	 */
	@Override
	public String handleRequest(ServerRequest request) {
		if (request.job.equals("LogInWorker")) {
			String[] parameters = ServerRequest.gson.fromJson(request.data, String[].class);
			String userName = parameters[0];
			String password = parameters[1];
			try {
			Worker worker = LogInWorker(userName, password);
			return ServerRequest.gson.toJson(worker, Worker.class);
			}catch(IllegalArgumentException ex) {
				if(ex.getMessage().equals("logged"))
					return "user already logged in";
			}
			
		}
		if (request.job.equals("LogOutWorker")) {
			String userName = request.data;
			boolean logoutSucceded = updateWorkerLogginDB(userName, false);
			return ServerRequest.gson.toJson(logoutSucceded, boolean.class);
		}
		return null;
	}

	/**
	 * add worker to DB
	 * 
	 * @param worker the worker we want to add
	 * @return true if success, false otherwise
	 */
	public boolean AddWorker(Worker worker) {
		worker.setIsLogged(false);
		PreparedStatement ps = dbController
				.getPreparedStatement("INSERT IGNORE INTO workers VALUES (?,?,?,?,?,?,?,?,?);");
		try {
			ps.setString(1, worker.getFirstName());
			ps.setString(2, worker.getLastName());
			ps.setString(3, worker.getWorkerID());
			ps.setString(4, worker.getEmail());
			ps.setString(5, worker.getUserName());
			ps.setString(6, worker.getWorkerType());
			ps.setString(7, worker.getPassword());
			ps.setString(8, ParseIsLoginBoolToString(worker.getIsLogged()));
			ps.setString(9, ParsePermissionsToString(worker.getPermissions()));
			ps.executeUpdate();
		} catch (SQLException e) { // if adding the worker failed
			e.printStackTrace();
			return false;
		}
		return true;

		// TODO delete, bugged ~Nir Pikan~
//		return dbController.sendUpdate("INSERT INTO worker VALUES (" + worker.getFirstName() + ","
//				+ worker.getLastName() + "," + worker.getWorkerID() + "," + worker.getEmail() + ","
//				+ worker.getUserName() + "," + worker.getWorkerType() + "," + worker.getPassword() + ","
//				+ ParseIsLoginBoolToString(worker.getIsLogged()) + ","
//				+ ParsePermissionsToString(worker.getPermissions()) + ");");
	}

	/**
	 * if this userName exist in DB and he has this password and he is not logged in
	 * return this worker and update his logged in status, else return null
	 * 
	 * @param userName login user name
	 * @param password login password
	 * @return if successes return logged in worker, null otherwise
	 */
	public Worker LogInWorker(String userName, String password) {
		ResultSet result = dbController.sendQuery(
				"select * from workers where userName=\"" + userName + "\" AND password=\"" + password + "\"");
		try {
			if (!result.next())
				return null;
			String FirstName = result.getString(1);
			String LastName = result.getString(2);
			String WorkerID = result.getString(3);
			String Email = result.getString(4);
			String WorkerType = result.getString(6);
			boolean isLogged = ParseIsLoginStringToBool(result.getString(8));
			Permissions permissions = ParseStringToPermissions(result.getString(9));
			if (isLogged)
				throw new IllegalArgumentException("logged");
			isLogged = true;
			Worker worker = new Worker(userName, FirstName, LastName, WorkerID, Email, WorkerType, password, isLogged,
					permissions);
			if (!updateWorkerLogginDB(worker.getUserName(), true))
				return null;
			return worker;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * update the worker log in status in DB
	 * 
	 * @param workerUserName the worker we want update
	 * @param status         the new status we want to update to
	 * @return true if success, false otherwise
	 */
	public boolean updateWorkerLogginDB(String workerUserName, boolean status) {
		return dbController.sendUpdate("UPDATE workers SET isLogged=\"" + ParseIsLoginBoolToString(status)
				+ "\" WHERE UserName=\"" + workerUserName + "\"");
	}

	/**
	 * converts {@link Permissions} into string
	 * 
	 * @param permissions the {@link Permissions} we want to convert
	 * @return the output String
	 */

	private String ParsePermissionsToString(Permissions permissions) {
		String output = permissions.GetParkID();
		for (Permission permission : permissions.GetPermissions()) {

			output = output + " " + permission.GetName();
		}
		return output;
	}

	/**
	 * converts a string to a {@link Permissions} and add them to wanted
	 * {@link Worker}
	 * 
	 * @param permissionsString the String we want to convert
	 * @return the {@link Permissions} made using the String
	 * @apiNote the start of the String should contain the ParkID to be used
	 * @apiNote each {@link Permissions} should be split via space
	 */
	private Permissions ParseStringToPermissions(String permissionsString) {
		permissionsString = permissionsString.trim();
		String[] permissionsStringArray = permissionsString.split(" ");
		String ParkID = permissionsStringArray[0];
		Permissions permissions = new Permissions(ParkID);
		for (String permission : permissionsStringArray) {
			if (!permission.equals(ParkID))
				permissions.AddPermission(new Permission(permission));
		}
		return permissions;
	}

	/**
	 * checks if login String is Yes
	 * 
	 * @param loginString the string we want to check
	 * @return true if YES, false otherwise
	 */
	private boolean ParseIsLoginStringToBool(String loginString) {
		return loginString.equals("YES");
	}

	/**
	 * converts boolean to login String
	 * 
	 * @param loginBool the boolean we want to convert
	 * @return YES if true, NO if false
	 */
	private String ParseIsLoginBoolToString(Boolean loginBool) {
		if (loginBool)
			return "YES";
		return "NO";
	}
}
