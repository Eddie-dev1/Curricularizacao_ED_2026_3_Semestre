import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;

public class Login_usuario {

    private JFrame frame;
    private JTextField textEmail;
    private JPasswordField textSenha;
    private BD bancoDeDados = new BD();

    public Login_usuario() {
        initialize();
    }

    public void exibir() {
        frame.setVisible(true);
    }

    private void initialize() {
        frame = new JFrame();
        frame.setTitle("Login do Sistema");
        frame.setBounds(100, 100, 350, 290); 
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        // --- Título ---
        JLabel titulo = new JLabel("Acesso ao Sistema");
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        titulo.setBounds(80, 20, 200, 30);
        frame.getContentPane().add(titulo);

        // --- Email ---
        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setBounds(50, 70, 80, 25);
        frame.getContentPane().add(lblEmail);
        textEmail = new JTextField();
        textEmail.setBounds(120, 70, 150, 25);
        frame.getContentPane().add(textEmail);

        // --- Senha ---
        JLabel lblSenha = new JLabel("Senha:");
        lblSenha.setBounds(50, 110, 80, 25);
        frame.getContentPane().add(lblSenha);
        textSenha = new JPasswordField();
        textSenha.setBounds(120, 110, 150, 25);
        frame.getContentPane().add(textSenha);

        // --- Botão Login ---
        JButton btnLogin = new JButton("Entrar");
        btnLogin.setBounds(120, 160, 150, 30);
        frame.getContentPane().add(btnLogin);

        // --- Botão de Cadastro ---
        JButton btnCadastro = new JButton("Novo Cliente (Cadastrar)");
        btnCadastro.setBounds(100, 200, 190, 25); 
        frame.getContentPane().add(btnCadastro);

        // Ações dos botões
        btnLogin.addActionListener(e -> {
            autenticarUsuario();
        });
        
        btnCadastro.addActionListener(e -> {
            abrirTelaDeCadastro();
        });
    }

    private void abrirTelaDeCadastro() {
        frame.dispose(); 
        Cadastro_usuario cadastro = new Cadastro_usuario();
        cadastro.exibir();
    }


    private void autenticarUsuario() {
        String email = textEmail.getText().trim();
        String senha = new String(textSenha.getPassword()).trim(); 

        if (email.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Preencha email e senha.", "Erro", JOptionPane.WARNING_MESSAGE);
            return;
        }

        bancoDeDados.conectar();
        Usuario usuarioLogado = null;

        try {
            ResultSet rs = bancoDeDados.buscarUsuario(email, senha);

            if (rs != null && rs.next()) {
                String tipo = rs.getString("Tipo_usuario");
                String nome = rs.getString("Nome_usuario"); 
                
                usuarioLogado = new Usuario(nome, email, tipo);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Erro ao consultar BD: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } finally {
            bancoDeDados.desconectar();
        }

        if (usuarioLogado != null) {
            JOptionPane.showMessageDialog(frame, "Bem-vindo, " + usuarioLogado.getNome() + "!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            
            frame.dispose(); 

         
            if (usuarioLogado.isAdmin()) {
              
                new home_admin(usuarioLogado).exibir();
            } else {
            
                new home_cliente(usuarioLogado).exibir(); 
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Email ou senha incorretos.", "Falha de Login", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Login_usuario window = new Login_usuario();
                window.exibir();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
