package Estoque;

import java.awt.*; 
import javax.swing.*; 
/**
 * Classe principal do sistema de gerenciamento de estoque.
 * Responsável por iniciar a aplicação e orquestrar a interação entre a interface
 * do usuário e a lógica de negócios do estoque.
 * @author Leandro
 */
public class Principal {
    // Instância única do gerenciador de estoque.
    private static final Estoque estoque = new Estoque();

    /**
     * Método principal que inicia a aplicação.
     * Cria e exibe a janela principal da lista de produtos na Thread de Despacho de Eventos (EDT).
     * @param args Argumentos da linha de comando (não utilizados).
     */
    public static void main(String[] args) {
        // Garante que a interface gráfica seja criada e manipulada na Event Dispatch Thread (EDT).
        SwingUtilities.invokeLater(() -> new Lista(estoque));
    }

    /**
     * Exibe uma caixa de diálogo de mensagem padronizada.
     * @param parent O componente pai para o diálogo (pode ser null para centralizar na tela).
     * @param mensagem A mensagem a ser exibida.
     * @param titulo O título da caixa de diálogo.
     * @param tipoMensagem O tipo de mensagem (ex: JOptionPane.INFORMATION_MESSAGE, JOptionPane.ERROR_MESSAGE).
     */
    private static void mostrarMensagem(Component parent, String mensagem, String titulo, int tipoMensagem) {
        JOptionPane.showMessageDialog(parent, mensagem, titulo, tipoMensagem);
    }

