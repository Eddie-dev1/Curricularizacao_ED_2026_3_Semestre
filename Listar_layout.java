/*EDUARDO ALVES DOS SANTOS
FERNANDO FREIRE DE OLIVEIRA
NICOLAS YUJI HIRATANI
VICTOR SOARES RIELO*/
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.Font;
import java.sql.ResultSet;

public class Listar_layout {

    private JFrame frame;
    private JTable tabela;

    BD bancoDeDados = new BD();

    public Listar_layout() {
        initialize(); 
        carregarTabela(); 
    }

    public void exibir() {
        if (frame != null) {
            frame.setVisible(true);
        } else {
             JOptionPane.showMessageDialog(null, "A interface não pôde ser inicializada.", "Erro Crítico", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void initialize() {

        frame = new JFrame();
        frame.setTitle("Lista de Gôndolas (Layout)");
        frame.setBounds(100, 100, 500, 500); 
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        // -------------------------------------
        // TÍTULO
        // -------------------------------------
        JLabel titulo = new JLabel("Gôndolas Cadastradas");
        titulo.setFont(new Font("Arial", Font.BOLD, 16));
        titulo.setBounds(150, 10, 200, 30);
        frame.getContentPane().add(titulo);

        // -------------------------------------
        // TABELA
        // -------------------------------------
        String[] colunas = {
            "Número Gôndola", // Coluna 1
            "Corredor",       // Coluna 2
            "Fileira"         // Coluna 3
        };

        DefaultTableModel modelo = new DefaultTableModel(colunas, 0);
        tabela = new JTable(modelo);

        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBounds(20, 60, 450, 350);
        frame.getContentPane().add(scroll);

        // -------------------------------------
        // BOTÃO ATUALIZAR LISTAGEM
        // -------------------------------------
        JButton atualizar = new JButton("Atualizar");
        atualizar.setBounds(190, 420, 120, 25);
        frame.getContentPane().add(atualizar);

        atualizar.addActionListener(e -> carregarTabela());
    }

    // -------------------------------------
    // MÉTODO PARA CARREGAR GÔNDOLAS
    // -------------------------------------
    private void carregarTabela() {
        try {
            bancoDeDados.conectar();

            if (!bancoDeDados.estaConectado()) {
                 JOptionPane.showMessageDialog(frame, "Falha ao carregar gôndolas: Erro de conexão com o BD.", "Erro BD", JOptionPane.ERROR_MESSAGE);
                 return;
            }

            ResultSet rs = bancoDeDados.listarGondolas(); 

            DefaultTableModel model = (DefaultTableModel) tabela.getModel();
            model.setRowCount(0);

            while (rs.next()) {
                Object[] linha = {
                    rs.getInt("Numero_gondola"),
                    rs.getInt("Corredor_gondola"),
                    rs.getInt("Fileira_gondola")
                };
                model.addRow(linha);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Erro ao carregar gôndolas: " + e.getMessage(), "Erro BD", JOptionPane.ERROR_MESSAGE);
        } finally {
            bancoDeDados.desconectar();
        }
    }
}