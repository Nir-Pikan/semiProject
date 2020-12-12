package entities;

public class Permission 
{
	private String Name;
	
	public String GetName() 
	{
		return Name;
	}
	
	public Permission(String name)
	{
		Name = name;
	}
	
	@Override
	public int hashCode()
{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Name == null) ? 0 : Name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) //Equals by Name 
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Permission other = (Permission) obj;
		if (Name == null) {
			if (other.Name != null)
				return false;
		} else if (!Name.equals(other.Name))
			return false;
		return true;
	}
}
