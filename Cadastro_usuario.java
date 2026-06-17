/*EDUARDO ALVES DOS SANTOS
FERNANDO FREIRE DE OLIVEIRA
NICOLAS YUJI HIRATANI
VICTOR SOARES RIELO*/
import javax.swing.*;
import java.awt.*;
public class Cadastro_usuario {

    private JFrame frame;
    private JTextField textNome;
    private JTextField textEmail;
    private JPasswordField textSenha;
    
    private BD bancoDeDados = new BD();

    public Cadastro_usuario() {
        initialize();
    }

    public void exibir() {
        frame.setVisible(true);
    }

    private void initialize() {
        frame = new JFrame();
        frame.setTitle("Cadastro de Novo Cliente");
        frame.setBounds(100, 100, 350, 350); 
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
        frame.getContentPane().setLayout(null);

        JLabel titulo = new JLabel("CADASTRO DE CLIENTE");
        titulo.setFont(new Font("Arial", Font.BOLD, 16));
        titulo.setBounds(80, 20, 200, 30);
        frame.getContentPane().add(titulo);
        
        // --- 1. Nome ---
        JLabel lblNome = new JLabel("Nome:");
        lblNome.setBounds(50, 70, 80, 25);
        frame.getContentPane().add(lblNome);
        textNome = new JTextField();
        textNome.setBounds(120, 70, 150, 25);
        frame.getContentPane().add(textNome);

        // --- 2. Email ---
        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setBounds(50, 110, 80, 25);
        frame.getContentPane().add(lblEmail);
        textEmail = new JTextField();
        textEmail.setBounds(120, 110, 150, 25);
        frame.getContentPane().add(textEmail);

        // --- 3. Senha ---
        JLabel lblSenha = new JLabel("Senha:");
        lblSenha.setBounds(50, 150, 80, 25);
        frame.getContentPane().add(lblSenha);
        textSenha = new JPasswordField();
        textSenha.setBounds(120, 150, 150, 25);
        frame.getContentPane().add(textSenha);

        // --- Botão Cadastrar ---
        JButton btnCadastrar = new JButton("Cadastrar");
        btnCadastrar.setBounds(170, 220, 100, 30);
        frame.getContentPane().add(btnCadastrar);

        // Ação do Botão
        btnCadastrar.addActionListener(e -> {
            cadastrarCliente();
        });
     // --- Botão Voltar ---
        JButton btnVoltar = new JButton("Voltar");
        btnVoltar.setBounds(50, 220, 100, 30);
        frame.getContentPane().add(btnVoltar);

        // Ação do botão Voltar
        btnVoltar.addActionListener(e -> {
            frame.dispose(); 
            new Login_usuario().exibir(); 
        });

    }

    
 
    private boolean emailFormatoValido(String email) {
        String regex = "^[\\w\\.-]+@[\\w\\.-]+\\.[a-zA-Z]{2,}$";
        return email.matches(regex);
    }

   
    private boolean dominioEmailValido(String email) {
        String[] dominiosValidos = {
            "gmail.com",
            "outlook.com",
            "hotmail.com",
            "yahoo.com",
            "icloud.com",
            "bol.com.br",
            "uol.com.br",
            "terra.com.br",
            "ig.com.br",
            "r7.com",
            "globo.com"
        };

        if (!email.contains("@")) return false;

        String dominio = email.substring(email.indexOf("@") + 1).toLowerCase();

        for (String d : dominiosValidos) {
            if (dominio.equals(d)) {
                return true;
            }
        }
        return false;
    }

    
    
  
    
    private void cadastrarCliente() {
        String nome = textNome.getText().trim();
        String email = textEmail.getText().trim();
        String senha = new String(textSenha.getPassword()).trim(); 

    
        if (nome.isEmpty() || email.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Preencha todos os campos!", "Erro de Cadastro", JOptionPane.WARNING_MESSAGE);
            return;
        }

     
        if (!emailFormatoValido(email)) {
            JOptionPane.showMessageDialog(frame, "O email informado não possui um formato válido.", "Email Inválido", JOptionPane.WARNING_MESSAGE);
            return;
        }

       
        if (!dominioEmailValido(email)) {
            JOptionPane.showMessageDialog(frame, "Domínio de email não permitido.\nUse email como: gmail, outlook, hotmail, yahoo...", "Domínio Inválido", JOptionPane.WARNING_MESSAGE);
            return;
        }


        bancoDeDados.conectar();
        boolean sucesso = false;

        if (bancoDeDados.estaConectado()) {
            
            sucesso = bancoDeDados.cadastrarNovoCliente(nome, email, senha);
        } else {
             JOptionPane.showMessageDialog(frame, "Não foi possível conectar ao banco de dados.", "Erro BD", JOptionPane.ERROR_MESSAGE);
        }

        bancoDeDados.desconectar();

       
        if (sucesso) {
            JOptionPane.showMessageDialog(frame, "Cadastro de " + nome + " realizado com sucesso! Você já pode fazer login.", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            
            textNome.setText("");
            textEmail.setText("");
            textSenha.setText("");
            
            frame.dispose();
            

        } else {
            JOptionPane.showMessageDialog(frame, "Falha ao cadastrar. O email pode já estar em uso.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}