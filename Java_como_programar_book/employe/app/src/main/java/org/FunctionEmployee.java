package org;

public class FunctionEmployee extends Employee{
	SalariedEmployee salariedEmployee;
	private String area;

	public FunctionEmployee(String firstName, String lastName, String area) {
		super(firstName, lastName);
		this.area = area;
	}

	@Override
	public double earnings() {
		return salariedEmployee.getWeeklySalary();
	}

	public SalariedEmployee getSalariedEmployee() {
		return salariedEmployee;
	}

	public void setSalariedEmployee(SalariedEmployee salariedEmployee) {
		this.salariedEmployee = salariedEmployee;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	};
}
