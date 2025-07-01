package org.main;

class TestMain {
	public static void main(String []args) {
		RegistrationStudent registrationStudent =  new RegistrationStudent();

		double finalGrade = 0;

		double result = registrationStudent.taskAverage(80, 95, 90);

		System.out.println(result);
		System.out.println(registrationStudent.examFinal(finalGrade));
	}
}
