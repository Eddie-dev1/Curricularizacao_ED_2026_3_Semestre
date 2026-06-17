/*EDUARDO ALVES DOS SANTOS
FERNANDO FREIRE DE OLIVEIRA
NICOLAS YUJI HIRATANI
VICTOR SOARES RIELO*/
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class ListaDeCompras {

    private JFrame frame;
    private JTable tabelaLista;
    private DefaultTableModel modeloTabela;
    private BD bancoDeDados = new BD(); 
    
    private Map<String, Integer> listaItens = new HashMap<>();

    private JLabel lblGondola;
    private JLabel lblCorredor;
    private JLabel lblPrateleira;
    private JLabel lblFileira;
    private JLabel lblStatusLocalizacao;

    private final String[] COLUNAS = {"Produto", "Quantidade", "Remover"}; 

    public ListaDeCompras() {
        initialize();
    }

    public void exibir() {
        frame.setVisible(true);
    }
    
    public void adicionarItem(String nomeProduto, int quantidade) {
        listaItens.merge(nomeProduto, quantidade, Integer::sum);
        recarregarTabela();
        JOptionPane.showMessageDialog(frame, quantidade + "x " + nomeProduto + " adicionado(s) à lista!", 
                                       "Item Adicionado", JOptionPane.INFORMATION_MESSAGE);
    }

    private void initialize() {
        frame = new JFrame();
        frame.setTitle("🛒 Minha Lista & Localização Rápida");
        frame.setBounds(100, 100, 850, 550); 
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
        frame.getContentPane().setLayout(new BorderLayout(10, 10)); 

        JPanel panelCentral = new JPanel(new GridLayout(1, 2, 10, 0)); 
        
        // Tabela da Lista
        modeloTabela = new DefaultTableModel(COLUNAS, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 1; 
            }
        };
        tabelaLista = new JTable(modeloTabela);
        JScrollPane scrollPane = new JScrollPane(tabelaLista);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Itens na Lista"));
        panelCentral.add(scrollPane);

        // Painel de Localização
        JPanel panelLocalizacao = criarPainelLocalizacao();
        panelCentral.add(panelLocalizacao);
        
        frame.getContentPane().add(panelCentral, BorderLayout.CENTER);
        
        // Botões inferiores
        JPanel panelSouth = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        JButton btnLimpar = new JButton("🗑️ Limpar Lista");
        JButton btnFechar = new JButton("Fechar");
        
        panelSouth.add(btnLimpar);
        panelSouth.add(btnFechar);
        frame.getContentPane().add(panelSouth, BorderLayout.SOUTH);

        btnFechar.addActionListener(e -> frame.dispose());
        
        btnLimpar.addActionListener(e -> {
            if (JOptionPane.showConfirmDialog(frame, "Tem certeza que deseja limpar toda a lista?", "Confirmar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                listaItens.clear();
                recarregarTabela();
                limparLocalizacao();
            }
        });
        
        // Listener para alteração da quantidade
        tabelaLista.getModel().addTableModelListener(e -> {
            int row = e.getFirstRow();
            int column = e.getColumn();
            if (column == 1 && row != -1) {
                try {
                    String nomeProduto = (String) modeloTabela.getValueAt(row, 0);
                    int novaQuantidade = Integer.parseInt(modeloTabela.getValueAt(row, column).toString());
                    
                    if (novaQuantidade <= 0) {
                         removerItem(nomeProduto);
                    } else {
                        listaItens.put(nomeProduto, novaQuantidade);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Quantidade inválida. Use apenas números inteiros.", "Erro", JOptionPane.ERROR_MESSAGE);
                    recarregarTabela();
                }
            }
        });

        // Listener para seleção de item
        tabelaLista.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabelaLista.getSelectedRow() != -1) {
                int selectedRow = tabelaLista.getSelectedRow();
                String nomeProduto = (String) modeloTabela.getValueAt(selectedRow, 0);
                buscarEExibirLocalizacao(nomeProduto);
            }
        });

        // Botão remover na tabela
        tabelaLista.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int col = tabelaLista.columnAtPoint(evt.getPoint());
                if (col == 2) {
                    int row = tabelaLista.rowAtPoint(evt.getPoint());
                    if (row >= 0) {
                        String nomeProduto = (String) modeloTabela.getValueAt(row, 0);
                        removerItem(nomeProduto);
                    }
                }
            }
        });
        
        recarregarTabela();
    }
    
    // MÉTODOS DE LOCALIZAÇÃO ---------------------------------------
    
    private JPanel criarPainelLocalizacao() {
        JPanel panel = new JPanel(new GridLayout(7, 1, 5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("🗺️ Localização do Item Selecionado"));
        
        lblStatusLocalizacao = new JLabel("Selecione um produto da lista.");
        lblStatusLocalizacao.setFont(new Font("Arial", Font.ITALIC, 12));
        
        lblCorredor = new JLabel("Corredor: -");
        lblCorredor.setFont(new Font("Arial", Font.BOLD, 14));
        
        lblGondola = new JLabel("Gôndola: -");
        lblGondola.setFont(new Font("Arial", Font.BOLD, 14));
        
        lblPrateleira = new JLabel("Prateleira: -");
        lblPrateleira.setFont(new Font("Arial", Font.BOLD, 14));

        lblFileira = new JLabel("Fileira: -");
        lblFileira.setFont(new Font("Arial", Font.BOLD, 14));
        
        panel.add(lblStatusLocalizacao);
        panel.add(new JSeparator());
        panel.add(lblCorredor);
        panel.add(lblGondola);
        panel.add(lblPrateleira);
        panel.add(lblFileira);

        return panel;
    }
    
    private void buscarEExibirLocalizacao(String nomeProduto) {
        lblStatusLocalizacao.setText("Buscando localização para: " + nomeProduto);
        
        bancoDeDados.conectar();
        ResultSet rs = null;
        try {
            String query = 
                "SELECT p.Nome_prod, p.Numero_gondola, p.Prateleira_gondola, " +
                "l.Corredor_gondola, l.Fileira_gondola " +
                "FROM produto p " +
                "JOIN layout l ON p.Numero_gondola = l.Numero_gondola " +
                "WHERE p.Nome_prod = '" + nomeProduto + "'";
                
            rs = bancoDeDados.executarQuery(query);

            if (rs != null && rs.next()) {
                String gondola = rs.getString("Numero_gondola");
                String prateleira = rs.getString("Prateleira_gondola");
                String corredor = rs.getString("Corredor_gondola");
                String fileira = rs.getString("Fileira_gondola");
                
                lblCorredor.setText("Corredor: " + corredor);
                lblGondola.setText("Gôndola: " + gondola);
                lblPrateleira.setText("Prateleira: " + prateleira);
                lblFileira.setText("Fileira: " + fileira);

                lblStatusLocalizacao.setText("✅ Localizado: " + nomeProduto);
                
            } else {
                limparLocalizacao();
                lblStatusLocalizacao.setText("Localização não configurada.");
            }
        } catch (Exception e) {
            limparLocalizacao();
            lblStatusLocalizacao.setText("Erro ao buscar: " + e.getMessage());
        } finally {
            bancoDeDados.desconectar();
        }
    }

    private void limparLocalizacao() {
        lblCorredor.setText("Corredor: -");
        lblGondola.setText("Gôndola: -");
        lblPrateleira.setText("Prateleira: -");
        lblFileira.setText("Fileira: -");
    }

    // MÉTODOS DA LISTA ---------------------------------------------
    
    private void removerItem(String nomeProduto) {
        listaItens.remove(nomeProduto);
        recarregarTabela();
        limparLocalizacao();
        JOptionPane.showMessageDialog(frame, nomeProduto + " removido da lista.", "Removido", JOptionPane.INFORMATION_MESSAGE);
    }

    private void recarregarTabela() {
        modeloTabela.setRowCount(0);
        for (Map.Entry<String, Integer> entry : listaItens.entrySet()) {
            modeloTabela.addRow(new Object[]{entry.getKey(), entry.getValue(), "Remover"}); 
        }
    }
}
