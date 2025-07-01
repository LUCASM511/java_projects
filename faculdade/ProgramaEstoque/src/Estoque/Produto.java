package Estoque;

/**
 * Classe que representa um produto no sistema de estoque.
 * Contém informações sobre nome, quantidade e preço.
 *
 * @author Leandro
 */
public class Produto {
    private final String nome;
    private int quantidade;
    private double preco;

    /**
     * Construtor que inicializa um produto com validação básica dos parâmetros.
     * O nome do produto é armazenado em minúsculas e sem espaços extras.
     * Quantidade e preço são garantidos como não negativos.
     *
     * @param nome Nome do produto (não pode ser nulo ou vazio).
     * @param quantidade Quantidade em estoque (será ajustada para 0 se for negativa).
     * @param preco Preço unitário (será ajustado para 0 se for negativo).
     * @throws IllegalArgumentException Se o nome for nulo ou vazio.
     */
    public Produto(String nome, int quantidade, double preco) {
        // Valida se o nome é nulo ou vazio, lançando uma exceção se for.
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do produto não pode ser nulo ou vazio.");
        }
        // Armazena o nome do produto em minúsculas e remove espaços em branco no início/fim.
        this.nome = nome.trim().toLowerCase();
        // Garante que a quantidade não seja negativa. Se for, define como 0.
        this.quantidade = Math.max(quantidade, 0);
        // Garante que o preço não seja negativo. Se for, define como 0.
        this.preco = Math.max(preco, 0);
    }

    /**
     * Obtém o nome do produto.
     *
     * @return O nome do produto.
     */
    public String getNome() {
        return nome;
    }

    /**
     * Obtém a quantidade em estoque do produto.
     *
     * @return A quantidade do produto.
     */
    public int getQuantidade() {
        return quantidade;
    }

    /**
     * Obtém o preço unitário do produto.
     *
     * @return O preço do produto.
     */
    public double getPreco() {
        return preco;
    }

    /**
     * Retorna uma representação em string formatada do produto.
     *
     * @return Uma string no formato "Produto: [nome], Quantidade: [quantidade], Preço: R$[preço]".
     */
    @Override
    public String toString() {
        return "Produto: " + nome + ", Quantidade: " + quantidade + ", Preço: R$" + preco;
    }
}