package entities;

import java.util.ArrayList;
import java.util.List;

public class Permissions 
{

	private String ParkID;
	private List<Permission> PermissionsList;
	
	public List<Permission> GetPermissions() 
	{
		return PermissionsList;
	}
	
	public String GetParkID() 
	{
		return ParkID;
	}
	
	public Permissions(String parkID)
	{
		ParkID = parkID;
		PermissionsList = new ArrayList<Permission>();
	}
	
	public Permissions(String parkID , List<Permission> permissions)
	{
		ParkID = parkID;
		PermissionsList = permissions;
	}
	
	public void RemovePermission(Permission permission)
	{
		PermissionsList.remove(permission);
	}
	
	public void AddPermission(Permission permission)
	{
		PermissionsList.add(permission);
	}
	
	public void AddPermissions(List<Permission> permissions)
	{
		for (Permission permission : permissions) 
		{
			PermissionsList.add(permission);
		}
	}
}
