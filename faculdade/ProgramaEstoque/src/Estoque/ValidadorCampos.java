package Estoque;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.regex.Pattern;
import javax.swing.border.LineBorder;
import javax.swing.border.Border;

/**
 * Classe responsável por validar campos de texto em formulários Swing,
 * fornecendo feedback visual (bordas e mensagens de erro) e controlando a
 * habilitação de um botão de confirmação.
 * Implementa DocumentListener para validação em tempo real da digitação e FocusListener
 * para validação ao ganhar/perder o foco.
 * @author Leandro
 */
public class ValidadorCampos implements DocumentListener, java.awt.event.FocusListener {
    private final JTextField nomeField; 
    private final JTextField quantidadeField; 
    private final JTextField precoField;
    private final JLabel erroLabel; 
    private final JButton botaoConfirmar; 
    private final Estoque estoque; 
    private final boolean validarExistenciaNome; 

    // Padrões de expressões regulares para validação numérica
    private static final Pattern NUMERIC_INTEGER_PATTERN = Pattern.compile("^\\d+$"); // Apenas dígitos para inteiros
    private static final Pattern NUMERIC_DECIMAL_PATTERN = Pattern.compile("^\\d+([.,]\\d{1,2})?$"); // Dígitos, opcionalmente com vírgula ou ponto e até 2 casas decimais

    private final Border defaultBorder; // Borda padrão dos campos de texto
    private static final Border ERROR_BORDER = new LineBorder(Color.RED, 1); // Borda de erro (vermelha)

    /* Flags para rastrear o estado lógico de validade de cada campo.
     *true se o campo contém um erro lógico (ex: nome vazio, quantidade negativa).
     */
    private boolean nomeFieldLogicamenteInvalido = false;
    private boolean quantidadeFieldLogicamenteInvalido = false;
    private boolean precoFieldLogicamenteInvalido = false;

    /* Flags para controlar se a borda vermelha e a mensagem de erro individual devem ser mostradas para cada campo.
     *Ativado quando o campo perde o foco ou quando há uma validação manual (no caso de um botão "salvar").
     */
    private boolean nomeFieldErroVisualAtivo = false;
    private boolean quantidadeFieldErroVisualAtivo = false;
    private boolean precoFieldErroVisualAtivo = false;
    
    /**
     * Construtor do ValidadorCampos.
     * @param nomeField O JTextField para o nome do produto. Pode ser null se não for aplicável.
     * @param quantidadeField O JTextField para a quantidade. Pode ser null se não for aplicável.
     * @param precoField O JTextField para o preço. Pode ser null se não for aplicável.
     * @param erroLabel O JLabel onde as mensagens de erro serão exibidas.
     * @param botaoConfirmar O JButton que será habilitado/desabilitado.
     * @param estoque A instância do Estoque para realizar buscas (verificar existência/inexistência de produto).
     * @param validarExistenciaNome Se true, o nome deve existir no estoque; se false, o nome NÃO deve existir.
     */
    public ValidadorCampos(JTextField nomeField, JTextField quantidadeField, JTextField precoField,
                           JLabel erroLabel, JButton botaoConfirmar, Estoque estoque, boolean validarExistenciaNome) {
        this.nomeField = nomeField;
        this.quantidadeField = quantidadeField;
        this.precoField = precoField;
        this.erroLabel = erroLabel;
        this.botaoConfirmar = botaoConfirmar;
        this.estoque = estoque;
        this.validarExistenciaNome = validarExistenciaNome;

        // Armazena a borda padrão de um dos campos (se existirem) para restaurá-la depois.
        if (nomeField != null) this.defaultBorder = nomeField.getBorder();
        else if (quantidadeField != null) this.defaultBorder = quantidadeField.getBorder();
        else if (precoField != null) this.defaultBorder = precoField.getBorder();
        else this.defaultBorder = UIManager.getBorder("TextField.border"); // Borda padrão do Swing
    }

    /**
     * Inicializa os listeners para os campos de texto.
     * Adiciona `DocumentListener` para validação em tempo real da digitação
     * e `FocusListener` para validação ao ganhar/perder o foco.
     * Realiza uma validação inicial para definir o estado do botão "Confirmar".
     */
    public void inicializarListeners() {
        if (nomeField != null) {
            nomeField.getDocument().addDocumentListener(this);
            nomeField.addFocusListener(this);
        }
        if (quantidadeField != null) {
            quantidadeField.getDocument().addDocumentListener(this);
            quantidadeField.addFocusListener(this);
        }
        if (precoField != null) {
            precoField.getDocument().addDocumentListener(this);
            precoField.addFocusListener(this);
        }
        // Validação inicial para desabilitar o botão se os campos estiverem inválidos,
        // sem mostrar erros visuais imediatamente.
        validarCamposInterno(null);
    }