    /**
     * Exibe uma caixa de diálogo de mensagem de sucesso.
     * @param parent O componente pai.
     * @param mensagem A mensagem de sucesso.
     */
    private static void mostrarMensagemSucesso(Component parent, String mensagem) {
        mostrarMensagem(parent, mensagem, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Exibe uma caixa de diálogo de mensagem de erro.
     * @param parent O componente pai.
     * @param mensagem A mensagem de erro.
     */
    private static void mostrarMensagemErro(Component parent, String mensagem) {
        mostrarMensagem(parent, mensagem, "Erro", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Exibe uma caixa de diálogo de confirmação.
     * @param parent O componente pai.
     * @param mensagem A mensagem de confirmação.
     * @param titulo O título da caixa de diálogo.
     * @param tipoMensagem O tipo de mensagem (ex: JOptionPane.WARNING_MESSAGE, JOptionPane.QUESTION_MESSAGE).
     * @return true se o usuário confirmar (clicar em "Sim"), false caso contrário.
     */
    private static boolean confirmarAcao(Component parent, String mensagem, String titulo, int tipoMensagem) {
        return JOptionPane.showConfirmDialog(parent, mensagem, titulo, JOptionPane.YES_NO_OPTION, tipoMensagem) == JOptionPane.YES_OPTION;
    }

    /**
     * Cria um diálogo base para adicionar ou atualizar produtos, com configurações iniciais.
     * @param title O título do diálogo.
     * @param width A largura preferencial do diálogo.
     * @param height A altura preferencial do diálogo.
     * @return O JDialog configurado.
     */
    private static JDialog createBaseDialog(String title, int width, int height) {
        JDialog dialog = new JDialog((Frame) null, title, true); // Cria um diálogo modal sem um pai específico
        dialog.setLayout(new GridBagLayout()); // Define o layout do diálogo como GridBagLayout
        dialog.setPreferredSize(new Dimension(width, height)); // Define o tamanho preferencial
        return dialog;
    }

    /**
     * Exibe um diálogo para adicionar um novo produto ao estoque.
     */
    public static void adicionarProduto() {
        JDialog dialog = createBaseDialog("Adicionar Produto", 400, 250); // Cria o diálogo de adição
        GridBagConstraints gbc = new GridBagConstraints(); // Objeto para configurar o GridBagLayout
        gbc.insets = new Insets(5, 5, 5, 5); // Define as margens internas dos componentes
        gbc.fill = GridBagConstraints.HORIZONTAL; // Faz os componentes preencherem horizontalmente

        // Campos de entrada para nome, quantidade e preço
        JTextField nomeField = new JTextField(15);
        JTextField quantidadeField = new JTextField(15);
        JTextField precoField = new JTextField(15);
        
        JLabel erroLabel = new JLabel(" "); // Rótulo para exibir mensagens de erro
        erroLabel.setForeground(Color.RED); // Define a cor do texto do erro como vermelho

        JButton salvarBtn = new JButton("Salvar"); // Botão Salvar
        JButton cancelarBtn = new JButton("Cancelar"); // Botão Cancelar

        // Cria um validador de campos para o diálogo de adição.
        // O `validarExistenciaNome` é false, pois queremos garantir que o produto NÃO exista ao adicionar.
        ValidadorCampos validador = new ValidadorCampos(nomeField, quantidadeField, precoField, erroLabel, salvarBtn, estoque, false);
        validador.inicializarListeners(); // Inicializa os listeners de validação

        // Adiciona um ActionListener ao botão Salvar.
        salvarBtn.addActionListener(e -> {
            try {
                String nome = nomeField.getText().trim();
                int quantidade = Integer.parseInt(quantidadeField.getText());
                // Substitui vírgula por ponto para parsear double corretamente (formato brasileiro para americano).
                double preco = Double.parseDouble(precoField.getText().replace(",", "."));

                // Pede confirmação ao usuário antes de adicionar o produto.
                if (confirmarAcao(dialog,
                    String.format("Deseja realmente adicionar o produto '%s' com quantidade %d e preço R$ %.2f?", nome, quantidade, preco),
                    "Confirmar Adição", JOptionPane.QUESTION_MESSAGE)) {
                    // Tenta adicionar o produto ao estoque.
                    if (estoque.adicionarProduto(new Produto(nome, quantidade, preco))) {
                        mostrarMensagemSucesso(dialog, "Produto adicionado com sucesso.");
                        dialog.dispose(); // Fecha o diálogo se a adição for bem-sucedida.
                    }
                }
            } catch (NumberFormatException ex) {
                // Exibe erro se houver problema na conversão de número.
                erroLabel.setText("<html>Erro ao processar dados. Verifique o formato.</html>");
            }
        });

        // Adiciona um ActionListener ao botão Cancelar para fechar o diálogo.
        cancelarBtn.addActionListener(e -> dialog.dispose());

        // Configuração e adição dos componentes ao GridBagLayout do diálogo.
        gbc.gridx = 0; gbc.gridy = 0; dialog.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.gridwidth = GridBagConstraints.REMAINDER; dialog.add(nomeField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1; gbc.weightx = 0.0; dialog.add(new JLabel("Quantidade:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.gridwidth = GridBagConstraints.REMAINDER; dialog.add(quantidadeField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1; gbc.weightx = 0.0; dialog.add(new JLabel("Preço:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.gridwidth = GridBagConstraints.REMAINDER; dialog.add(precoField, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = GridBagConstraints.REMAINDER; gbc.weighty = 0.1; dialog.add(erroLabel, gbc);

        gbc.gridy = 4; gbc.gridwidth = 1; gbc.weightx = 0.5; gbc.weighty = 0.0; gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.EAST; dialog.add(salvarBtn, gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST; dialog.add(cancelarBtn, gbc);

        dialog.pack(); // Ajusta o tamanho do diálogo aos componentes
        dialog.setLocationRelativeTo(null); // Centraliza o diálogo na tela
        dialog.setVisible(true); // Torna o diálogo visível
    }

    /**
     * Remove um produto do estoque após confirmação.
     * @param nome O nome do produto a ser removido.
     */
    public static void removerProduto(String nome) {
        // Pede confirmação ao usuário antes de remover o produto.
        if (confirmarAcao(null, "Deseja realmente remover o produto " + nome + "?", "Confirmar Remoção", JOptionPane.WARNING_MESSAGE)) {
            boolean removido = estoque.removerProduto(nome); // Tenta remover o produto.
            if (removido) mostrarMensagemSucesso(null, "Produto removido com sucesso.");
            else mostrarMensagemErro(null, "Produto não encontrado.");
        }
    }

    /**
     * Exibe um diálogo para atualizar a quantidade e o preço de um produto existente.
     * O nome do produto é exibido, mas não pode ser editado.
     * @param nome O nome do produto a ser atualizado.
     * @param quantidadeAtual A quantidade atual do produto.
     * @param precoAtual O preço atual do produto.
     */
    public static void mostrarDialogoAtualizarProduto(String nome, int quantidadeAtual, double precoAtual) {
        JDialog dialog = createBaseDialog("Atualizar Produto", 400, 250); // Cria o diálogo de atualização
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField nomeField = new JTextField(nome, 15); // Campo para o nome do produto (não editável)
        nomeField.setEditable(false);

        JTextField quantidadeField = new JTextField(String.valueOf(quantidadeAtual), 15); // Campo para a nova quantidade
        // Campo para o novo preço, formatado para exibir vírgula como separador decimal.
        JTextField precoField = new JTextField(String.valueOf(precoAtual).replace(".", ","), 15);

        JLabel erroLabel = new JLabel(" ");
        erroLabel.setForeground(Color.RED);

        JButton salvarBtn = new JButton("Salvar");
        JButton cancelarBtn = new JButton("Cancelar");

        // Cria um validador de campos para o diálogo de atualização.
        // `nomeField` é null, pois não é validado aqui, e `validarExistenciaNome` é false
        // porque a validação de existência do nome já é dada, estamos apenas atualizando.
        ValidadorCampos validador = new ValidadorCampos(null, quantidadeField, precoField, erroLabel, salvarBtn, estoque, false);
        validador.inicializarListeners();

        // Adiciona um ActionListener ao botão Salvar.
        salvarBtn.addActionListener(e -> {
            try {
                int quantidade = Integer.parseInt(quantidadeField.getText());
                // Substitui vírgula por ponto para parsear double corretamente.
                double preco = Double.parseDouble(precoField.getText().replace(",", "."));

                // Pede confirmação ao usuário antes de atualizar.
                if (confirmarAcao(dialog,
                    String.format("Deseja realmente atualizar o produto %s com quantidade %d e preço R$ %.2f?", nome, quantidade, preco),
                    "Confirmar Atualização", JOptionPane.WARNING_MESSAGE)) {
                    boolean atualizado = estoque.atualizarProduto(nome, quantidade, preco); // Tenta atualizar o produto.
                    if (atualizado) {
                        mostrarMensagemSucesso(dialog, "Produto atualizado com sucesso.");
                        dialog.dispose(); // Fecha o diálogo se a atualização for bem-sucedida.
                    } else {
                        mostrarMensagemErro(dialog, "Erro ao atualizar o produto.");
                    }
                }
            } catch (NumberFormatException ex) {
                erroLabel.setText("<html>Erro ao processar dados. Verifique o formato.</html>");
            }
        });

        // Adiciona um ActionListener ao botão Cancelar para fechar o diálogo.
        cancelarBtn.addActionListener(e -> dialog.dispose());

        // Configuração e adição dos componentes ao GridBagLayout do diálogo.
        gbc.gridx = 0; gbc.gridy = 0; dialog.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.gridwidth = GridBagConstraints.REMAINDER; dialog.add(nomeField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1; gbc.weightx = 0.0; dialog.add(new JLabel("Nova quantidade:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.gridwidth = GridBagConstraints.REMAINDER; dialog.add(quantidadeField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1; gbc.weightx = 0.0; dialog.add(new JLabel("Novo preço:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.gridwidth = GridBagConstraints.REMAINDER; dialog.add(precoField, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = GridBagConstraints.REMAINDER; gbc.weighty = 0.1; dialog.add(erroLabel, gbc);

        gbc.gridy = 4; gbc.gridwidth = 1; gbc.weightx = 0.5; gbc.weighty = 0.0; gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.EAST; dialog.add(salvarBtn, gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST; dialog.add(cancelarBtn, gbc);

        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }
}
