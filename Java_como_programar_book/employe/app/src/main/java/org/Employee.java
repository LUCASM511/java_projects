package org;

public abstract class Employee {
	private final String firstName;
	private final String lastName;


	public Employee(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	@Override
	public String toString() {
		return String.format("%s %s", getFirstName(), getLastName());
	}

	public abstract double earnings();
}
