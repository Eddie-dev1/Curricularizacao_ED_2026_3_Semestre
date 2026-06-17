/*EDUARDO ALVES DOS SANTOS
FERNANDO FREIRE DE OLIVEIRA
NICOLAS YUJI HIRATANI
VICTOR SOARES RIELO*/
import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;

public class localizar_produto {

    private JFrame frame;
    private JTextField txtIdProduto;
    private JLabel lblPosicaoValor;
    private BD bancoDeDados = new BD();

    public localizar_produto() {
        initialize();
    }

    public void exibir() {
        frame.setVisible(true);
    }

    private void initialize() {
        frame = new JFrame();
        frame.setTitle("Buscar Posição do Produto");
        frame.setBounds(100, 100, 450, 250);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
        frame.getContentPane().setLayout(null);

        JLabel titulo = new JLabel("ONDE ESTÁ O PRODUTO?");
        titulo.setFont(new Font("Arial", Font.BOLD, 16));
        titulo.setBounds(100, 15, 300, 25);
        frame.getContentPane().add(titulo);

        // Campo ID do Produto
        JLabel lblId = new JLabel("ID do Produto:");
        lblId.setBounds(50, 60, 100, 25);
        frame.getContentPane().add(lblId);

        txtIdProduto = new JTextField();
        txtIdProduto.setBounds(150, 60, 150, 25);
        frame.getContentPane().add(txtIdProduto);

        JButton btnBuscar = new JButton("Buscar Posição");
        btnBuscar.setBounds(310, 60, 120, 25);
        frame.getContentPane().add(btnBuscar);

        // Posição (Resultado)
        JLabel lblPosicao = new JLabel("Posição:");
        lblPosicao.setFont(new Font("Arial", Font.BOLD, 14));
        lblPosicao.setBounds(50, 120, 100, 25);
        frame.getContentPane().add(lblPosicao);

        lblPosicaoValor = new JLabel("Insira o ID para buscar...");
        lblPosicaoValor.setFont(new Font("Arial", Font.PLAIN, 12));
        lblPosicaoValor.setBounds(50, 150, 380, 50);
        frame.getContentPane().add(lblPosicaoValor);

        btnBuscar.addActionListener(e -> buscarPosicao());
    }

    private void buscarPosicao() {
        lblPosicaoValor.setText("Buscando...");
        String idStr = txtIdProduto.getText().trim();

        if (idStr.isEmpty()) {
            lblPosicaoValor.setText("ID inválido. Por favor, insira um número.");
            return;
        }

        try {
            int id = Integer.parseInt(idStr);
            bancoDeDados.conectar();
            ResultSet rs = bancoDeDados.buscarLocalizacaoProduto(id); 

            if (rs != null && rs.next()) {
                
                String nome = rs.getString("Nome_prod");
                int gondola = rs.getInt("Numero_gondola");
                int prateleira = rs.getInt("Prateleira_gondola");
                int corredor = rs.getInt("Corredor_gondola");
                String fileira = rs.getString("Fileira_gondola");
                
                // String de resultado
                String resultado = String.format(
                    "<html>Produto: <b>%s</b><br>" +
                    "Local: Gôndola Nº %d (Prateleira %d)<br>" +
                    "Corredor: %d (Fileira %s)</html>", 
                    nome, gondola, prateleira, corredor, fileira
                );
                
                lblPosicaoValor.setText(resultado);
                
            } else {
                lblPosicaoValor.setText("<html>Produto com ID " + id + " não encontrado, ou não possui localização definida.</html>");
            }
            
        } catch (NumberFormatException ex) {
            lblPosicaoValor.setText("ID inválido. Use apenas números.");
        } catch (Exception ex) {
            lblPosicaoValor.setText("Erro ao consultar o banco de dados: " + ex.getMessage());
        } finally {
            bancoDeDados.desconectar();
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                localizar_produto window = new localizar_produto();
                window.exibir();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}