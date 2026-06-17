/*EDUARDO ALVES DOS SANTOS
FERNANDO FREIRE DE OLIVEIRA
NICOLAS YUJI HIRATANI
VICTOR SOARES RIELO*/
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import java.awt.Font;
import java.sql.ResultSet;

public class Atualizar_produto {

    private JFrame frame;
    private JTextField textId;
    private JTextField textNome;
    private JTextField textTipo;
    private JTextField textMarca;
    private JTextField textVolume;
    private JTextField textPeso;
    private JTextField textPreco;
    private JTextField textQuantidade;
    private JComboBox<String> comboGondola;
    private JTextField textPrateleira;
    
    private JComboBox<String> comboUnidadeVolume;
    private JComboBox<String> comboUnidadePeso;

    BD bancoDeDados = new BD();

    public Atualizar_produto() {
        initialize();
    }

    public void exibir() {
        frame.setVisible(true);
    }

    private void initialize() {

        frame = new JFrame();
        frame.setTitle("Atualizar Produto");
        frame.setBounds(100, 100, 700, 650); 
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JLabel titulo = new JLabel("Atualização de Produto");
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        titulo.setBounds(180, 10, 300, 30);
        frame.getContentPane().add(titulo);

        int y = 60;
        Font fonteLabel = new Font("Arial", Font.PLAIN, 14);

        // --- ID ---
        JLabel lblId = new JLabel("ID do Produto:");
        lblId.setFont(fonteLabel);
        lblId.setBounds(60, y, 150, 25);
        frame.getContentPane().add(lblId);

        textId = new JTextField();
        textId.setBounds(220, y, 300, 25);
        frame.getContentPane().add(textId);

        // --- Campos de Dados ---
        y += 45;
        JLabel lblNome = new JLabel("Nome:");
        lblNome.setFont(fonteLabel);
        lblNome.setBounds(60, y, 150, 25);
        frame.getContentPane().add(lblNome);

        textNome = new JTextField();
        textNome.setBounds(220, y, 300, 25);
        frame.getContentPane().add(textNome);

        y += 45;
        JLabel lblTipo = new JLabel("Tipo:");
        lblTipo.setFont(fonteLabel);
        lblTipo.setBounds(60, y, 150, 25);
        frame.getContentPane().add(lblTipo);

        textTipo = new JTextField();
        textTipo.setBounds(220, y, 300, 25);
        frame.getContentPane().add(textTipo);

        y += 45;
        JLabel lblMarca = new JLabel("Marca:");
        lblMarca.setFont(fonteLabel);
        lblMarca.setBounds(60, y, 150, 25);
        frame.getContentPane().add(lblMarca);

        textMarca = new JTextField();
        textMarca.setBounds(220, y, 300, 25);
        frame.getContentPane().add(textMarca);

        // --- VOLUME + Unidade ---
        y += 45;
        JLabel lblVolume = new JLabel("Volume:");
        lblVolume.setFont(fonteLabel);
        lblVolume.setBounds(60, y, 150, 25);
        frame.getContentPane().add(lblVolume);

        textVolume = new JTextField();
        textVolume.setBounds(220, y, 150, 25); 
        frame.getContentPane().add(textVolume);
        
        String[] unidadesVolume = {"ml", "L"};
        comboUnidadeVolume = new JComboBox<>(unidadesVolume);
        comboUnidadeVolume.setBounds(375, y, 75, 25);
        frame.getContentPane().add(comboUnidadeVolume);

        // --- PESO + Unidade ---
        y += 45;
        JLabel lblPeso = new JLabel("Peso:");
        lblPeso.setFont(fonteLabel);
        lblPeso.setBounds(60, y, 150, 25);
        frame.getContentPane().add(lblPeso);

        textPeso = new JTextField();
        textPeso.setBounds(220, y, 150, 25); 
        frame.getContentPane().add(textPeso);
        
        String[] unidadesPeso = {"mg", "g", "kg"};
        comboUnidadePeso = new JComboBox<>(unidadesPeso);
        comboUnidadePeso.setBounds(375, y, 75, 25);
        frame.getContentPane().add(comboUnidadePeso);
        
        // --- Preço ---
        y += 45;
        JLabel lblPreco = new JLabel("Preço:");
        lblPreco.setFont(fonteLabel);
        lblPreco.setBounds(60, y, 150, 25);
        frame.getContentPane().add(lblPreco);

        textPreco = new JTextField();
        textPreco.setBounds(220, y, 300, 25);
        frame.getContentPane().add(textPreco);

        // --- Quantidade ---
        y += 45;
        JLabel lblQuantidade = new JLabel("Quantidade:");
        lblQuantidade.setFont(fonteLabel);
        lblQuantidade.setBounds(60, y, 150, 25);
        frame.getContentPane().add(lblQuantidade);

        textQuantidade = new JTextField();
        textQuantidade.setBounds(220, y, 300, 25);
        frame.getContentPane().add(textQuantidade);

        // --- Gôndola ---
        y += 45;
        JLabel lblGondola = new JLabel("Gôndola:");
        lblGondola.setFont(fonteLabel);
        lblGondola.setBounds(60, y, 150, 25);
        frame.getContentPane().add(lblGondola);

        comboGondola = new JComboBox<>();
        comboGondola.setBounds(220, y, 300, 25);
        frame.getContentPane().add(comboGondola);

        carregarGondolas();

        // --- Prateleira ---
        y += 45;
        JLabel lblPrateleira = new JLabel("Prateleira:");
        lblPrateleira.setFont(fonteLabel);
        lblPrateleira.setBounds(60, y, 150, 25);
        frame.getContentPane().add(lblPrateleira);

        textPrateleira = new JTextField();
        textPrateleira.setBounds(220, y, 300, 25);
        frame.getContentPane().add(textPrateleira);

        // Botão salvar
        JButton btnAtualizar = new JButton("Salvar Alterações");
        btnAtualizar.setFont(new Font("Arial", Font.BOLD, 14));
        btnAtualizar.setBounds(200, y + 60, 220, 35);
        frame.getContentPane().add(btnAtualizar);


        btnAtualizar.addActionListener(e -> {

            try {
                bancoDeDados.conectar();

                if (!bancoDeDados.estaConectado()) {
                    JOptionPane.showMessageDialog(frame, "Erro ao conectar ao banco!", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // ---------------------
                // Validação e Captura
                // ---------------------
                if (textId.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "O ID do produto é obrigatório!", "Atenção", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                int id = Integer.parseInt(textId.getText().trim());

                String nome = textNome.getText().trim();
                String tipo = textTipo.getText().trim();
                String marca = textMarca.getText().trim();

                if (nome.isEmpty() || tipo.isEmpty() || marca.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Nome, tipo e marca são obrigatórios!", "Atenção", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                
                double volumeEntrada = parseDouble(textVolume.getText(), "volume");
                String unidadeVolume = comboUnidadeVolume.getSelectedItem().toString();
                
                double pesoEntrada = parseDouble(textPeso.getText(), "peso");
                String unidadePeso = comboUnidadePeso.getSelectedItem().toString();
                
                
                double volumeNormalizado = normalizarVolume(volumeEntrada, unidadeVolume);
                double pesoNormalizado = normalizarPeso(pesoEntrada, unidadePeso);
                
                double preco = parseDouble(textPreco.getText(), "preço");
                int quantidade = parseInt(textQuantidade.getText(), "quantidade");
                int prateleira = parseInt(textPrateleira.getText(), "prateleira");

                if (comboGondola.getSelectedItem() == null) {
                    JOptionPane.showMessageDialog(frame, "Escolha uma gôndola!", "Atenção", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                int numeroGondola = Integer.parseInt(comboGondola.getSelectedItem().toString());
                

                bancoDeDados.editarProduto(
                    id, nome, tipo, marca, volumeNormalizado, pesoNormalizado, preco,
                    numeroGondola, quantidade, prateleira
                );

                JOptionPane.showMessageDialog(
                    frame, 
                    "Produto ID " + id + " atualizado com sucesso!\n" +
                    "Volume (salvo como Litros): " + volumeNormalizado + 
                    ", Peso (salvo como Kilogramas): " + pesoNormalizado,
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE
                );
                
                bancoDeDados.desconectar();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                    frame,
                    "Erro: " + ex.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
                );
            }

        });
    }

    // --------------------------------------
    // MÉTODOS DE NORMALIZAÇÃO
    // --------------------------------------

    /** Converte o volume para a unidade base Litros (L) */
    private double normalizarVolume(double valor, String unidade) {
        if (valor == 0) return 0;
        if (unidade.equals("L")) return valor; 
        if (unidade.equals("ml")) return valor / 1000.0; 
        return valor; 
    }

    /** Converte o peso para a unidade base Kilogramas (kg) */
    private double normalizarPeso(double valor, String unidade) {
        if (valor == 0) return 0;
        if (unidade.equals("kg")) return valor; 
        if (unidade.equals("g")) return valor / 1000.0; 
        if (unidade.equals("mg")) return valor / 1000000.0; 
        return valor; 
    }
    
    // ---------------------------
    // Carregar números das gôndolas
    // ---------------------------
    private void carregarGondolas() {
        try {
            bancoDeDados.conectar();
            ResultSet rs = bancoDeDados.listarNumerosGondolas();

            comboGondola.removeAllItems();

            while (rs.next()) {
                comboGondola.addItem(String.valueOf(rs.getInt("Numero_gondola")));
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Erro ao carregar gôndolas: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } finally {
            bancoDeDados.desconectar(); 
        }
    }

    // ---------------------------
    // Validadores
    // ---------------------------
    private double parseDouble(String valor, String nomeCampo) {
        if (valor.trim().isEmpty()) return 0;
        try { return Double.parseDouble(valor); }
        catch (Exception e) { throw new RuntimeException("Valor inválido no campo " + nomeCampo + ". Use números."); }
    }

    private int parseInt(String valor, String nomeCampo) {
        if (valor.trim().isEmpty()) return 0;
        try { return Integer.parseInt(valor); }
        catch (Exception e) { throw new RuntimeException("Valor inválido no campo " + nomeCampo + ". Use números inteiros."); }
    }
}