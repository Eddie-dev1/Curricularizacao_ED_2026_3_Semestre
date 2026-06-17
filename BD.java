/*EDUARDO ALVES DOS SANTOS
FERNANDO FREIRE DE OLIVEIRA
NICOLAS YUJI HIRATANI
VICTOR SOARES RIELO*/
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BD {

    private Connection connection = null;
    private Statement statement = null;

    // ==========================================
    // CONEXÃO
    // ==========================================
    public void conectar() {
        String servidor = "jdbc:mysql://localhost:3306/MapShelf?useTimezone=true&serverTimezone=UTC&useSSL=false";
        String usuario = "root";
        String senha = ""; // coloque sua senha real aqui
        String driver = "com.mysql.cj.jdbc.Driver";

        try {
            Class.forName(driver);
            this.connection = DriverManager.getConnection(servidor, usuario, senha);
            this.statement = this.connection.createStatement();
            System.out.println("Status BD: Conexão estabelecida com sucesso.");
        } catch (Exception e) {
            System.err.println("Status BD: ERRO ao conectar. Verifique MySQL e credenciais!");
            System.out.println("Erro ao conectar: " + e.getMessage());
        }
    }

    public boolean estaConectado() {
        try {
            return this.connection != null && !this.connection.isClosed();
        } catch (Exception e) {
            return false;
        }
    }

    public void desconectar() {
        try {
            if (this.statement != null) this.statement.close();
            if (this.connection != null) this.connection.close();
        } catch (Exception e) {
            System.out.println("Erro ao desconectar: " + e.getMessage());
        }
    }

    // ==========================================
    // USUÁRIOS (login e cadastro)
    // ==========================================
    public ResultSet buscarUsuario(String email, String senha) {
        try {
            String query = "SELECT Nome_usuario, Email_usuario, Tipo_usuario FROM usuario " +
                           "WHERE Email_usuario = '" + email + "' AND Senha_usuario = '" + senha + "'";
            return this.statement.executeQuery(query);
        } catch (Exception e) {
            System.out.println("Erro ao buscar usuário: " + e.getMessage());
            return null;
        }
    }

    public boolean cadastrarNovoCliente(String nome, String email, String senha) {
        PreparedStatement ps = null;
        String tipo = "cliente";

        try {
            String query = "INSERT INTO usuario (Nome_usuario, Email_usuario, Senha_usuario, Tipo_usuario) VALUES (?, ?, ?, ?)";

            ps = this.connection.prepareStatement(query);
            ps.setString(1, nome);
            ps.setString(2, email);
            ps.setString(3, senha);
            ps.setString(4, tipo);

            ps.executeUpdate();
            System.out.println("Novo cliente cadastrado com sucesso: " + email);
            return true;

        } catch (Exception e) {
            System.out.println("Erro ao cadastrar novo cliente: " + e.getMessage());
            return false;

        } finally {
            try { if (ps != null) ps.close(); } catch (SQLException ignored) {}
        }
    }

    // ==========================================
    // PRODUTOS (CRUD + localização)
    // ==========================================
    public ResultSet listarProdutos() {
        try {
            String query = "SELECT * FROM produto ORDER BY Nome_prod";
            return this.statement.executeQuery(query);
        } catch (Exception e) {
            System.out.println("Erro ao listar produtos: " + e.getMessage());
            return null;
        }
    }

    public ResultSet buscarLocalizacaoProduto(int idProduto) {
        try {
            String query =
                "SELECT p.Nome_prod, p.Numero_gondola, p.Prateleira_gondola, l.Corredor_gondola, l.Fileira_gondola " +
                "FROM produto p " +
                "JOIN layout l ON p.Numero_gondola = l.Numero_gondola " +
                "WHERE p.Id_prod = " + idProduto;

            return this.statement.executeQuery(query);
        } catch (Exception e) {
            System.out.println("Erro ao buscar localização do produto: " + e.getMessage());
            return null;
        }
    }

    public void inserirProduto(String nome, String tipo, String marca,
                               double volume, double peso, double preco,
                               int numeroGondola, int quantidade, int prateleira) {

        PreparedStatement ps = null;

        try {
            String query =
                "INSERT INTO produto (Nome_prod, Tipo_prod, Marca_prod, Volume_prod, Peso_prod, Preco_prod, Numero_gondola, Quantidade_prod, Prateleira_gondola) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

            ps = this.connection.prepareStatement(query);
            ps.setString(1, nome);
            ps.setString(2, tipo);
            ps.setString(3, marca);
            ps.setDouble(4, volume);
            ps.setDouble(5, peso);
            ps.setDouble(6, preco);
            ps.setInt(7, numeroGondola);
            ps.setInt(8, quantidade);
            ps.setInt(9, prateleira);

            ps.executeUpdate();
            System.out.println("Produto inserido com sucesso!");

        } catch (Exception e) {
            throw new RuntimeException("Falha ao inserir produto: " + e.getMessage());

        } finally {
            try { if (ps != null) ps.close(); } catch (SQLException ignored) {}
        }
    }

    public void editarProduto(int id, String nome, String tipo, String marca,
                              double volume, double peso, double preco,
                              int numeroGondola, int quantidade, int prateleira) {

        PreparedStatement ps = null;

        try {
            String query =
                "UPDATE produto SET Nome_prod=?, Tipo_prod=?, Marca_prod=?, Volume_prod=?, Peso_prod=?, Preco_prod=?, Numero_gondola=?, Quantidade_prod=?, Prateleira_gondola=? " +
                "WHERE Id_prod=?";

            ps = this.connection.prepareStatement(query);
            ps.setString(1, nome);
            ps.setString(2, tipo);
            ps.setString(3, marca);
            ps.setDouble(4, volume);
            ps.setDouble(5, peso);
            ps.setDouble(6, preco);
            ps.setInt(7, numeroGondola);
            ps.setInt(8, quantidade);
            ps.setInt(9, prateleira);
            ps.setInt(10, id);

            ps.executeUpdate();
            System.out.println("Produto atualizado!");

        } catch (Exception e) {
            System.out.println("Erro ao editar produto: " + e.getMessage());

        } finally {
            try { if (ps != null) ps.close(); } catch (SQLException ignored) {}
        }
    }
    
    
    public Produto buscarProdutoPorId(int idProduto) {

        String sql = "SELECT * FROM produto WHERE Id_prod = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, idProduto);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                Produto p = new Produto();

                p.setId_prod(rs.getInt("Id_prod"));
                p.setNome_prod(rs.getString("Nome_prod"));
                p.setTipo_prod(rs.getString("Tipo_prod"));
                p.setMarca_prod(rs.getString("Marca_prod"));
                p.setVolume_prod(rs.getDouble("Volume_prod"));
                p.setPeso_prod(rs.getDouble("Peso_prod"));
                p.setPreco_prod(rs.getDouble("Preco_prod"));
                p.setPrateleira_prod(rs.getInt("Prateleira_gondola"));

                return p;
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar produto por ID: " + e.getMessage());
        }

        return null;
    }



    public void apagarProduto(int id) {
        try {
            String query = "DELETE FROM produto WHERE Id_prod = " + id;
            this.statement.executeUpdate(query);
            System.out.println("Produto removido!");
        } catch (Exception e) {
            System.out.println("Erro ao apagar produto: " + e.getMessage());
        }
    }

    // ==========================================
    // GÔNDOLAS (CRUD)
    // ==========================================
    public void inserirGondola(int numero, int corredor, int fileira) {
        try {
            String query = "INSERT INTO layout (Numero_gondola, Corredor_gondola, Fileira_gondola) " +
                           "VALUES (" + numero + ", " + corredor + ", " + fileira + ")";
            this.statement.executeUpdate(query);
            System.out.println("Gôndola cadastrada!");
        } catch (Exception e) {
            System.out.println("Erro ao inserir gôndola: " + e.getMessage());
        }
    }

    public ResultSet buscarGondolaPorNumero(int numeroGondola) {
        try {
            String query = "SELECT Corredor_gondola, Fileira_gondola FROM layout WHERE Numero_gondola = " + numeroGondola;
            return this.statement.executeQuery(query);
        } catch (Exception e) {
            System.out.println("Erro ao buscar gôndola: " + e.getMessage());
            return null;
        }
    }

    public ResultSet listarGondolas() {
        try {
            String query = "SELECT Numero_gondola, Corredor_gondola, Fileira_gondola FROM layout ORDER BY Numero_gondola";
            return this.statement.executeQuery(query);
        } catch (Exception e) {
            System.out.println("Erro ao listar gôndolas: " + e.getMessage());
            return null;
        }
    }

    public ResultSet listarNumerosGondolas() {
        try {
            String query = "SELECT Numero_gondola FROM layout ORDER BY Numero_gondola";
            return this.statement.executeQuery(query);
        } catch (Exception e) {
            System.out.println("Erro ao listar números das gôndolas: " + e.getMessage());
            return null;
        }
    }

    public void editarGondola(int numeroGondola, int novoCorredor, int novaFileira) {
        try {
            String query = "UPDATE layout SET Corredor_gondola=" + novoCorredor +
                           ", Fileira_gondola=" + novaFileira +
                           " WHERE Numero_gondola=" + numeroGondola;
            this.statement.executeUpdate(query);
            System.out.println("Gôndola atualizada!");
        } catch (Exception e) {
            System.out.println("Erro ao editar gôndola: " + e.getMessage());
        }
    }

    public void apagarGondola(int numeroGondola) {
        try {
            String query = "DELETE FROM layout WHERE Numero_gondola=" + numeroGondola;
            this.statement.executeUpdate(query);
            System.out.println("Gôndola removida!");
        } catch (Exception e) {
            System.out.println("Erro ao apagar gôndola: " + e.getMessage());
        }
    }

    // ==========================================
    // EXECUÇÕES GENÉRICAS
    // ==========================================
    public ResultSet executarQuery(String sql) {
        try {
            return this.statement.executeQuery(sql);
        } catch (Exception e) {
            System.out.println("Erro ao executar query: " + e.getMessage());
            return null;
        }
    }

    public void executarUpdate(String sql) {
        try {
            this.statement.executeUpdate(sql);
        } catch (Exception e) {
            System.out.println("Erro ao executar update: " + e.getMessage());
        }
    }
}
