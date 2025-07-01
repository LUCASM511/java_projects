package Estoque;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 * Gerencia as operações de estoque e a notificação de listeners sobre alterações no estoque.
 * Interage diretamente com o MongoDB para persistência dos dados.
 * @author Leandro
 */
public class Estoque {
    // Coleção do MongoDB para armazenar documentos de produtos.
    private final MongoCollection<Document> produtosCollection;
    // Lista de listeners que serão notificados quando o estoque for alterado.
    private final List<EstoqueListener> listeners = new ArrayList<>();

    /**
     * Interface para listeners que desejam ser notificados sobre alterações no estoque.
     */
    public interface EstoqueListener {
        /**
         * Chamado quando o estoque é alterado.
         */
        void estoqueAlterado();
    }

    /**
     * Construtor da classe Estoque.
     * Inicializa a conexão com a coleção de produtos no MongoDB através de `MongoDBConnection`.
     */
    public Estoque() {
        this.produtosCollection = MongoDBConnection.getInstance().getCollection();
    }

    /**
     * Adiciona um listener à lista de ouvintes.
     * @param listener O listener a ser adicionado.
     */
    public void addListener(EstoqueListener listener) {
        listeners.add(listener);
    }

    /**
     * Remove um listener da lista de ouvintes.
     * @param listener O listener a ser removido.
     */
    public void removeListener(EstoqueListener listener) {
        listeners.remove(listener);
    }

    /**
     * Notifica todos os listeners registrados que o estoque foi alterado.
     * Este método é chamado após qualquer operação de modificação bem-sucedida (adicionar, remover, atualizar).
     */
    private void notificarListeners() {
        listeners.forEach(EstoqueListener::estoqueAlterado);
    }

    /**
     * Adiciona um novo produto ao estoque no MongoDB.
     * O nome do produto é armazenado em minúsculas para garantir unicidade e facilitar buscas.
     *
     * @param produto O objeto Produto a ser adicionado.
     * @return true se o produto foi adicionado com sucesso, false caso contrário.
     */
    public boolean adicionarProduto(Produto produto) {
        try {
            // Cria um documento MongoDB a partir do objeto Produto.
            // O nome já vem em minúsculas do construtor de Produto.
            Document doc = new Document()
                .append("nome", produto.getNome())
                .append("quantidade", produto.getQuantidade())
                .append("preco", produto.getPreco());

            // Insere o documento na coleção.
            produtosCollection.insertOne(doc);
            // Notifica os listeners sobre a alteração no estoque.
            notificarListeners();
            return true;
        } catch (Exception e) {
            // Exibe uma mensagem de erro em caso de falha na adição.
            JOptionPane.showMessageDialog(null,
                "Erro ao adicionar produto: " + e.getMessage(),
                "Erro no Banco de Dados",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    /**
     * Remove um produto do estoque com base no seu nome.
     * A busca é feita usando o nome em minúsculas.
     *
     * @param nome O nome do produto a ser removido.
     * @return true se o produto foi removido com sucesso, false se não foi encontrado.
     */
    public boolean removerProduto(String nome) {
        // Converte o nome para minúsculas antes de buscar para garantir a correspondência.
        Bson filter = Filters.eq("nome", nome.toLowerCase());
        // Executa a operação de exclusão.
        var result = produtosCollection.deleteOne(filter);
        // Verifica se algum documento foi excluído.
        if (result.getDeletedCount() > 0) {
            // Notifica os listeners sobre a alteração no estoque.
            notificarListeners();
            return true;
        }
        return false;
    }

    /**
     * Atualiza a quantidade e o preço de um produto existente no estoque.
     * A busca do produto é feita pelo nome.
     *
     * @param nome O nome do produto a ser atualizado.
     * @param quantidade A nova quantidade do produto.
     * @param preco O novo preço do produto.
     * @return true se o produto foi atualizado com sucesso, false se não foi encontrado.
     */
    public boolean atualizarProduto(String nome, int quantidade, double preco) {
        // Converte o nome para minúsculas antes de buscar.
        Bson filter = Filters.eq("nome", nome.toLowerCase());
        // Define as atualizações a serem aplicadas.
        Bson updates = Updates.combine(
            Updates.set("quantidade", quantidade),
            Updates.set("preco", preco)
        );

        // Executa a operação de atualização.
        var result = produtosCollection.updateOne(filter, updates);
        // Verifica se algum documento foi modificado.
        if (result.getModifiedCount() > 0) {
            // Notifica os listeners sobre a alteração no estoque.
            notificarListeners();
            return true;
        }
        return false;
    }

    /**
     * Lista todos os produtos atualmente no estoque.
     *
     * @return Uma lista de objetos Produto.
     */
    public List<Produto> listarProdutos() {
        List<Produto> produtos = new ArrayList<>();
        // Itera sobre todos os documentos na coleção de produtos e os converte em objetos Produto.
        produtosCollection.find().forEach(doc ->
            produtos.add(new Produto(
                doc.getString("nome"),
                doc.getInteger("quantidade"),
                doc.getDouble("preco")
            ))
        );
        return produtos;
    }

    /**
     * Busca um produto no estoque pelo seu nome.
     * A busca é insensível a maiúsculas/minúsculas devido à conversão do nome para minúsculas.
     *
     * @param nome O nome do produto a ser buscado.
     * @return O objeto Produto se encontrado, ou null se não for encontrado.
     */
    public Produto buscarProduto(String nome) {
        // Converte o nome para minúsculas antes de buscar.
        Bson filter = Filters.eq("nome", nome.toLowerCase());
        // Encontra o primeiro documento que corresponde ao filtro.
        Document doc = produtosCollection.find(filter).first();

        // Se um documento for encontrado, cria e retorna um objeto Produto.
        if (doc != null) {
            return new Produto(
                doc.getString("nome"),
                doc.getInteger("quantidade"),
                doc.getDouble("preco")
            );
        }
        return null;
    }
}