package Estoque;

import javax.swing.*;
import java.awt.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;

/**
 * A classe `Lista` representa a interface gráfica principal do sistema de gerenciamento de estoque.
 * Ela exibe uma tabela de produtos, permite filtragem e interação para adicionar, remover e atualizar produtos.
 * Implementa `Estoque.EstoqueListener` para ser notificada sobre alterações no estoque e atualizar a tabela.
 * @author Leandro
 */
public class Lista extends JFrame implements Estoque.EstoqueListener {
    private Estoque estoque; // Instância do gerenciador de estoque
    private DefaultTableModel modelo; // Modelo de dados da tabela
    private JTable tabela; // Componente visual da tabela

    private JTextField nomeFilter; // Campo de texto para filtrar por nome
    private JTextField quantFilter; // Campo de texto para filtrar por quantidade mínima

    /**
     * Construtor da classe Lista.
     * @param estoque A instância do objeto Estoque que gerencia os dados.
     */
    public Lista(Estoque estoque) {
        this.estoque = estoque;
        // Adiciona esta instância como listener para receber notificações de alterações no estoque.
        estoque.addListener((Estoque.EstoqueListener) this);
        initUI(); // Inicializa a interface do usuário
        atualizarTabela(); // Carrega os dados iniciais na tabela
    }

    /**
     * Método chamado quando o estoque é alterado.
     * Garante que a atualização da tabela ocorra na thread de despacho de eventos da Swing (EDT).
     */
    @Override
    public void estoqueAlterado() {
        SwingUtilities.invokeLater(this::atualizarTabela);
    }