    /**
     * Chamado quando texto é inserido no documento.
     * @param e O evento do documento.
     */
    @Override
    public void insertUpdate(DocumentEvent e) {
        JTextField sourceField = null;
        // Identifica qual campo disparou o evento.
        if (e.getDocument() == (nomeField != null ? nomeField.getDocument() : null)) {
            sourceField = nomeField;
        } else if (e.getDocument() == (quantidadeField != null ? quantidadeField.getDocument() : null)) {
            sourceField = quantidadeField;
        } else if (e.getDocument() == (precoField != null ? precoField.getDocument() : null)) {
            sourceField = precoField;
        }
        // Reseta a flag de erro visual para o campo ao digitar, se ele não estiver focado.
        // Isso permite que a borda vermelha seja removida assim que o usuário começa a corrigir o erro.
        if (sourceField != null && !sourceField.hasFocus()) {
            if (sourceField == nomeField) nomeFieldErroVisualAtivo = false;
            else if (sourceField == quantidadeField) quantidadeFieldErroVisualAtivo = false;
            else if (sourceField == precoField) precoFieldErroVisualAtivo = false;
        }
        // Realiza a validação interna para atualizar o estado do botão e bordas.
        validarCamposInterno(sourceField);
    }

    /**
     * Chamado quando texto é removido do documento. Delega para `insertUpdate`.
     * @param e O evento do documento.
     */
    @Override
    public void removeUpdate(DocumentEvent e) { insertUpdate(e); }

    /**
     * Chamado quando um atributo do documento é alterado (raro para JTextFields). Delega para `insertUpdate`.
     * @param e O evento do documento.
     */
    @Override
    public void changedUpdate(DocumentEvent e) { insertUpdate(e); }

    /**
     * Chamado quando um componente ganha o foco.
     * Limpa a borda vermelha e a mensagem de erro para o campo focado.
     * @param e O evento de foco.
     */
    @Override
    public void focusGained(java.awt.event.FocusEvent e) {
        JTextField sourceField = (JTextField) e.getSource();
        sourceField.setBorder(defaultBorder); // Limpa a borda vermelha ao ganhar foco
        
        // Reseta a flag de erro visual para este campo ao ganhar foco.
        if (sourceField == nomeField) nomeFieldErroVisualAtivo = false;
        else if (sourceField == quantidadeField) quantidadeFieldErroVisualAtivo = false;
        else if (sourceField == precoField) precoFieldErroVisualAtivo = false;
        
        erroLabel.setText(" "); // Limpa a mensagem de erro global.
        validarCamposInterno(sourceField); // Revalida e mostra o erro do campo focado imediatamente.
    }

    /**
     * Chamado quando um componente perde o foco.
     * Ativa a exibição de erros visuais para o campo que perdeu o foco e revalida.
     * @param e O evento de foco.
     */
    @Override
    public void focusLost(java.awt.event.FocusEvent e) {
        JTextField sourceField = (JTextField) e.getSource();
        
        // Ativa a exibição de erros para este campo ao perder o foco.
        if (sourceField == nomeField) nomeFieldErroVisualAtivo = true;
        else if (sourceField == quantidadeField) quantidadeFieldErroVisualAtivo = true;
        else if (sourceField == precoField) precoFieldErroVisualAtivo = true;
        
        validarCamposInterno(sourceField); // Valida e mostra o erro do campo que perdeu o foco.
    }

    /**
     * Valida todos os campos gerenciados por este validador e atualiza o estado do botão de confirmação.
     * Controla a exibição de bordas vermelhas nos campos e mensagens de erro no `erroLabel`.
     * A borda vermelha e a mensagem de erro de um campo individual só aparecem se o erro visual para aquele campo
     * foi ativado (geralmente ao perder o foco ou em uma validação acionada pelo botão de salvar).
     *
     * @param fieldToValidate O JTextField que disparou o evento (focado, perdendo foco, digitando), ou `null` para uma validação global (ex: na inicialização ou ao clicar no botão "Salvar").
     */
    private void validarCamposInterno(JTextField fieldToValidate) {
        boolean todosValidos = true;
        
        // --- Validação lógica de cada campo e atualização das flags _LogicamenteInvalido ---
        // Verifica se cada campo tem um erro lógico (independente da exibição visual).
        nomeFieldLogicamenteInvalido = (getNomeErro(nomeField) != null);
        quantidadeFieldLogicamenteInvalido = (getQuantidadeErro(quantidadeField) != null);
        precoFieldLogicamenteInvalido = (getPrecoErro(precoField) != null);

        // Se qualquer campo for logicamente inválido, o formulário como um todo não é válido.
        if (nomeFieldLogicamenteInvalido || quantidadeFieldLogicamenteInvalido || precoFieldLogicamenteInvalido) {
            todosValidos = false;
        }

        // --- Aplicação de Bordas Vermelhas ---
        // A borda só fica vermelha se o campo é logicamente inválido E o erro visual foi ativado para ele.
        aplicarBorda(nomeField, nomeFieldLogicamenteInvalido && nomeFieldErroVisualAtivo);
        aplicarBorda(quantidadeField, quantidadeFieldLogicamenteInvalido && quantidadeFieldErroVisualAtivo);
        aplicarBorda(precoField, precoFieldLogicamenteInvalido && precoFieldErroVisualAtivo);

        // --- Atualização do erroLabel (mensagem de erro global) ---
        String erroMensagemParaLabel = null;
        // Prioriza a exibição do erro do campo que está sendo ativamente validado (digitando ou perdendo foco).
        if (fieldToValidate != null) {
            if (fieldToValidate == nomeField) erroMensagemParaLabel = getNomeErro(nomeField);
            else if (fieldToValidate == quantidadeField) erroMensagemParaLabel = getQuantidadeErro(quantidadeField);
            else if (fieldToValidate == precoField) erroMensagemParaLabel = getPrecoErro(precoField);
        }
        
        if (erroMensagemParaLabel != null) {
             // Exibe a mensagem de erro específica do campo.
             erroLabel.setText("<html>" + erroMensagemParaLabel + "</html>");
        } else if (!todosValidos) {
             // Se não há um erro de campo focado, mas o formulário geral não é válido (outros campos com erro visual ativo),
             // exibe uma mensagem de erro genérica.
             erroLabel.setText("<html>Existem campos inválidos. Por favor, verifique.</html>");
        }
        else {
             // Se todos os campos estão válidos ou não há erros visuais ativos, limpa o label.
             erroLabel.setText(" ");
        }
        
        // --- Atualização do botão Confirmar ---
        // O botão é habilitado apenas se todos os campos estiverem logicamente válidos.
        botaoConfirmar.setEnabled(todosValidos);
    }

