package entities;

public class Worker 
{

	private String UserName;
	private String FirstName;
	private String LastName;
	private String WorkerID;
	private String Email;
	private String WorkerType;
	private String Password;
	private Boolean isLogged;
	private Permissions permissions;
	
	public Worker()
	{
		
	}
	
	public Worker(String userName, String firstName, String lastName, String workerID,
			String email, String workerType, String password, Boolean isLogged, Permissions permissions)
	{
		UserName = userName;
		FirstName = firstName;
		LastName = lastName;
		WorkerID = workerID;
		Email = email;
		WorkerType = workerType;
		Password = password;
		this.isLogged = isLogged;
		this.permissions = permissions;
	}
	
	public Permissions getPermissions() {
		return permissions;
	}
	public void setPermissions(Permissions permissions) {
		this.permissions = permissions;
	}
	public String getUserName() {
		return UserName;
	}
	public void setUserName(String userName) {
		UserName = userName;
	}
	public String getFirstName() {
		return FirstName;
	}
	public void setFirstName(String firstName) {
		FirstName = firstName;
	}
	public String getLastName() {
		return LastName;
	}
	public void setLastName(String lastName) {
		LastName = lastName;
	}
	public String getWorkerID() {
		return WorkerID;
	}
	public void setWorkerID(String workerID) {
		WorkerID = workerID;
	}
	public String getEmail() {
		return Email;
	}
	public void setEmail(String email) {
		Email = email;
	}
	public String getWorkerType() {
		return WorkerType;
	}
	public void setWorkerType(String workerType) {
		WorkerType = workerType;
	}
	public String getPassword() {
		return Password;
	}
	public void setPassword(String password) {
		Password = password;
	}
	public Boolean getIsLogged() {
		return isLogged;
	}
	public void setIsLogged(Boolean isLogged) {
		this.isLogged = isLogged;
	}
	
	
}
