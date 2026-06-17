/*EDUARDO ALVES DOS SANTOS
FERNANDO FREIRE DE OLIVEIRA
NICOLAS YUJI HIRATANI
VICTOR SOARES RIELO*/
import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;

public class Deletar_layout {

private JFrame frame;  
private JTextField txtNumeroGondola;  

private JLabel lblCorredorValor;  
private JLabel lblFileiraValor;  

BD bancoDeDados = new BD();  

public Deletar_layout() {  
    initialize();  
}  

public void exibir() {  
    frame.setVisible(true);  
}  

private void initialize() {  
    frame = new JFrame();  
    frame.setTitle("Deletar Gôndola");  
    frame.setBounds(100, 100, 450, 300);  
    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);  
    frame.getContentPane().setLayout(null);  

    JLabel titulo = new JLabel("DELETAR GÔNDOLA");  
    titulo.setFont(new Font("Arial", Font.BOLD, 16));  
    titulo.setBounds(130, 10, 200, 30);  
    frame.getContentPane().add(titulo);  

    JLabel lblGondola = new JLabel("Gôndola:");  
    lblGondola.setBounds(50, 60, 100, 25);  
    frame.getContentPane().add(lblGondola);  

    txtNumeroGondola = new JTextField();  
    txtNumeroGondola.setBounds(150, 60, 150, 25);  
    frame.getContentPane().add(txtNumeroGondola);  

    JButton btnBuscar = new JButton("Buscar");  
    btnBuscar.setBounds(320, 60, 80, 25);  
    frame.getContentPane().add(btnBuscar);  

    JLabel lblCorredor = new JLabel("Corredor:");  
    lblCorredor.setBounds(50, 110, 100, 25);  
    frame.getContentPane().add(lblCorredor);  

    lblCorredorValor = new JLabel("---");  
    lblCorredorValor.setBounds(150, 110, 200, 25);  
    frame.getContentPane().add(lblCorredorValor);  

    JLabel lblFileira = new JLabel("Fileira:");  
    lblFileira.setBounds(50, 140, 100, 25);  
    frame.getContentPane().add(lblFileira);  

    lblFileiraValor = new JLabel("---");  
    lblFileiraValor.setBounds(150, 140, 200, 25);  
    frame.getContentPane().add(lblFileiraValor);  

    JButton btnDeletar = new JButton("Deletar Gôndola");  
    btnDeletar.setBounds(150, 200, 150, 30);  
    frame.getContentPane().add(btnDeletar);  

    btnBuscar.addActionListener(e -> buscarLayout());  
    btnDeletar.addActionListener(e -> deletarLayout());  
}  

private void buscarLayout() {  
    String texto = txtNumeroGondola.getText().trim();  
    if (texto.isEmpty()) {  
        JOptionPane.showMessageDialog(frame, "Digite o número da gôndola!", "Erro", JOptionPane.ERROR_MESSAGE);  
        return;  
    }  

    int numeroGondola;  
    try {  
        numeroGondola = Integer.parseInt(texto);  
    } catch (NumberFormatException e) {  
        JOptionPane.showMessageDialog(frame, "Número da gôndola inválido!", "Erro", JOptionPane.ERROR_MESSAGE);  
        return;  
    }  

    try {  
        bancoDeDados.conectar();  
        ResultSet rs = bancoDeDados.buscarGondolaPorNumero(numeroGondola);  

        if (rs.next()) {  
            lblCorredorValor.setText(String.valueOf(rs.getInt("Corredor_gondola")));  
            lblFileiraValor.setText(String.valueOf(rs.getInt("Fileira_gondola")));  
        } else {  
            JOptionPane.showMessageDialog(frame, "Gôndola não encontrada!", "Erro", JOptionPane.ERROR_MESSAGE);  
            lblCorredorValor.setText("---");  
            lblFileiraValor.setText("---");  
        }  

        bancoDeDados.desconectar();  
    } catch (Exception e) {  
        JOptionPane.showMessageDialog(frame, "Erro ao buscar gôndola: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);  
    }  
}  

private void deletarLayout() {  
    String texto = txtNumeroGondola.getText().trim();  
    if (texto.isEmpty()) {  
        JOptionPane.showMessageDialog(frame, "Digite o número da gôndola!", "Erro", JOptionPane.ERROR_MESSAGE);  
        return;  
    }  

    int numeroGondola;  
    try {  
        numeroGondola = Integer.parseInt(texto);  
    } catch (NumberFormatException e) {  
        JOptionPane.showMessageDialog(frame, "Número da gôndola inválido!", "Erro", JOptionPane.ERROR_MESSAGE);  
        return;  
    }  

    try {  
        bancoDeDados.conectar();  
        ResultSet rs = bancoDeDados.buscarGondolaPorNumero(numeroGondola);  
        if (!rs.next()) {  
            JOptionPane.showMessageDialog(frame, "Gôndola não encontrada! Não é possível deletar.", "Erro", JOptionPane.ERROR_MESSAGE);  
            lblCorredorValor.setText("---");  
            lblFileiraValor.setText("---");  
            bancoDeDados.desconectar();  
            return;  
        }  
      
        lblCorredorValor.setText(String.valueOf(rs.getInt("Corredor_gondola")));  
        lblFileiraValor.setText(String.valueOf(rs.getInt("Fileira_gondola")));  

        int confirmar = JOptionPane.showConfirmDialog(frame,  
                "Tem certeza que deseja deletar a gôndola " + numeroGondola + "?",  
                "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);  

        if (confirmar == JOptionPane.YES_OPTION) {  
            bancoDeDados.apagarGondola(numeroGondola);  
            JOptionPane.showMessageDialog(frame, "Gôndola deletada com sucesso!");  
            frame.dispose();  
        }  
        bancoDeDados.desconectar();  
    } catch (Exception e) {  
        JOptionPane.showMessageDialog(frame, "Erro ao deletar gôndola: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);  
    }  
}  

}
