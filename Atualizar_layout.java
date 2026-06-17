import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import java.awt.Font;
import java.sql.ResultSet;
import java.util.Vector;

public class Atualizar_layout {

    private JFrame frame;
    private JComboBox<Integer> comboNumeroGondola; 
    private JTextField textCorredor;
    private JTextField textFileira;
    
    BD bancoDeDados = new BD();

    public Atualizar_layout() {
        initialize();
    }

    public void exibir() {
        frame.setVisible(true);
    }

    private void initialize() {
        frame = new JFrame();
        frame.setTitle("Atualizar Gôndola");
        frame.setBounds(100, 100, 550, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        // -----------------------------
        // TÍTULO
        // -----------------------------
        JLabel titulo = new JLabel("Atualização de Gôndola");
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        titulo.setBounds(160, 20, 300, 30);
        frame.getContentPane().add(titulo);

        int y = 80;
        Font fonteLabel = new Font("Arial", Font.PLAIN, 14);

        // -----------------------------
        // CAMPO: Numero_gondola (ID/PK - 
        // -----------------------------
        JLabel lblNumero = new JLabel("Número da Gôndola (ID):");
        lblNumero.setFont(fonteLabel);
        lblNumero.setBounds(60, y, 180, 25);
        frame.getContentPane().add(lblNumero);

        comboNumeroGondola = new JComboBox<>();
        comboNumeroGondola.setBounds(250, y, 200, 25);
        frame.getContentPane().add(comboNumeroGondola);
        
        carregarNumerosGondolas();

        y += 45;

        // -----------------------------
        // CAMPO: Corredor_gondola
        // -----------------------------
        JLabel lblCorredor = new JLabel("Corredor:");
        lblCorredor.setFont(fonteLabel);
        lblCorredor.setBounds(60, y, 180, 25);
        frame.getContentPane().add(lblCorredor);

        textCorredor = new JTextField();
        textCorredor.setBounds(250, y, 200, 25);
        frame.getContentPane().add(textCorredor);

        y += 45;

        // -----------------------------
        // CAMPO: Fileira_gondola
        // -----------------------------
        JLabel lblFileira = new JLabel("Fileira:");
        lblFileira.setFont(fonteLabel);
        lblFileira.setBounds(60, y, 180, 25);
        frame.getContentPane().add(lblFileira);

        textFileira = new JTextField();
        textFileira.setBounds(250, y, 200, 25);
        frame.getContentPane().add(textFileira);

        // -----------------------------
        // BOTÕES
        // -----------------------------
        JButton btnAtualizar = new JButton("Salvar Alterações");
        btnAtualizar.setFont(new Font("Arial", Font.BOLD, 14));
        btnAtualizar.setBounds(290, y + 60, 160, 35);
        frame.getContentPane().add(btnAtualizar);

        JButton btnVoltar = new JButton("Voltar");
        btnVoltar.setFont(new Font("Arial", Font.PLAIN, 14));
        btnVoltar.setBounds(60, y + 60, 150, 35);
        frame.getContentPane().add(btnVoltar);
        
        // -----------------------------
        // AÇÃO DO BOTÃO VOLTAR
        // -----------------------------
        btnVoltar.addActionListener(e -> {
            frame.dispose(); 
        });

        // -----------------------------
        // AÇÃO DO BOTÃO ATUALIZAR
        // -----------------------------
        btnAtualizar.addActionListener(e -> {
            try {
                Integer numeroGondolaObj = (Integer) comboNumeroGondola.getSelectedItem();
                
                if (numeroGondolaObj == null) {
                    JOptionPane.showMessageDialog(null, "Selecione um Número de Gôndola.", 
                        "Erro", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                int numeroGondola = numeroGondolaObj.intValue();

                bancoDeDados.conectar();

                if (!bancoDeDados.estaConectado()) {
                    JOptionPane.showMessageDialog(null, "Erro ao conectar ao banco de dados.", 
                        "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
               
                String corredorStr = textCorredor.getText().trim();
                String fileiraStr = textFileira.getText().trim();

                if (corredorStr.isEmpty() || fileiraStr.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Corredor e Fileira não podem ser vazios.", 
                        "Erro", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                int corredor = Integer.parseInt(corredorStr);
                int fileira = Integer.parseInt(fileiraStr);

               
                bancoDeDados.editarGondola(numeroGondola, corredor, fileira);

                JOptionPane.showMessageDialog(null, "Gôndola atualizada com sucesso!");
                bancoDeDados.desconectar();
                
               
                textCorredor.setText("");
                textFileira.setText("");

            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(null, "Erro de formato: Certifique-se de que Corredor e Fileira são números inteiros válidos.",
                        "Erro de Entrada", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                        null,
                        "Erro ao atualizar Gôndola: " + ex.getMessage(),
                        "Erro",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });
    }
    
    private void carregarNumerosGondolas() {
        try {
            bancoDeDados.conectar();
            if (!bancoDeDados.estaConectado()) {
                JOptionPane.showMessageDialog(frame, "Falha ao carregar gôndolas: Erro de conexão.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

         
            ResultSet rs = bancoDeDados.listarNumerosGondolas();
            Vector<Integer> gondolaList = new Vector<>();
            
            while (rs.next()) {
                gondolaList.add(rs.getInt("Numero_gondola"));
            }
            
         
            comboNumeroGondola.setModel(new DefaultComboBoxModel<>(gondolaList));

            bancoDeDados.desconectar();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Erro ao carregar números de gôndolas: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