    /**
     * Inicializa todos os componentes da interface do usuário (UI).
     */
    private void initUI() {
        setTitle("Sistema de Gerenciamento de Estoque"); // Define o título da janela
        // Impede o fechamento direto da janela para que a confirmação de saída possa ser exibida.
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        // Adiciona um WindowListener para tratar o evento de fechamento da janela.
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                confirmarSaida(); // Chama o método de confirmação ao tentar fechar.
            }
        });
        setLayout(new BorderLayout(10, 10)); // Define o layout principal da janela

        // Painel superior para botões de ação
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        topPanel.add(criarBotao("Adicionar", e -> Principal.adicionarProduto())); // Botão Adicionar
        topPanel.add(criarBotao("Remover Selecionado", e -> removerProdutoSelecionado())); // Botão Remover
        topPanel.add(criarBotao("Atualizar Selecionado", e -> atualizarProdutoSelecionado())); // Botão Atualizar
        topPanel.add(criarBotao("Relatório", e -> {
            // Abre a janela de relatório em uma nova thread da Swing.
            SwingUtilities.invokeLater(() -> new Relatorio(Lista.this, estoque).setVisible(true));
        }));

        // Painel para os campos de filtro
        JPanel filterPanel = criarFiltroPanel();

        // Combina os painéis de botões e filtros no painel superior da janela
        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.add(topPanel, BorderLayout.WEST);
        northPanel.add(filterPanel, BorderLayout.EAST);
        add(northPanel, BorderLayout.NORTH); // Adiciona o painel superior à janela

        // Cria e adiciona a tabela ao centro da janela
        JScrollPane scrollPane = criarTabela();
        add(scrollPane, BorderLayout.CENTER);

        pack(); // Ajusta o tamanho da janela para caber todos os componentes
        setLocationRelativeTo(null); // Centraliza a janela na tela
        setVisible(true); // Torna a janela visível
    }

    /**
     * Cria um botão com texto e ação especificados.
     * @param texto O texto a ser exibido no botão.
     * @param acao O ActionListener a ser executado quando o botão for clicado.
     * @return O JButton criado.
     */
    private JButton criarBotao(String texto, java.awt.event.ActionListener acao) {
        JButton botao = new JButton(texto);
        botao.setPreferredSize(new Dimension(120, 30)); // Define o tamanho preferencial do botão
        botao.addActionListener(acao); // Adiciona a ação ao botão
        return botao;
    }

    /**
     * Cria o painel contendo os campos de filtro de nome e quantidade.
     * @return O JPanel com os campos de filtro.
     */
    private JPanel criarFiltroPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); // Layout vertical

        nomeFilter = new JTextField(15); // Campo para filtrar por nome
        quantFilter = new JTextField(5); // Campo para filtrar por quantidade

        // Listener para os campos de filtro que atualiza a tabela quando o texto muda.
        DocumentListener listener = new DocumentListener() {
            @Override
            public void changedUpdate(DocumentEvent e) { atualizarTabela(); }
            @Override
            public void insertUpdate(DocumentEvent e) { atualizarTabela(); }
            @Override
            public void removeUpdate(DocumentEvent e) { atualizarTabela(); }
        };

        nomeFilter.getDocument().addDocumentListener(listener); // Adiciona o listener ao campo de nome
        quantFilter.getDocument().addDocumentListener(listener); // Adiciona o listener ao campo de quantidade

        panel.add(new JLabel("Filtrar por nome:"));
        panel.add(nomeFilter);
        panel.add(Box.createVerticalStrut(10)); // Espaçamento vertical
        panel.add(new JLabel("Quantidade mínima:"));
        panel.add(quantFilter);

        return panel;
    }

    /**
     * Cria e configura a JTable para exibir os produtos.
     * @return Um JScrollPane contendo a tabela.
     */
    private JScrollPane criarTabela() {
        String[] colunas = {"Nome", "Quantidade", "Preço"}; // Nomes das colunas da tabela
        modelo = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Impede a edição direta das células da tabela
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                // Define o tipo da coluna para permitir ordenação correta.
                if (columnIndex == 1) return Integer.class; // Coluna Quantidade é Integer
                if (columnIndex == 2) return Double.class;  // Coluna Preço é Double
                return String.class; // Outras colunas são String
            }
        };

        tabela = new JTable(modelo); // Cria a tabela com o modelo
        
        // Adiciona um sorter à tabela para permitir ordenação por coluna.
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modelo);
        tabela.setRowSorter(sorter);

        tabela.getTableHeader().setReorderingAllowed(false); // Impede a reordenação das colunas

        // Adiciona um MouseListener para detectar cliques duplos na tabela.
        tabela.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // Se for um clique duplo
                    atualizarProdutoSelecionado(); // Chama o método para atualizar o produto selecionado
                }
            }
        });

        return new JScrollPane(tabela); // Retorna a tabela dentro de um JScrollPane
    }

    /**
     * Atualiza os dados exibidos na tabela, aplicando os filtros de nome e quantidade.
     */
    private void atualizarTabela() {
        modelo.setRowCount(0); // Limpa todas as linhas existentes na tabela

        String nomeFiltro = nomeFilter.getText().trim().toLowerCase(); // Obtém o texto do filtro de nome
        String quantidadeTexto = quantFilter.getText().trim(); // Obtém o texto do filtro de quantidade
        int quantidadeMinima = 0;

        try {
            if (!quantidadeTexto.isEmpty()) {
                // Tenta converter o texto da quantidade mínima para um número inteiro.
                quantidadeMinima = Integer.parseInt(quantidadeTexto);
            }
        } catch (NumberFormatException e) {
            // Em caso de erro na conversão (texto não numérico), define a quantidade mínima como 0.
            quantidadeMinima = 0;
        }

        // Itera sobre a lista de produtos do estoque.
        for (Produto p : estoque.listarProdutos()) {
            // Aplica os filtros: nome começa com o filtro E quantidade é maior ou igual à mínima.
            if (p.getNome().toLowerCase().startsWith(nomeFiltro) &&
                p.getQuantidade() >= quantidadeMinima) {
                // Adiciona o produto à tabela se ele passar pelos filtros.
                modelo.addRow(new Object[]{
                    p.getNome(),
                    p.getQuantidade(),
                    p.getPreco()
                });
            }
        }
    }

    /**
     * Remove o produto selecionado na tabela.
     * Exibe uma mensagem de aviso se nenhum produto estiver selecionado.
     */
    private void removerProdutoSelecionado() {
        int selectedRow = tabela.getSelectedRow(); // Obtém a linha selecionada na tabela
        if (selectedRow == -1) {
            // Exibe um aviso se nenhuma linha estiver selecionada.
            JOptionPane.showMessageDialog(this, "Selecione um produto na tabela para remover.", "Nenhum Produto Selecionado", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Converte o índice da linha da visão (ordenada/filtrada) para o índice do modelo (real).
        int modelRow = tabela.convertRowIndexToModel(selectedRow);
        String nomeProduto = (String) modelo.getValueAt(modelRow, 0); // Obtém o nome do produto da linha selecionada

        Principal.removerProduto(nomeProduto); // Chama o método de remoção na classe Principal
    }

    /**
     * Abre um diálogo para atualizar os dados do produto selecionado na tabela.
     * Exibe uma mensagem de aviso se nenhum produto estiver selecionado.
     */
    private void atualizarProdutoSelecionado() {
        int selectedRow = tabela.getSelectedRow(); // Obtém a linha selecionada
        if (selectedRow == -1) {
            // Exibe um aviso se nenhuma linha estiver selecionada.
            JOptionPane.showMessageDialog(this, "Selecione um produto na tabela para atualizar.", "Nenhum Produto Selecionado", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Converte o índice da linha da visão para o índice do modelo.
        int modelRow = tabela.convertRowIndexToModel(selectedRow);
        String nomeProduto = (String) modelo.getValueAt(modelRow, 0);
        int quantidadeAtual = (Integer) modelo.getValueAt(modelRow, 1);
        double precoAtual = (Double) modelo.getValueAt(modelRow, 2);

        // Chama o diálogo de atualização na classe Principal, passando os dados do produto.
        Principal.mostrarDialogoAtualizarProduto(nomeProduto, quantidadeAtual, precoAtual);
    }

    /**
     * Exibe uma caixa de diálogo de confirmação antes de fechar o aplicativo.
     * Se o usuário confirmar, a conexão com o MongoDB é fechada e o aplicativo é encerrado.
     */
    private void confirmarSaida() {
        int resultado = JOptionPane.showConfirmDialog(
            this, // Componente pai para o diálogo
            "Tem certeza que deseja sair do aplicativo?", // Mensagem da confirmação
            "Confirmar Saída", // Título do diálogo
            JOptionPane.YES_NO_OPTION, // Opções de botões (Sim/Não)
            JOptionPane.QUESTION_MESSAGE // Tipo de ícone (Pergunta)
        );

        if (resultado == JOptionPane.YES_OPTION) {
            // Se o usuário clicar em "Sim", fecha a conexão com o MongoDB.
            MongoDBConnection.getInstance().close();
            dispose(); // Libera os recursos da janela
            System.exit(0); // Encerra o aplicativo Java
        }
    }

    /**
     * Sobrescreve o método dispose() para remover o listener do estoque
     * quando a janela é fechada, evitando vazamentos de memória.
     */
    @Override
    public void dispose() {
        estoque.removeListener(this); // Remove esta instância como listener do estoque.
        super.dispose(); // Chama o método dispose da superclasse.
    }
}