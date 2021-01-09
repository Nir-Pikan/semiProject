package entities;

/** A class representing a specific permission. */
public class Permission {
	private String Name;

	/**
	 * creates a {@link Permission} with a name
	 * 
	 * @param name the premission's name
	 */
	public Permission(String name) {
		Name = name;
	}

	public String GetName() {
		return Name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Name == null) ? 0 : Name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) // Equals by Name
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
