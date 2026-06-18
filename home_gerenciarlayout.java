import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class home_gerenciarlayout {

    private JFrame frame;

    public home_gerenciarlayout() {
        initialize();
    }

    public void exibir() {
        frame.setVisible(true);
    }

    private void initialize() {

        frame = new JFrame();
        frame.setTitle("Gerenciar Layout");
        frame.setBounds(100, 100, 400, 300);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JLabel titulo = new JLabel("Gerenciamento de Layout");
        titulo.setBounds(110, 20, 250, 25);
        frame.getContentPane().add(titulo);

        JButton btnCadastrar = new JButton("Cadastrar Gôndola");
        btnCadastrar.setBounds(100, 70, 180, 30);
        frame.getContentPane().add(btnCadastrar);

        JButton btnAtualizar = new JButton("Atualizar Gôndola");
        btnAtualizar.setBounds(100, 120, 180, 30);
        frame.getContentPane().add(btnAtualizar);

        JButton btnDeletar = new JButton("Deletar Gôndola");
        btnDeletar.setBounds(100, 170, 180, 30);
        frame.getContentPane().add(btnDeletar);

        JButton btnListar = new JButton("Listar Gôndolas");
        btnListar.setBounds(100, 220, 180, 30);
        frame.getContentPane().add(btnListar);

        // ações
        btnCadastrar.addActionListener(e -> new Cadastrar_layout().exibir());
        btnAtualizar.addActionListener(e -> new Atualizar_layout().exibir());
        btnDeletar.addActionListener(e -> new Deletar_layout().exibir());
        btnListar.addActionListener(e -> new Listar_layout().exibir());
        
    }
}
