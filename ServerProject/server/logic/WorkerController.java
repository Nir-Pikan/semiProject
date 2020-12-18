package logic;

import java.sql.ResultSet;
import entities.Permission;
import entities.Permissions;
import entities.Worker;
import io.DbController;
import modules.IController;
import modules.ServerRequest;

public class WorkerController implements IController 
{
	DbController dbController ;
	
	/**Create worker table in DB if not exist*/
	public WorkerController() 
	{
		dbController = DbController.getInstance();
		createTable();
	}
	
	private void createTable() 
	{
		dbController.createTable("worker(FirstName varchar(20),LastName varchar(20),WorkerID varchar(12),"
				+ "Email varchar(30),UserName varchar(20),WorkerType varchar(20),Password varchar(20),"
				+ "isLogged varchar(4),permissions varchar(200),"
				+ "primary key(UserName));"); 
	}
		
	/** Request: 1. (request.job = LogInWorker), (request.data = userName password) */
	@Override
	public String handleRequest(ServerRequest request) 
	{	
		if(request.job.equals("LogInWorker"))
		{
			String[] parameters = request.data.split(" ");
			String userName = parameters[0];
			String password = parameters[1];
			Worker worker = LogInWorker(userName, password);	
			return ServerRequest.gson.toJson(worker,Worker.class);
		}
		return null;
	}
	
	/** add worker to DB, return true if success*/
	public boolean AddWorker(Worker worker) 
	{
      worker.setIsLogged(false);
	  return dbController.sendUpdate("INSERT INTO worker() VALUES ("+worker.getFirstName()+","+worker.getLastName()+","+worker.getWorkerID()+","
	  		+ worker.getEmail()+","+worker.getUserName()+","+worker.getWorkerType()+","
	  		+ ""+worker.getPassword()+","+ParseIsLogginBoolToString(worker.getIsLogged())+","
	        + ParsePermissionsToString(worker.getPermissions()) + ");");
	}
	
	/** if this userName exist in DB and he has this password and he not log in return this worker and update that he log in, else return null*/
	public Worker LogInWorker(String userName, String password)
	{
		ResultSet result = dbController.sendQuery("select * from worker where UserName=" + userName + " AND Password=" + password + ";");
		try 
		{
			if(!result.next())
				return null;
			String FirstName = result.getString(1);
			String LastName = result.getString(2);
			String WorkerID = result.getString(3);
			String Email = result.getString(4);
			String WorkerType = result.getString(6);
			boolean isLogged = ParseIsLogginStringToBool(result.getString(8));
			Permissions permissions = ParseStringToPermissions(result.getString(9), WorkerID);
			if(isLogged)
				return null;
			isLogged = true;
			Worker worker = new Worker(userName, FirstName, LastName, WorkerID, Email, WorkerType, password, isLogged, permissions);		
			if(!updateWorkerLogginDB(worker))
				return null;
			return worker;		
		} 
		catch (Exception e) 
		{
			return null;
		} 
	}
	
	/** update the worker log in status in DB (worker.IsLogged property need to be updated before), return true if success*/
	public boolean updateWorkerLogginDB(Worker worker) 
	{
		return dbController.sendUpdate("UPDATE worker SET isLogged=" + ParseIsLogginBoolToString(worker.getIsLogged()) + " WHERE UserName=" + worker.getUserName());
	}
	
	private String ParsePermissionsToString(Permissions permissions)
	{
		String output = "";
		for (Permission permission : permissions.GetPermissions()) 
		{
			output = output + " " + permission.GetName();
		}
		return output;
	}
	
	private Permissions ParseStringToPermissions(String permissionsString, String WorkerID)
	{
		Permissions permissions = new Permissions(WorkerID);
		permissionsString = permissionsString.trim();
		String[] permissionsStringArray = permissionsString.split(" ");
		for (String permission : permissionsStringArray) 
		{
			permissions.AddPermission(new Permission(permission));
		}
		return permissions;
	}
	
	private boolean ParseIsLogginStringToBool(String logginString)
	{
		return logginString.equals("YES");		
	}
	
	private String ParseIsLogginBoolToString(Boolean logginBool)
	{
		if(logginBool)
		  return "YES";
		return "NO";
	}
}
