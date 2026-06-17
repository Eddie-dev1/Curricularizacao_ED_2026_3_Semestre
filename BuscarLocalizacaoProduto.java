/*EDUARDO ALVES DOS SANTOS
FERNANDO FREIRE DE OLIVEIRA
NICOLAS YUJI HIRATANI
VICTOR SOARES RIELO*/
import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class BuscarLocalizacaoProduto {

    private JFrame frame;
    private BD bancoDeDados = new BD();
    
    // Componentes de UI
    private JTextField textProdutoBusca; 
    private JButton btnBuscar;
    private Map<String, Integer> produtosMap = new HashMap<>();

    public BuscarLocalizacaoProduto() {
        initialize();
        carregarMapaProdutos();
    }

    public void exibir() {
        frame.setVisible(true);
    }

    private void initialize() {
        frame = new JFrame();
        frame.setTitle("🗺️ Localização de Produto");
        frame.setBounds(150, 150, 600, 400); 
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout(10, 10));

        // --- Painel Superior (Pesquisa)
        JPanel panelPesquisa = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 15)); // Alinha à esquerda e adiciona mais margem vertical (15)
        panelPesquisa.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10)); 
        
        panelPesquisa.add(new JLabel("Digite o Nome do Produto:"));
        
        textProdutoBusca = new JTextField(15); 
        panelPesquisa.add(textProdutoBusca);
        
        btnBuscar = new JButton("Buscar Localização");
        panelPesquisa.add(btnBuscar);
        
        frame.getContentPane().add(panelPesquisa, BorderLayout.NORTH);
        
        JTextArea areaStatus = new JTextArea("Aguardando pesquisa...");
        areaStatus.setFont(new Font("Arial", Font.ITALIC, 12));
        areaStatus.setEditable(false);
        
        JScrollPane scrollStatus = new JScrollPane(areaStatus);
        
        JPanel panelResultado = new JPanel(new BorderLayout());
        panelResultado.setBorder(BorderFactory.createTitledBorder("Status da Busca"));
        
        panelResultado.add(scrollStatus, BorderLayout.CENTER);
        
        frame.getContentPane().add(panelResultado, BorderLayout.CENTER);

        JButton btnFechar = new JButton("Fechar");
        btnFechar.addActionListener(e -> frame.dispose());
        
        JPanel panelSouth = new JPanel();
        panelSouth.add(btnFechar);
        frame.getContentPane().add(panelSouth, BorderLayout.SOUTH);

        btnBuscar.addActionListener(e -> buscarLocalizacaoPorNome());
        textProdutoBusca.addActionListener(e -> buscarLocalizacaoPorNome());
    }
    

    
    // MÉTODOS AUXILIARES (Não alterados, mas necessários para compilação) ---
    
    private void carregarMapaProdutos() {
        bancoDeDados.conectar();
        ResultSet rs = null;
        try {
            rs = bancoDeDados.executarQuery("SELECT Id_prod, Nome_prod FROM produto");
            
            produtosMap.clear();

            while (rs.next()) {
                int id = rs.getInt("Id_prod");
                String nome = rs.getString("Nome_prod");
                produtosMap.put(nome.toUpperCase(), id); 
            }
            
        } catch (Exception e) {
            System.err.println("Erro ao carregar mapa de produtos: " + e.getMessage());
        } finally {
            bancoDeDados.desconectar();
        }
    }
    
    private void buscarLocalizacaoPorNome() {
        String nomeBusca = textProdutoBusca.getText().trim().toUpperCase();
        
        if (nomeBusca.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Por favor, digite o nome do produto.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (!produtosMap.containsKey(nomeBusca)) {
            JOptionPane.showMessageDialog(frame, "Produto '" + textProdutoBusca.getText().trim() + "' não encontrado no cadastro.", "Produto Não Encontrado", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idProduto = produtosMap.get(nomeBusca);
        buscarEExibirLocalizacao(idProduto);
    }
    
    private void buscarEExibirLocalizacao(int idProduto) {
        bancoDeDados.conectar();
        ResultSet rs = null;
        try {
            rs = bancoDeDados.buscarLocalizacaoProduto(idProduto);

            if (rs != null && rs.next()) {
                String nomeProd = rs.getString("Nome_prod");
                String gondola = rs.getString("Numero_gondola");
                String prateleira = rs.getString("Prateleira_gondola");
                String corredor = rs.getString("Corredor_gondola");
                String fileira = rs.getString("Fileira_gondola");
                
                String mensagemHtml = String.format(
                    "<html>" +
                    "<h2>Localização de: <span style='color: blue;'>%s</span></h2>" +
                    "<hr>" +
                    "<table border='0' cellspacing='10'>" +
                    "<tr><td align='right'><b>Corredor:</b></td><td><span style='font-size: 14pt; color: green;'>%s</span></td></tr>" +
                    "<tr><td align='right'><b>Fileira:</b></td><td><span style='font-size: 14pt; color: green;'>%s</span></td></tr>" +
                    "<tr><td align='right'><b>Gôndola:</b></td><td><span style='font-size: 14pt;'>%s</span></td></tr>" +
                    "<tr><td align='right'><b>Prateleira:</b></td><td><span style='font-size: 14pt;'>%s</span></td></tr>" +
                    "</table>" +
                    "<br><i>Siga as placas para o corredor indicado.</i>" +
                    "</html>",
                    nomeProd, corredor, fileira, gondola, prateleira
                );
                
                JOptionPane.showMessageDialog(
                    frame, 
                    mensagemHtml, 
                    "✅ Produto Localizado!", 
                    JOptionPane.INFORMATION_MESSAGE
                );
                
            } else {
                JOptionPane.showMessageDialog(
                    frame, 
                    "Localização do produto não está configurada (Falta Gôndola/Layout).", 
                    "Erro de Configuração", 
                    JOptionPane.ERROR_MESSAGE
                );
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                frame, 
                "Erro ao buscar localização: " + e.getMessage(), 
                "Erro BD", 
                JOptionPane.ERROR_MESSAGE
            );
        } finally {
            bancoDeDados.desconectar();
        }
    }
    
}
