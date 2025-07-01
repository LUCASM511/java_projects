package org;

public class SalariedEmployee extends Employee{
	private double weeklySalary;

	public SalariedEmployee(String firstName, String lastName, double weeklySalary) {
		super(firstName, lastName);

		if (weeklySalary < 0.0) 
			throw new IllegalArgumentException("Weekly salary must be >= 0.0");

		this.weeklySalary = weeklySalary;
	}

	public double getWeeklySalary() {
		return weeklySalary;
	}

	public void setWeeklySalary(double weeklySalary) {
		this.weeklySalary = weeklySalary;
	}

	@Override
	public String toString() {
		return String.format("salaried employee %s %s: $%,.2f", super.toString(), "weekly salary", getWeeklySalary());
	}

	@Override
	public double earnings() {
		return getWeeklySalary();
	}
}

