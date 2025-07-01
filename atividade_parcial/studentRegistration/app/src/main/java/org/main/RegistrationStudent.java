package org.main;

class RegistrationStudent {
	private String register;
	private String name;
	private double examGrade1;
	private double examGrade2;
	private double taskGrade;

	public String getRegister() {
		return this.register;
	}

	public void setRegister(String register) {
		this.register = register;
	} 

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getExamGrade1() {
		return examGrade1;
	}

	public void setExamGrade1(double examGrade1) {
		this.examGrade1 = examGrade1;
	}

	public double getExamGrade2() {
		return examGrade2;
	}

	public void setExamGrade2(double examGrade2) {
		this.examGrade2 = examGrade2;
	}

	public double getTaskGrade() {
		return taskGrade;
	}

	public void setTaskGrade(double taskGrade) {
		this.taskGrade = taskGrade;
	}

	public double taskAverage(double exam1, double exam2, double task) {
		double average = 0;
		average = (exam1 + exam2 + task)  / 3;
		examFinal(average);
		return average;
	}

	public int examFinal(double average) {
		if (average < 50) {
			return 1;
		}
		return 0;
	}
}
