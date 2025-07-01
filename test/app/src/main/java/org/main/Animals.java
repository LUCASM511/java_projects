package org.main;

public class Animals{
	private String especie;
	private int age;

	public Animals(String especie, int age) {
		this.especie = especie;
		this.age = age;
	}


	public String getEspecie() {
		return especie;
	}

	public void setEspecie(String especie) {
		this.especie = especie;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	@Override
	public String toString() {
		return "Especie: " + getEspecie() + "\n" + "Age: " + getAge() + "\n";
	}

}
