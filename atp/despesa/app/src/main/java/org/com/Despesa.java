package org.com;

public class Despesa {
	private double meses;
	private double resultado;

	public void setDados(double meses) {
		this.meses = meses;
	}

	public double calcula(double janeiro, double fevereiro, double marco) {

		return this.resultado = (janeiro + fevereiro + marco);

	}

	public void display() {
		System.out.println("Mes" + meses);
	}
}
