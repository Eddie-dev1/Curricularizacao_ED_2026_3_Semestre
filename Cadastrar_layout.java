/*EDUARDO ALVES DOS SANTOS
FERNANDO FREIRE DE OLIVEIRA
NICOLAS YUJI HIRATANI
VICTOR SOARES RIELO*/
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.Font;

public class Cadastrar_layout {

    private JFrame frame;
    private JTextField txtNumero;
    private JTextField txtCorredor;
    private JTextField txtFileira;

    public Cadastrar_layout() {
        initialize();
    }

    public void exibir() {
        frame.setVisible(true);
    }

    private void initialize() {

        frame = new JFrame();
        frame.setTitle("Cadastrar Gôndola");
        frame.setBounds(100, 100, 400, 330);
        frame.setLayout(null);

        JLabel titulo = new JLabel("Cadastrar Gôndola");
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        titulo.setBounds(90, 20, 250, 25);
        frame.add(titulo);

        JLabel lblNumero = new JLabel("Número da Gôndola:");
        lblNumero.setBounds(30, 80, 150, 20);
        frame.add(lblNumero);

        txtNumero = new JTextField();
        txtNumero.setBounds(180, 80, 150, 25);
        frame.add(txtNumero);

        JLabel lblCorredor = new JLabel("Corredor:");
        lblCorredor.setBounds(30, 130, 150, 20);
        frame.add(lblCorredor);

        txtCorredor = new JTextField();
        txtCorredor.setBounds(180, 130, 150, 25);
        frame.add(txtCorredor);

        JLabel lblFileira = new JLabel("Fileira:");
        lblFileira.setBounds(30, 180, 150, 20);
        frame.add(lblFileira);

        txtFileira = new JTextField();
        txtFileira.setBounds(180, 180, 150, 25);
        frame.add(txtFileira);

        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.setBounds(120, 230, 130, 35);
        frame.add(btnSalvar);

        btnSalvar.addActionListener(e -> salvarLayout());
    }

    private void salvarLayout() {

        try {
            int numero = Integer.parseInt(txtNumero.getText());
            int corredor = Integer.parseInt(txtCorredor.getText());
            int fileira = Integer.parseInt(txtFileira.getText());

            BD banco = new BD();
            banco.conectar();

            if (!banco.estaConectado()) {
                JOptionPane.showMessageDialog(null, "Erro ao conectar ao banco.");
                return;
            }

            banco.inserirGondola(numero, corredor, fileira);   

            JOptionPane.showMessageDialog(null, "Gôndola cadastrada com sucesso!");

            banco.desconectar();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erro ao salvar: " + ex.getMessage());
        }
    }
}

