/*EDUARDO ALVES DOS SANTOS
FERNANDO FREIRE DE OLIVEIRA
NICOLAS YUJI HIRATANI
VICTOR SOARES RIELO*/
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class home_gerenciarprod {

    private JFrame frame;

    public home_gerenciarprod() {
        initialize();
    }

    public void exibir() {
        frame.setVisible(true);
    }

    private void initialize() {

        frame = new JFrame();
        frame.setTitle("Sistema de Gerenciamento");
        frame.setBounds(100, 100, 400, 350);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JLabel titulo = new JLabel("MENU PRINCIPAL");
        titulo.setBounds(130, 20, 200, 30);
        frame.getContentPane().add(titulo);

        // ----------------------------
        // Botão Cadastrar Produto
        // ----------------------------
        JButton btnCadastrar = new JButton("Cadastrar Produto");
        btnCadastrar.setBounds(100, 70, 180, 30);
        frame.getContentPane().add(btnCadastrar);

        btnCadastrar.addActionListener(e -> {
            Cadastrar_produto tela = new Cadastrar_produto();
            tela.exibir();
            frame.dispose();
        });

        // ----------------------------
        // Botão Atualizar Produto
        // ----------------------------
        JButton btnAtualizar = new JButton("Atualizar Produto");
        btnAtualizar.setBounds(100, 110, 180, 30);
        frame.getContentPane().add(btnAtualizar);

        btnAtualizar.addActionListener(e -> {
            Atualizar_produto tela = new Atualizar_produto();
            tela.exibir();
            frame.dispose();
        });

        // ----------------------------
        // Botão Deletar Produto
        // ----------------------------
        JButton btnDeletar = new JButton("Deletar Produto");
        btnDeletar.setBounds(100, 150, 180, 30);
        frame.getContentPane().add(btnDeletar);

        btnDeletar.addActionListener(e -> {
            Deletar_produto tela = new Deletar_produto();
            tela.exibir();
            frame.dispose();
        });

        // ----------------------------
        // Botão Listar Produtos
        // ----------------------------
        JButton btnListar = new JButton("Listar Produtos");
        btnListar.setBounds(100, 190, 180, 30);
        frame.getContentPane().add(btnListar);

        btnListar.addActionListener(e -> {
            Listar_produto tela = new Listar_produto();
            tela.exibir();
            frame.dispose();
        });
    }
}
