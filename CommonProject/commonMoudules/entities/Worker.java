package entities;

/**
 * A class containing the data of a GoNature's worker.
 * <p>
 * Each worker contains {@link Permissions}
 */
public class Worker {

	private String UserName;
	private String FirstName;
	private String LastName;
	private String WorkerID;
	private String Email;
	private String WorkerType;
	private String Password;
	private Boolean isLogged;
	private Permissions permissions;

	// TODO Why create an empty worker? ~Nir Pikan~
	public Worker() {

	}

	/**
	 * creates a {@link Worker} using all fields.
	 * 
	 * @param userName    String
	 * @param firstName   String
	 * @param lastName    String
	 * @param workerID    String
	 * @param email       String
	 * @param workerType  String
	 * @param password    String
	 * @param isLogged    Boolean
	 * @param permissions {@link Permissions}
	 */
	public Worker(String userName, String firstName, String lastName, String workerID, String email, String workerType,
			String password, Boolean isLogged, Permissions permissions) {
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

	/** sets the worker's {@link Permissions} */
	public void setPermissions(Permissions permissions) {
		this.permissions = permissions;
	}

	public String getUserName() {
		return UserName;
	}

	/** sets the worker's userName */
	public void setUserName(String userName) {
		UserName = userName;
	}

	public String getFirstName() {
		return FirstName;
	}

	/** sets the worker's firstName */
	public void setFirstName(String firstName) {
		FirstName = firstName;
	}

	public String getLastName() {
		return LastName;
	}

	/** sets the worker's lastName */
	public void setLastName(String lastName) {
		LastName = lastName;
	}

	public String getWorkerID() {
		return WorkerID;
	}

	/** sets the worker's workerID */
	public void setWorkerID(String workerID) {
		WorkerID = workerID;
	}

	public String getEmail() {
		return Email;
	}

	/** sets the worker's email */
	public void setEmail(String email) {
		Email = email;
	}

	public String getWorkerType() {
		return WorkerType;
	}

	/** sets the worker's workerType */
	public void setWorkerType(String workerType) {
		WorkerType = workerType;
	}

	public String getPassword() {
		return Password;
	}

	/** sets the worker's password */
	public void setPassword(String password) {
		Password = password;
	}

	public Boolean getIsLogged() {
		return isLogged;
	}

	/** sets the worker's Logged in status */
	public void setIsLogged(Boolean isLogged) {
		this.isLogged = isLogged;
	}

}