    /**
     * Retorna a mensagem de erro para o campo de nome, se houver.
     * @param field O JTextField do nome.
     * @return A mensagem de erro ou null se o campo for válido.
     */
    private String getNomeErro(JTextField field) {
        if (field == null) return null; // Se o campo não existe, não há erro
        String nome = field.getText().trim();
        if (nome.isEmpty()) {
            return "Nome não pode ser vazio.";
        }
        // Valida a existência/inexistência do produto no estoque, dependendo da flag `validarExistenciaNome`.
        if (validarExistenciaNome && estoque.buscarProduto(nome) == null) {
            return "Produto não encontrado."; // Para atualização, o produto deve existir.
        }
        if (!validarExistenciaNome && estoque.buscarProduto(nome) != null) {
            return "Produto já existe."; // Para adição, o produto não deve existir.
        }
        return null; // Campo válido
    }

    /**
     * Retorna a mensagem de erro para o campo de quantidade, se houver.
     * @param field O JTextField da quantidade.
     * @return A mensagem de erro ou null se o campo for válido.
     */
    private String getQuantidadeErro(JTextField field) {
        if (field == null) return null;
        String quantidade = field.getText().trim();
        if (quantidade.isEmpty()) {
            return "Quantidade não pode ser vazia.";
        }
        // Verifica se a quantidade corresponde ao padrão de número inteiro.
        if (!NUMERIC_INTEGER_PATTERN.matcher(quantidade).matches()) {
            return "Quantidade deve ser um número inteiro.";
        }
        try {
            // Tenta converter para inteiro e verifica se é não negativo.
            if (Integer.parseInt(quantidade) < 0) {
                return "Quantidade não pode ser negativa.";
            }
        } catch (NumberFormatException ex) {
            return "Quantidade inválida."; // Caso raro de erro de parsing após regex
        }
        return null; // Campo válido
    }

    /**
     * Retorna a mensagem de erro para o campo de preço, se houver.
     * @param field O JTextField do preço.
     * @return A mensagem de erro ou null se o campo for válido.
     */
    private String getPrecoErro(JTextField field) {
        if (field == null) return null;
        String preco = field.getText().trim();
        if (preco.isEmpty()) {
            return "Preço não pode ser vazio.";
        }
        // Verifica se o preço corresponde ao padrão de número decimal (com ponto ou vírgula e até 2 casas).
        if (!NUMERIC_DECIMAL_PATTERN.matcher(preco).matches()) {
            return "Formato de preço inválido. Use 99 ou 99,99.";
        }
        try {
            // Converte vírgula para ponto antes de parsear para double.
            double valorPreco = Double.parseDouble(preco.replace(",", "."));
            // Verifica se o preço é não negativo.
            if (valorPreco < 0) {
                return "Preço não pode ser negativo.";
            }
        } catch (NumberFormatException ex) {
            return "Preço inválido."; // Caso raro de erro de parsing após regex
        }
        return null; // Campo válido
    }

    /**
     * Aplica a borda de erro (vermelha) ou a borda padrão a um JTextField.
     * @param field O JTextField ao qual a borda será aplicada.
     * @param isInvalid true para aplicar a borda de erro, false para aplicar a borda padrão.
     */
    private void aplicarBorda(JTextField field, boolean isInvalid) {
        if (field != null) {
            field.setBorder(isInvalid ? ERROR_BORDER : defaultBorder);
        }
    }
}