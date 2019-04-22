
public class Actor {

	private String name;
	private String surName;

	public Actor(String name, String surName) {
		super();
		this.name = name;
		this.surName = surName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurName() {
		return surName;
	}

	public void setSurName(String surName) {
		this.surName = surName;
	}

	@Override
	public String toString() {
		return getFullName();
	}

	public String getFullName() {
		return getName() + " " + getSurName();
	}

}
