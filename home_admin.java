/*EDUARDO ALVES DOS SANTOS
FERNANDO FREIRE DE OLIVEIRA
NICOLAS YUJI HIRATANI
VICTOR SOARES RIELO*/
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;

public class home_admin { 

    private JFrame frame;
    private Usuario usuario;

    public home_admin(Usuario usuario) {
        this.usuario = usuario;
        initialize();
    }

    public void exibir() {
        frame.setVisible(true);
    }

    private void initialize() {
        
        frame = new JFrame(); 
        frame.setTitle("Menu do Administrador - Logado como: " + usuario.getNome());
        frame.setBounds(100, 100, 400, 250);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);


        JLabel titulo = new JLabel("Administrador");
        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        titulo.setBounds(120, 20, 200, 30);
        frame.getContentPane().add(titulo);
        
        JLabel lblUsuario = new JLabel("Bem-vindo(a), " + usuario.getNome() + " (" + usuario.getTipo() + ")");
        lblUsuario.setBounds(80, 50, 250, 20);
        frame.getContentPane().add(lblUsuario);


        // ----------------------------------------------------
        // Botões de CRUD
        // ----------------------------------------------------
        
        JButton btnGerenciarProd = new JButton("Gerenciar Produtos (CRUD)");
        btnGerenciarProd.setBounds(90, 80, 220, 35);
        frame.getContentPane().add(btnGerenciarProd);

        JButton btnGerenciarLayout = new JButton("Gerenciar Layout (CRUD)");
        btnGerenciarLayout.setBounds(90, 130, 220, 35);
        frame.getContentPane().add(btnGerenciarLayout);

        // Ações dos botões
        btnGerenciarProd.addActionListener(e -> {
        	new home_gerenciarprod().exibir(); 
        });

        btnGerenciarLayout.addActionListener(e -> {
            new home_gerenciarlayout().exibir(); 
        });
    }
}