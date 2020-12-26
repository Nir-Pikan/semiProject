package logic;

import java.sql.ResultSet;
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
	}

	/**
	 * Create Worker table in DB if not exists
	 * <p>
	 * table of {@link Worker}s
	 */
	private void createTable() {
		dbController.createTable("worker(FirstName varchar(20),LastName varchar(20),WorkerID varchar(12),"
				+ "Email varchar(30),UserName varchar(20),WorkerType varchar(20),Password varchar(20),"
				+ "isLogged varchar(4),permissions varchar(200)," + "primary key(UserName));");
	}

	/**
	 * Request: 1. (request.job = LogInWorker), (request.data = userName password (seprated by space)) 
	 * Request: 2. (request.job = LogOutWorker), (request.data = userName)
	 */
	@Override
	public String handleRequest(ServerRequest request)
    {
		if (request.job.equals("LogInWorker")) {
			String[] parameters = request.data.split(" ");
			String userName = parameters[0];
			String password = parameters[1];
			Worker worker = LogInWorker(userName, password);
			return ServerRequest.gson.toJson(worker, Worker.class);
		}
		if (request.job.equals("LogOutWorker")) 
		{
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
		return dbController.sendUpdate("INSERT INTO worker() VALUES (" + worker.getFirstName() + ","
				+ worker.getLastName() + "," + worker.getWorkerID() + "," + worker.getEmail() + ","
				+ worker.getUserName() + "," + worker.getWorkerType() + "," + "" + worker.getPassword() + ","
				+ ParseIsLoginBoolToString(worker.getIsLogged()) + ","
				+ ParsePermissionsToString(worker.getPermissions()) + ");");
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
		ResultSet result = dbController
				.sendQuery("select * from worker where UserName=" + userName + " AND Password=" + password + ";");
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
				return null;
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

	 * @param workerUserName the worker we want update
	 * @param status the new status we want to update to
	 * @return true if success, false otherwise
	 */
	public boolean updateWorkerLogginDB(String workerUserName, boolean status) 
	{
		return dbController.sendUpdate("UPDATE worker SET isLogged=" + ParseIsLoginBoolToString(status)
				+ " WHERE UserName=" + workerUserName);
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
