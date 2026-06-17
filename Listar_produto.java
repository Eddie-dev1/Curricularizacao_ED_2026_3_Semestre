/*EDUARDO ALVES DOS SANTOS
FERNANDO FREIRE DE OLIVEIRA
NICOLAS YUJI HIRATANI
VICTOR SOARES RIELO*/
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.ResultSet;
import java.text.DecimalFormat;

public class Listar_produto {

    private JFrame frame;
    private JTable tabela;

    BD bancoDeDados = new BD();

    private final DecimalFormat df = new DecimalFormat("0.00");

    public Listar_produto() {
        initialize();
        carregarTabela(); 
    }

    public void exibir() {
        frame.setVisible(true);
    }

    private void initialize() {
        
        frame = new JFrame();
        frame.setTitle("Lista de Produtos");
        frame.setBounds(100, 100, 1050, 500);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JLabel titulo = new JLabel("Produtos Cadastrados");
        titulo.setBounds(400, 10, 300, 30);
        frame.getContentPane().add(titulo);

        // -------------------------------------
        // TABELA (Colunas Simples)
        // -------------------------------------
        String[] colunas = {
            "ID", "Nome", "Tipo", "Marca",
            "Volume", "Peso", "Preço (R$)",
            "Quantidade", "Gôndola", "Prateleira"
        };

        DefaultTableModel modelo = new DefaultTableModel(colunas, 0);
        tabela = new JTable(modelo);

        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBounds(20, 60, 1000, 350);
        frame.getContentPane().add(scroll);

        JButton atualizar = new JButton("Atualizar");
        atualizar.setBounds(450, 420, 120, 25);
        frame.getContentPane().add(atualizar);

        atualizar.addActionListener(e -> carregarTabela());
    }

    // -------------------------------------
    // MÉTODO PARA CARREGAR PRODUTOS (COM DESNORMALIZAÇÃO)
    //------------------
    private void carregarTabela() {
        try {
            bancoDeDados.conectar();

            ResultSet rs = bancoDeDados.listarProdutos(); 

            DefaultTableModel model = (DefaultTableModel) tabela.getModel();
            model.setRowCount(0); 

            while (rs.next()) {
                
                double volumeNormalizado = rs.getDouble("Volume_prod"); 
                double pesoNormalizado = rs.getDouble("Peso_prod");     
                
                // DESNORMALIZAÇÃO
                String volumeExibido = desnormalizarVolume(volumeNormalizado);
                String pesoExibido = desnormalizarPeso(pesoNormalizado);
                
                Object[] linha = {
                    rs.getInt("Id_prod"),
                    rs.getString("Nome_prod"),
                    rs.getString("Tipo_prod"),
                    rs.getString("Marca_prod"),
                    volumeExibido, 
                    pesoExibido, 
                    df.format(rs.getDouble("Preco_prod")), 
                    rs.getInt("Quantidade_prod"),
                    rs.getInt("Numero_gondola"),        
                    rs.getInt("Prateleira_gondola")     
                };
                model.addRow(linha);
            }

            bancoDeDados.desconectar();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Erro ao carregar produtos: " + e.getMessage(), "Erro BD", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // --------------------------------------
    // MÉTODOS DE DESNORMALIZAÇÃO
    // --------------------------------------

   
    private String desnormalizarVolume(double volumeL) {
        if (volumeL == 0) return "N/A";
        
     
        if (volumeL < 1.0) {
            double volumeML = volumeL * 1000.0;
            return df.format(volumeML) + " ml";
        }
      
        return df.format(volumeL) + " L";
    }


    private String desnormalizarPeso(double pesoKG) {
        if (pesoKG == 0) return "N/A";
        
       
        if (pesoKG < 1.0) {
            double pesoG = pesoKG * 1000.0;
            return df.format(pesoG) + " g";
        }
     
        return df.format(pesoKG) + " kg";
    }
}