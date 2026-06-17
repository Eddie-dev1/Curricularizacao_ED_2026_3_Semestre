/*EDUARDO ALVES DOS SANTOS
FERNANDO FREIRE DE OLIVEIRA
NICOLAS YUJI HIRATANI
VICTOR SOARES RIELO*/
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.sql.ResultSet;
import java.text.DecimalFormat; // 💡 Importação necessária para formatação
import java.util.Vector;
import java.util.List;
import java.util.ArrayList;

public class ListaProdutosCliente {

    private JFrame frame;
    private JTable tabelaProdutos;
    private DefaultTableModel modeloTabela;
    private BD bancoDeDados = new BD();
    
    private ListaDeCompras listaCompras; 

    private JTextField textBusca;
    private JComboBox<String> comboFiltro;
    

    private final DecimalFormat df = new DecimalFormat("0.00");

    public ListaProdutosCliente(ListaDeCompras listaCompras) {
        this.listaCompras = listaCompras;
        initialize();
    }

    public void exibir() {
        frame.setVisible(true);
        carregarDadosProdutos(null, null); 
    }

    private void initialize() {
        frame = new JFrame();
        frame.setTitle("🛒 Vitrine: Produtos do Mercado (Adicionar ao Carrinho)");
        frame.setBounds(100, 100, 1100, 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
        frame.getContentPane().setLayout(new BorderLayout(0, 0));

        // Painel de Controle (Filtros e Busca)
        JPanel panelControle = new JPanel();
        panelControle.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        frame.getContentPane().add(panelControle, BorderLayout.NORTH);

        panelControle.add(new JLabel("🔍 Buscar por Nome/Marca:"));
        textBusca = new JTextField(20);
        panelControle.add(textBusca);

        panelControle.add(new JLabel("📦 Categoria:"));
        comboFiltro = new JComboBox<>();
        comboFiltro.addItem("TODAS"); 
        carregarCategoriasUnicas(); 
        panelControle.add(comboFiltro);
        
        JButton btnAplicar = new JButton("Aplicar Filtros");
        panelControle.add(btnAplicar);

        // Configuração da Tabela
        modeloTabela = new DefaultTableModel() {
             @Override
             public boolean isCellEditable(int row, int column) {
                 return column == getColumnCount() - 2; 
             }
         };
        tabelaProdutos = new JTable(modeloTabela);
        tabelaProdutos.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);

        JScrollPane scrollPane = new JScrollPane(tabelaProdutos);
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
        
        // Painel Inferior (Botões de Ação)
        JPanel panelSouth = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        
        JButton btnFechar = new JButton("Fechar Vitrine");
        
        panelSouth.add(btnFechar);
        frame.getContentPane().add(panelSouth, BorderLayout.SOUTH);

        // Ações dos Componentes
        btnAplicar.addActionListener(e -> {
            String termo = textBusca.getText().trim();
            String categoria = (String) comboFiltro.getSelectedItem();
            carregarDadosProdutos(termo.isEmpty() ? null : termo, 
                                  "TODAS".equals(categoria) ? null : categoria);
        });
        
        btnFechar.addActionListener(e -> frame.dispose());
        
        tabelaProdutos.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int col = tabelaProdutos.columnAtPoint(evt.getPoint());
                int colCountTotal = modeloTabela.getColumnCount();
                
                if (col == colCountTotal - 1) {
                    int row = tabelaProdutos.rowAtPoint(evt.getPoint());
                    if (row >= 0) {
                        try {
                            String nomeProduto = modeloTabela.getValueAt(row, 0).toString(); 
                            int qtdDisponivelColumnIndex = colCountTotal - 3;
                            
                            int estoqueAtual = (int) modeloTabela.getValueAt(row, qtdDisponivelColumnIndex);
                            String qtdRequeridaStr = modeloTabela.getValueAt(row, colCountTotal - 2).toString();
                            int quantidadeRequerida = Integer.parseInt(qtdRequeridaStr);
                            
                            // VERIFICAÇÃO DE ESTOQUE ZERO
                            if (estoqueAtual <= 0) {
                                JOptionPane.showMessageDialog(frame, "🚫 Produto esgotado! Não é possível adicionar ao carrinho.", "Erro de Estoque", JOptionPane.ERROR_MESSAGE);
                                return;
                            }

                            if (quantidadeRequerida <= 0) {
                                JOptionPane.showMessageDialog(frame, "A quantidade deve ser maior que zero.", "Erro", JOptionPane.WARNING_MESSAGE);
                            } else if (quantidadeRequerida > estoqueAtual) {
                                JOptionPane.showMessageDialog(frame, "Estoque insuficiente. Máximo disponível: " + estoqueAtual, "Erro de Estoque", JOptionPane.WARNING_MESSAGE);
                            } else {
                                listaCompras.adicionarItem(nomeProduto, quantidadeRequerida);
                                JOptionPane.showMessageDialog(frame, quantidadeRequerida + "x " + nomeProduto + " adicionado(s) à lista!", "Item Adicionado", JOptionPane.INFORMATION_MESSAGE);
                            }
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(frame, "Insira uma quantidade válida.", "Erro", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        });
    }
    
    // ------------------------------------
    // MÉTODOS DE DESNORMALIZAÇÃO
    // ------------------------------------
    
    
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
    
    // Métodos de Suporte

    private void carregarCategoriasUnicas() {
        bancoDeDados.conectar();
        ResultSet rs = null;
        try {
           
            rs = bancoDeDados.executarQuery("SELECT DISTINCT Tipo_prod FROM produto ORDER BY Tipo_prod");
            while (rs.next()) {
                comboFiltro.addItem(rs.getString("Tipo_prod"));
            }
        } catch (Exception e) {
            System.err.println("Erro ao carregar categorias: " + e.getMessage());
        } finally {
            bancoDeDados.desconectar();
        }
    }


    private void carregarDadosProdutos(String termoBusca, String categoriaFiltro) {
        bancoDeDados.conectar();
        ResultSet rs = null;
        
       
        String queryBase = "SELECT Nome_prod, Marca_prod, Preco_prod, Peso_prod, Volume_prod, Quantidade_prod, Tipo_prod FROM produto"; 
        Vector<String> clausulasWhere = new Vector<>();
        
        if (termoBusca != null && !termoBusca.isEmpty()) {
            String likeTerm = "'%" + termoBusca + "%'";
            String clausulaBusca = String.format(
                "(Nome_prod LIKE %s OR Marca_prod LIKE %s)", 
                likeTerm, likeTerm
            );
            clausulasWhere.add(clausulaBusca);
        }
        
        if (categoriaFiltro != null && !categoriaFiltro.isEmpty()) {
            String clausulaCategoria = String.format("Tipo_prod = '%s'", categoriaFiltro);
            clausulasWhere.add(clausulaCategoria);
        }
        
        String queryFinal = queryBase;
        if (!clausulasWhere.isEmpty()) {
            queryFinal += " WHERE " + String.join(" AND ", clausulasWhere);
        }
        queryFinal += " ORDER BY Nome_prod"; 

        try {
            rs = bancoDeDados.executarQuery(queryFinal); 
            
           
            List<Object[]> dadosProdutos = new ArrayList<>();
            
            while (rs.next()) {
                Object[] produto = new Object[6]; 
                
                double pesoKG = rs.getDouble("Peso_prod");
                double volumeL = rs.getDouble("Volume_prod");
                int quantidade = rs.getInt("Quantidade_prod");
                
                produto[0] = rs.getString("Nome_prod");
                produto[1] = rs.getString("Marca_prod");
                produto[2] = "R$ " + df.format(rs.getDouble("Preco_prod"));
                
               
                produto[3] = desnormalizarPeso(pesoKG);
                produto[4] = desnormalizarVolume(volumeL);
                
                produto[5] = quantidade;
                
                dadosProdutos.add(produto);
            }

            // Definir Nomes de Coluna
            String[] nomesAmigaveis = {
                "Nome", "Marca", "Preço", "Peso", "Volume", 
                "Qtd. Disponível (OCULTA)", // 💡 Coluna 5: Estoque real
                "Qtd. a Adicionar", 
                "Ação"
            };
            
            modeloTabela.setColumnIdentifiers(nomesAmigaveis);
            int colCountTotal = nomesAmigaveis.length; 
            modeloTabela.setRowCount(0);  
            int qtdDisponivelColumnIndex = 5; 
            
            for (Object[] produto : dadosProdutos) {
                // O array 'produto' tem 6 itens (Nome, Marca, Preco, Peso (Str), Volume (Str), Quantidade (Int))
                Object[] row = new Object[colCountTotal]; 
                
                row[0] = produto[0]; // Nome
                row[1] = produto[1]; // Marca
                row[2] = produto[2]; // Preço (String formatada)
                row[3] = produto[3]; // Peso (String desnormalizada)
                row[4] = produto[4]; // Volume (String desnormalizada)
                
                row[qtdDisponivelColumnIndex] = produto[5]; // Estoque (Quantidade_prod - Int)
                
                row[qtdDisponivelColumnIndex + 1] = 1; 
                row[qtdDisponivelColumnIndex + 2] = "ADICIONAR"; 
                
                modeloTabela.addRow(row);
            }
            
           
            tabelaProdutos.getColumnModel().getColumn(qtdDisponivelColumnIndex).setMinWidth(0);
            tabelaProdutos.getColumnModel().getColumn(qtdDisponivelColumnIndex).setMaxWidth(0);
            tabelaProdutos.getColumnModel().getColumn(qtdDisponivelColumnIndex).setWidth(0);

            tabelaProdutos.getColumnModel().getColumn(colCountTotal - 1).setCellRenderer(new BotaoRenderer());
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Erro ao carregar dados dos produtos: " + e.getMessage(), "Erro BD", JOptionPane.ERROR_MESSAGE);
        } finally {
            bancoDeDados.desconectar();
        }
    }
   
    
	@SuppressWarnings("serial")
	class BotaoRenderer extends DefaultCellEditor implements TableCellRenderer {
        private JButton button;
        
        public BotaoRenderer() {
            super(new JCheckBox());
            button = new JButton("ADICIONAR");
            button.setBackground(new Color(60, 179, 113));
            button.setForeground(Color.WHITE);
            button.setOpaque(true);
            button.setBorderPainted(false);

            editorComponent = button;
            delegate = new DefaultCellEditor.EditorDelegate() {
                @Override
                public void setValue(Object value) {
                    button.setText((value != null) ? value.toString() : "");
                }
                @Override
                public Object getCellEditorValue() {
                    return button.getText();
                }
            };
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            button.setText((value == null) ? "ADICIONAR" : value.toString());
            return button;
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            button.setText((value == null) ? "ADICIONAR" : value.toString());
            return button;
        }
    }
}