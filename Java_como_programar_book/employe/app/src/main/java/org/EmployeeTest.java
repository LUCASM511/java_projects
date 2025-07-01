package org;

public class EmployeeTest {
	public static void main(String []args) {

		SalariedEmployee salariedEmployee = new SalariedEmployee("Nepturno", "CK21", 9400.50);

		Employee[] employees = new Employee[1];
		employees[0] = salariedEmployee;

		for (Employee employee: employees)
			System.out.println(employee);

	}
}
