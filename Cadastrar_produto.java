/*EDUARDO ALVES DOS SANTOS
FERNANDO FREIRE DE OLIVEIRA
NICOLAS YUJI HIRATANI
VICTOR SOARES RIELO*/
import javax.swing.*;
import java.awt.Font;
import java.sql.ResultSet;

public class Cadastrar_produto {

    private JFrame frame;
    private JTextField textNome;
    private JTextField textTipo;
    private JTextField textMarca;
    private JTextField textVolume;
    private JTextField textPeso;
    private JTextField textPreco;
    private JTextField textQuantidade;
    private JTextField textPrateleira;

    private JComboBox<String> comboGondola;
    private JComboBox<String> comboUnidadeVolume;
    private JComboBox<String> comboUnidadePeso;

    BD bancoDeDados = new BD();

    public Cadastrar_produto() {
        initialize();
    }

    public void exibir() {
        frame.setVisible(true);
    }

    private void initialize() {
        frame = new JFrame();
        frame.setTitle("Gerenciador de Produtos");
        frame.setBounds(100, 100, 900, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        int yOffset = 40;
        
        // Coluna 1: Dados básicos
        JLabel lblNome = new JLabel("Nome:");
        lblNome.setBounds(40, yOffset, 150, 20);
        frame.getContentPane().add(lblNome);
        textNome = new JTextField();
        textNome.setBounds(160, yOffset, 200, 20);
        frame.getContentPane().add(textNome);
        yOffset += 40;

        JLabel lblTipo = new JLabel("Tipo:");
        lblTipo.setBounds(40, yOffset, 150, 20);
        frame.getContentPane().add(lblTipo);
        textTipo = new JTextField();
        textTipo.setBounds(160, yOffset, 200, 20);
        frame.getContentPane().add(textTipo);
        yOffset += 40;

        JLabel lblMarca = new JLabel("Marca:");
        lblMarca.setBounds(40, yOffset, 150, 20);
        frame.getContentPane().add(lblMarca);
        textMarca = new JTextField();
        textMarca.setBounds(160, yOffset, 200, 20);
        frame.getContentPane().add(textMarca);
        yOffset += 40;

        // VOLUME (e Seletor de Unidade) ---
        JLabel lblVolume = new JLabel("Volume:");
        lblVolume.setBounds(40, yOffset, 150, 20);
        frame.getContentPane().add(lblVolume);
        textVolume = new JTextField();
        textVolume.setBounds(160, yOffset, 120, 20);
        frame.getContentPane().add(textVolume);
        
        String[] unidadesVolume = {"ml", "L"};
        comboUnidadeVolume = new JComboBox<>(unidadesVolume);
        comboUnidadeVolume.setBounds(285, yOffset, 75, 20);
        frame.getContentPane().add(comboUnidadeVolume);
        yOffset += 40;

        // PESO (e Seletor de Unidade) ---
        JLabel lblPeso = new JLabel("Peso:");
        lblPeso.setBounds(40, yOffset, 150, 20);
        frame.getContentPane().add(lblPeso);
        textPeso = new JTextField();
        textPeso.setBounds(160, yOffset, 120, 20);
        frame.getContentPane().add(textPeso);
        
        String[] unidadesPeso = {"mg", "g", "kg"};
        comboUnidadePeso = new JComboBox<>(unidadesPeso);
        comboUnidadePeso.setBounds(285, yOffset, 75, 20);
        frame.getContentPane().add(comboUnidadePeso);
        yOffset += 40;
        
        // Coluna 2: Preço e Estoque
        yOffset = 40; 

        JLabel lblPreco = new JLabel("Preço (R$):");
        lblPreco.setBounds(510, yOffset, 150, 20);
        frame.getContentPane().add(lblPreco);
        textPreco = new JTextField();
        textPreco.setBounds(660, yOffset, 200, 20);
        frame.getContentPane().add(textPreco);
        yOffset += 40;

        JLabel lblQuantidade = new JLabel("Quantidade em Estoque:");
        lblQuantidade.setBounds(510, yOffset, 150, 20);
        frame.getContentPane().add(lblQuantidade);
        textQuantidade = new JTextField();
        textQuantidade.setBounds(660, yOffset, 200, 20);
        frame.getContentPane().add(textQuantidade);
        yOffset += 40;

        // Coluna 3: Localização
        JLabel lblLocalizacao = new JLabel("LOCALIZAÇÃO DO PRODUTO");
        lblLocalizacao.setFont(new Font("Arial", Font.BOLD, 14));
        lblLocalizacao.setBounds(510, yOffset, 350, 20);
        frame.getContentPane().add(lblLocalizacao);
        yOffset += 40;

        JLabel lblGondola = new JLabel("Número da Gôndola:");
        lblGondola.setBounds(510, yOffset, 150, 20);
        frame.getContentPane().add(lblGondola);
        comboGondola = new JComboBox<>();
        comboGondola.setBounds(660, yOffset, 200, 20);
        frame.getContentPane().add(comboGondola);
        carregarGondolas();
        yOffset += 40;

        JLabel lblPrateleira = new JLabel("Prateleira (ex: 1 a 5):");
        lblPrateleira.setBounds(510, yOffset, 150, 20);
        frame.getContentPane().add(lblPrateleira);
        textPrateleira = new JTextField();
        textPrateleira.setBounds(660, yOffset, 200, 20);
        frame.getContentPane().add(textPrateleira);
        yOffset += 40;

        JButton btnCriar = new JButton("Criar Produto");
        btnCriar.setBounds(350, 300, 200, 30);
        frame.getContentPane().add(btnCriar);
        
        // --------------------------------------
        // AÇÃO DO BOTÃO (COM NORMALIZAÇÃO)
        // --------------------------------------
        btnCriar.addActionListener(e -> {
            try {
                bancoDeDados.conectar();

                if (!bancoDeDados.estaConectado()) {
                    JOptionPane.showMessageDialog(frame, "Erro ao conectar ao banco!", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String nome = textNome.getText().trim();
                String tipo = textTipo.getText().trim();
                String marca = textMarca.getText().trim();

                if (nome.isEmpty() || tipo.isEmpty() || marca.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Nome, tipo e marca são obrigatórios!");
                    return;
                }

                double volumeEntrada = parseDouble(textVolume.getText(), "volume");
                String unidadeVolume = comboUnidadeVolume.getSelectedItem().toString();
                
                double pesoEntrada = parseDouble(textPeso.getText(), "peso");
                String unidadePeso = comboUnidadePeso.getSelectedItem().toString();

                double preco = parseDouble(textPreco.getText(), "preço");
                int quantidade = parseInt(textQuantidade.getText(), "quantidade");
                
                // CONVERSÃO/NORMALIZAÇÃO
                double volumeNormalizado = normalizarVolume(volumeEntrada, unidadeVolume);
                double pesoNormalizado = normalizarPeso(pesoEntrada, unidadePeso);
                
                if (comboGondola.getSelectedItem() == null) {
                    JOptionPane.showMessageDialog(frame, "Escolha um número de gôndola!");
                    return;
                }

                int gondola = Integer.parseInt(comboGondola.getSelectedItem().toString());
                int prateleira = parseInt(textPrateleira.getText(), "prateleira");

                // Salva os valores normalizados (em Litros e Kilogramas) no BD
                bancoDeDados.inserirProduto(
                    nome, tipo, marca,
                    volumeNormalizado, pesoNormalizado, preco,
                    gondola, quantidade, prateleira
                );

                JOptionPane.showMessageDialog(frame, "Produto inserido com sucesso!\n" +
                                               "Volume (salvo como Litros): " + volumeNormalizado +
                                               "\nPeso (salvo como Kilogramas): " + pesoNormalizado, 
                                               "Sucesso", JOptionPane.INFORMATION_MESSAGE);

                bancoDeDados.desconectar();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Erro: " + ex.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }

        });
    }

    // --------------------------------------
    // MÉTODOS DE NORMALIZAÇÃO
    // --------------------------------------

    /** Converte o volume para a unidade base Litros (L) */
    private double normalizarVolume(double valor, String unidade) {
        if (valor == 0) return 0;
        if (unidade.equals("L")) return valor; // Já está em Litros
        if (unidade.equals("ml")) return valor / 1000.0; // Converte ml para L
        return valor; // Retorno padrão
    }

    /** Converte o peso para a unidade base Kilogramas (kg) */
    private double normalizarPeso(double valor, String unidade) {
        if (valor == 0) return 0;
        if (unidade.equals("kg")) return valor; // Já está em Kilogramas
        if (unidade.equals("g")) return valor / 1000.0; // Converte g para kg
        if (unidade.equals("mg")) return valor / 1000000.0; // Converte mg para kg
        return valor; // Retorno padrão
    }


    private void carregarGondolas() {
        try {
            bancoDeDados.conectar();
            ResultSet rs = bancoDeDados.listarNumerosGondolas();

            comboGondola.removeAllItems();

            while (rs.next()) {
                comboGondola.addItem(String.valueOf(rs.getInt("Numero_gondola")));
            }

            bancoDeDados.desconectar();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Erro ao carregar gôndolas: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

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