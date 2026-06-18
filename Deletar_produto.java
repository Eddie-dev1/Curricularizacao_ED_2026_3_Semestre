import java.sql.ResultSet;
import javax.swing.*;
import java.awt.*;

public class Deletar_produto {

    private JFrame frame;
    private JTextField txtId;

    private JLabel lblNomeValor;
    private JLabel lblTipoValor;
    private JLabel lblMarcaValor;
    private JLabel lblVolumeValor;
    private JLabel lblPesoValor;
    private JLabel lblPrecoValor;
    private JLabel lblPrateleiraValor;

    public Deletar_produto() {
        initialize();
    }

    public void exibir() {
        frame.setVisible(true);
    }

    private void initialize() {

        frame = new JFrame();
        frame.setTitle("Deletar Produto");
        frame.setBounds(100, 100, 450, 390);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JLabel titulo = new JLabel("DELETAR PRODUTO");
        titulo.setFont(new Font("Arial", Font.BOLD, 16));
        titulo.setBounds(140, 10, 200, 30);
        frame.getContentPane().add(titulo);

        // Campo ID
        JLabel lblId = new JLabel("ID do Produto:");
        lblId.setBounds(50, 60, 120, 25);
        frame.getContentPane().add(lblId);

        txtId = new JTextField();
        txtId.setBounds(150, 60, 180, 25);
        frame.getContentPane().add(txtId);

        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.setBounds(340, 60, 80, 25);
        frame.getContentPane().add(btnBuscar);

        // ---------- LABELS ----------
        JLabel lblNome = new JLabel("Nome:");
        lblNome.setBounds(50, 110, 100, 25);
        frame.getContentPane().add(lblNome);

        lblNomeValor = new JLabel("---");
        lblNomeValor.setBounds(150, 110, 200, 25);
        frame.getContentPane().add(lblNomeValor);

        JLabel lblTipo = new JLabel("Tipo:");
        lblTipo.setBounds(50, 140, 100, 25);
        frame.getContentPane().add(lblTipo);

        lblTipoValor = new JLabel("---");
        lblTipoValor.setBounds(150, 140, 200, 25);
        frame.getContentPane().add(lblTipoValor);

        JLabel lblMarca = new JLabel("Marca:");
        lblMarca.setBounds(50, 170, 100, 25);
        frame.getContentPane().add(lblMarca);

        lblMarcaValor = new JLabel("---");
        lblMarcaValor.setBounds(150, 170, 200, 25);
        frame.getContentPane().add(lblMarcaValor);

        JLabel lblVolume = new JLabel("Volume:");
        lblVolume.setBounds(50, 200, 100, 25);
        frame.getContentPane().add(lblVolume);

        lblVolumeValor = new JLabel("---");
        lblVolumeValor.setBounds(150, 200, 200, 25);
        frame.getContentPane().add(lblVolumeValor);

        JLabel lblPeso = new JLabel("Peso:");
        lblPeso.setBounds(50, 230, 100, 25);
        frame.getContentPane().add(lblPeso);

        lblPesoValor = new JLabel("---");
        lblPesoValor.setBounds(150, 230, 200, 25);
        frame.getContentPane().add(lblPesoValor);

        JLabel lblPreco = new JLabel("Preço:");
        lblPreco.setBounds(50, 260, 100, 25);
        frame.getContentPane().add(lblPreco);

        lblPrecoValor = new JLabel("---");
        lblPrecoValor.setBounds(150, 260, 200, 25);
        frame.getContentPane().add(lblPrecoValor);

        JLabel lblPrateleira = new JLabel("Prateleira:");
        lblPrateleira.setBounds(50, 290, 100, 25);
        frame.getContentPane().add(lblPrateleira);

        lblPrateleiraValor = new JLabel("---");
        lblPrateleiraValor.setBounds(150, 290, 200, 25);
        frame.getContentPane().add(lblPrateleiraValor);

        // Botão Deletar
        JButton btnDeletar = new JButton("Deletar Produto");
        btnDeletar.setBounds(150, 330, 150, 30);
        frame.getContentPane().add(btnDeletar);

        btnBuscar.addActionListener(e -> buscarProduto());
        btnDeletar.addActionListener(e -> deletarProduto());
    }

    // --------------------------
    // BUSCAR PRODUTO NO BANCO
    // --------------------------
    private void buscarProduto() {

        String idTexto = txtId.getText().trim();

        if (idTexto.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Digite o ID!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int id = Integer.parseInt(idTexto);

        BD bd = new BD();
        bd.conectar();

        Produto p = bd.buscarProdutoPorId(id);

        if (p == null) {
            JOptionPane.showMessageDialog(frame, "Produto não encontrado!", "Erro", JOptionPane.ERROR_MESSAGE);
            bd.desconectar();
            return;
        }

        lblNomeValor.setText(p.getNome_prod());
        lblTipoValor.setText(p.getTipo_prod());
        lblMarcaValor.setText(p.getMarca_prod());
        lblVolumeValor.setText(String.valueOf(p.getVolume_prod()));
        lblPesoValor.setText(String.valueOf(p.getPeso_prod()));
        lblPrecoValor.setText(String.valueOf(p.getPreco_prod()));
        lblPrateleiraValor.setText(String.valueOf(p.getPrateleira_prod()));

        bd.desconectar();
    }


    // --------------------------
    // DELETAR PRODUTO NO BANCO
    // --------------------------
    private void deletarProduto() {

        String idStr = txtId.getText().trim();

        if (idStr.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Digite o ID!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int id = Integer.parseInt(idStr);

        int confirmar = JOptionPane.showConfirmDialog(
            frame,
            "Tem certeza que deseja deletar o produto ID " + id + "?",
            "Confirmar Exclusão",
            JOptionPane.YES_NO_OPTION
        );

        if (confirmar == JOptionPane.YES_OPTION) {

            BD banco = new BD();
            banco.conectar();
            banco.apagarProduto(id);
            banco.desconectar();

            JOptionPane.showMessageDialog(frame, "Produto deletado com sucesso!");

            limparCampos();

            frame.dispose();
            home_gerenciarprod home = new home_gerenciarprod();
            home.exibir();
        }
    }

    private void limparCampos() {
        lblNomeValor.setText("---");
        lblTipoValor.setText("---");
        lblMarcaValor.setText("---");
        lblVolumeValor.setText("---");
        lblPesoValor.setText("---");
        lblPrecoValor.setText("---");
        lblPrateleiraValor.setText("---");
    }
}
