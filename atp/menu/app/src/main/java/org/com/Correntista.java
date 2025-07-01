package org.com;

public class Correntista {
	private String nome;
	private int cpf;

	public Correntista(String nome, int cpf) {
		this.nome = nome;
		this.cpf = cpf;
	}

	@Override
	public String toString() {
		return "Correntista{" + "nome=" + nome + ", cpf=" + cpf + '}';
	}
}
