package entities;

import java.util.ArrayList;
import java.util.List;

/**
 * A class containing a list of {@link Permission}. <br>
 * Each list is for a specific {@link Park}
 */
public class Permissions {

	private String ParkID;
	private List<Permission> PermissionsList;

	public List<Permission> GetPermissions() {
		return PermissionsList;
	}

	/**
	 * creates an empty {@link Permissions} for a {@link Park}
	 * 
	 * @param parkID the park ID to which the permissions are for
	 */
	public Permissions(String parkID) {
		ParkID = parkID;
		PermissionsList = new ArrayList<Permission>();
	}

	/**
	 * creates {@link Permissions} for a {@link Park} using a {@link Permission}
	 * List.
	 * 
	 * @param parkID      the park ID to which the permissions are for
	 * @param permissions a list of existing {@link Permission}s
	 */
	public Permissions(String parkID, List<Permission> permissions) {
		ParkID = parkID;
		PermissionsList = permissions;
	}

	public String GetParkID() {
		return ParkID;
	}

	/**
	 * removes wanted {@link Permission} from the list
	 * 
	 * @param permission the {@link Permission} to be removed
	 */
	public void RemovePermission(Permission permission) {
		PermissionsList.remove(permission);
	}

	/**
	 * adds wanted {@link Permission} to the list
	 * 
	 * @param permission the {@link Permission} to be added
	 */
	public void AddPermission(Permission permission) {
		PermissionsList.add(permission);
	}

	/**
	 * adds a List of {@link Permission} to the list
	 * 
	 * @param permissions the list of {@link Permission}s to be added
	 */
	public void AddPermissions(List<Permission> permissions) {
		for (Permission permission : permissions) {
			PermissionsList.add(permission);
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ParkID == null) ? 0 : ParkID.hashCode());
		result = prime * result + ((PermissionsList == null) ? 0 : PermissionsList.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Permissions other = (Permissions) obj;
		if (ParkID == null) {
			if (other.ParkID != null)
				return false;
		} else if (!ParkID.equals(other.ParkID))
			return false;
		if (PermissionsList == null) {
			if (other.PermissionsList != null)
				return false;
		} else if (!PermissionsList.equals(other.PermissionsList))
			return false;
		return true;
	}
	
	
}
