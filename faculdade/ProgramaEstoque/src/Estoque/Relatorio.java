package Estoque;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream; 
import java.io.PrintWriter; 
import java.io.OutputStreamWriter; 
import java.nio.charset.StandardCharsets; 

/**
 * A classe `Relatorio` representa um diálogo para exibir um relatório detalhado do estoque.
 * Ele mostra os produtos em uma tabela, calcula o valor total do estoque e permite exportar
 * os dados para um arquivo CSV.
 * @author Leandro
 */
public class Relatorio extends JDialog {
    private JTable tabela; 
    private DefaultTableModel modelo; 
    private final JButton exportarBtn; 
    private final Estoque estoque; 
    private final JLabel totalValueLabel; 

    /**
     * Construtor da classe Relatorio.
     * @param parent O JFrame pai deste diálogo.
     * @param estoque A instância do objeto Estoque que contém os dados a serem exibidos.
     */
    public Relatorio(JFrame parent, Estoque estoque) {
        super(parent, "Relatório de Estoque", true); // Chama o construtor da superclasse JDialog (modal)
        this.estoque = estoque; // Atribui a instância do estoque
        setSize(600, 400); // Define o tamanho preferencial do diálogo
        setLocationRelativeTo(parent); // Centraliza o diálogo em relação ao componente pai
        setLayout(new BorderLayout()); // Define o layout do diálogo como BorderLayout

        // Inicializa o JLabel para exibir o valor total do estoque.
        totalValueLabel = new JLabel();
        totalValueLabel.setHorizontalAlignment(SwingConstants.RIGHT); // Alinha o texto à direita
        totalValueLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); // Adiciona um preenchimento

        // Cria a tabela e preenche os dados, atualizando também o valor total.
        criarTabela();
        
        // Inicializa o botão de exportação e adiciona um ActionListener.
        exportarBtn = new JButton("Exportar para CSV");
        exportarBtn.addActionListener(e -> exportarParaCSV()); // Chama o método de exportação ao clicar no botão
        
        // Cria um painel inferior para agrupar o label do valor total e o botão de exportação.
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(totalValueLabel, BorderLayout.WEST); // Adiciona o label à esquerda
        southPanel.add(exportarBtn, BorderLayout.EAST);     // Adiciona o botão à direita
        
        // Adiciona os componentes principais ao diálogo.
        add(new JScrollPane(tabela), BorderLayout.CENTER); // Adiciona a tabela com scroll no centro
        add(southPanel, BorderLayout.SOUTH); // Adiciona o painel inferior na parte inferior
    }

    /**
     * Cria e preenche a JTable com os dados dos produtos do estoque,
     * e calcula o valor total do estoque para exibir no `totalValueLabel`.
     */
    private void criarTabela() {
        String[] colunas = {"Produto", "Quantidade", "Preço Unitário", "Valor Total"}; // Nomes das colunas
        modelo = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Impede a edição direta das células da tabela.
            }
        };

        tabela = new JTable(modelo); // Cria a tabela com o modelo.
        
        double currentValorTotalEstoque = 0; // Variável para acumular o valor total de todos os produtos.
        // Itera sobre todos os produtos no estoque.
        for (Produto p : estoque.listarProdutos()) {
            double valorTotalLinha = p.getQuantidade() * p.getPreco(); // Calcula o valor total para cada linha.
            currentValorTotalEstoque += valorTotalLinha; // Acumula o valor no total do estoque.
            // Adiciona uma nova linha à tabela com os dados do produto e seus valores formatados.
            modelo.addRow(new Object[]{
                p.getNome(),
                p.getQuantidade(),
                formatarMoeda(p.getPreco()), // Formata o preço unitário como moeda.
                formatarMoeda(valorTotalLinha) // Formata o valor total da linha como moeda.
            });
        }
        
        // Atualiza o texto do JLabel com o valor total do estoque formatado.
        totalValueLabel.setText("Valor Total do Estoque: R$ " + formatarMoeda(currentValorTotalEstoque));
    }

    /**
     * Abre um diálogo de seleção de arquivo para o usuário escolher onde salvar o CSV.
     * Exporta os dados da tabela para um arquivo CSV, utilizando UTF-8 e ponto e vírgula como delimitador.
     */
    private void exportarParaCSV() {
        JFileChooser fileChooser = new JFileChooser(); // Cria um seletor de arquivos.
        fileChooser.setDialogTitle("Salvar como CSV"); // Define o título do diálogo.
        fileChooser.setSelectedFile(new File("estoque.csv")); // Sugere um nome de arquivo padrão.
        
        // Exibe o diálogo de salvar arquivo e verifica se o usuário aprovou a seleção.
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File arquivo = fileChooser.getSelectedFile(); // Obtém o arquivo selecionado pelo usuário.
            
            // Garante que a extensão do arquivo seja .csv
            if (!arquivo.getName().toLowerCase().endsWith(".csv")) {
                arquivo = new File(arquivo.getAbsolutePath() + ".csv");
            }

            try (FileOutputStream fos = new FileOutputStream(arquivo); // Abre um stream de saída de arquivo.
                 OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8); // Define a codificação UTF-8.
                 PrintWriter writer = new PrintWriter(osw)) { // Cria um escritor de texto.

                // Escreve o Byte Order Mark (BOM) para garantir que o CSV seja aberto corretamente em programas como Excel.
                fos.write(0xEF);
                fos.write(0xBB);
                fos.write(0xBF);

                // Escreve o cabeçalho do CSV, usando ';' como delimitador e aspas para envolver os nomes.
                writer.println("\"Produto\";\"Quantidade\";\"Preço Unitário\";\"Valor Total\"");
                
                // Itera sobre os produtos para escrever cada linha no CSV.
                for (Produto p : estoque.listarProdutos()) {
                    double valorTotal = p.getQuantidade() * p.getPreco(); // Calcula o valor total da linha.
                    
                    // Escreve a linha do produto, escapando aspas no nome do produto e formatando moedas.
                    writer.println(String.format("\"%s\";%d;%s;%s",
                        p.getNome().replace("\"", "\"\""), // Escapa aspas duplas dentro do nome do produto.
                        p.getQuantidade(),
                        formatarMoeda(p.getPreco()),
                        formatarMoeda(valorTotal)));
                }
                
                // Exibe uma mensagem de sucesso após a exportação.
                JOptionPane.showMessageDialog(this,
                    "Dados exportados com sucesso para:\n" + arquivo.getAbsolutePath(),
                    "Exportação Concluída",
                    JOptionPane.INFORMATION_MESSAGE);
                    
            } catch (Exception e) {
                // Exibe uma mensagem de erro se ocorrer uma exceção durante a exportação.
                JOptionPane.showMessageDialog(this,
                    "Erro ao exportar arquivo:\n" + e.getMessage(),
                    "Erro na Exportação",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Método auxiliar para formatar valores monetários para exibição e exportação.
     * Formata o valor com duas casas decimais, utilizando vírgula como separador decimal.
     * @param valor O valor numérico a ser formatado.
     * @return Uma string formatada como valor monetário (ex: "12,34").
     */
    private String formatarMoeda(double valor) {
        // Formata com 2 casas decimais e substitui o ponto decimal por vírgula.
        return String.format("%.2f", valor).replace(".", ",");
    }
}