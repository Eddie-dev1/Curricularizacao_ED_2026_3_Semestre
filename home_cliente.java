/*EDUARDO ALVES DOS SANTOS
FERNANDO FREIRE DE OLIVEIRA
NICOLAS YUJI HIRATANI
VICTOR SOARES RIELO*/
import javax.swing.*;
import java.awt.*;

public class home_cliente {

    private JFrame frame;
    private Usuario usuario;
    private ListaDeCompras listaCompras; 
    public home_cliente(Usuario usuario) {
        this.usuario = usuario;
        this.listaCompras = new ListaDeCompras(); 
        initialize();
    }

    public void exibir() {
        frame.setVisible(true);
    }

    private void initialize() {
        frame = new JFrame();
        frame.setTitle("Área do Cliente - Bem-vindo(a) " + usuario.getNome());
        frame.setBounds(100, 100, 450, 280); 
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        frame.getContentPane().setLayout(null);

        // Título Principal ---
        JLabel titulo = new JLabel("MENU DO CLIENTE");
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        titulo.setBounds(120, 20, 250, 30);
        frame.getContentPane().add(titulo);
        
        // Mensagem de Boas-vindas ---
        JLabel lblBemVindo = new JLabel("Olá, " + usuario.getNome() + "! Escolha uma opção:");
        lblBemVindo.setBounds(50, 60, 350, 20);
        frame.getContentPane().add(lblBemVindo);

        // ----------------------------------------------------
        // BOTÕES DE FUNCIONALIDADE
        // ----------------------------------------------------

        // Botão: Listar todos os Produtos
        JButton btnListaProdutos = new JButton("Visualizar Produtos (Vitrine)");
        btnListaProdutos.setBounds(100, 100, 250, 40);
        frame.getContentPane().add(btnListaProdutos);

        // Botão: Visualizar Carrinho
        JButton btnVerCarrinho = new JButton("🛒 Visualizar Meu Carrinho & Mapa");
        btnVerCarrinho.setBounds(100, 160, 250, 40); 
        frame.getContentPane().add(btnVerCarrinho);

        btnListaProdutos.addActionListener(e -> {
            new ListaProdutosCliente(listaCompras).exibir();
        });

        // BOTÃO CARRINHO: Exibe o conteúdo da lista
        btnVerCarrinho.addActionListener(e -> {
            listaCompras.exibir();
        });
    }
}