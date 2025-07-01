package Estoque;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

/**
 * Classe que gerencia a conexão com o banco de dados MongoDB.
 * Implementa o padrão Singleton para garantir que haja apenas uma instância da conexão.
 * @author Leandro
 */
public class MongoDBConnection {
    // Instância única da classe, criada na inicialização (Eager initialization).
    private static final MongoDBConnection INSTANCE = new MongoDBConnection();
    // Cria cliente MongoDB
    private final MongoClient mongoClient;
    // Representa o banco de dados "Estoque_BD".
    private final MongoDatabase database;
    // Representa a coleção dentro do banco de dados.
    private final MongoCollection<Document> collection;
    
    // Conexão com MongoDB
    private static final String CONNECTION_STRING = "mongodb://localhost:27017"; 
    private static final String DATABASE_NAME = "Estoque_BD"; 
    private static final String COLLECTION_NAME = "produtos";
    
    /**
     * Construtor privado para implementar o padrão Singleton.
     * Inicializa a conexão com o MongoDB.
     * Lança uma RuntimeException se a conexão falhar.
     */
    private MongoDBConnection() {
        try {
            // Cria uma nova instância do cliente MongoDB.
            this.mongoClient = MongoClients.create(CONNECTION_STRING);
            this.database = mongoClient.getDatabase(DATABASE_NAME); 
            this.collection = database.getCollection(COLLECTION_NAME);
        } catch (Exception e) {
            // Em caso de falha na inicialização, lança uma exceção em tempo de execução.
            throw new RuntimeException("Falhou em inicializar a conexão com MongoDB", e);
        }
    }
    
    /**
     * Retorna a única instância de `MongoDBConnection`.
     * @return A instância de `MongoDBConnection`.
     */
    public static MongoDBConnection getInstance() {
        return INSTANCE;
    }
    
    /**
     * Retorna a coleção de produtos do MongoDB.
      @return A {@code MongoCollection<Document>} contendo os produtos.
     */
    public MongoCollection<Document> getCollection() {
        return collection;
    }
    
    /**
     * Fecha a conexão com o cliente MongoDB.
     * Deve ser chamado ao encerrar a aplicação para liberar recursos.
     */
    public void close() {
        mongoClient.close();
    }
}