/*EDUARDO ALVES DOS SANTOS
FERNANDO FREIRE DE OLIVEIRA
NICOLAS YUJI HIRATANI
VICTOR SOARES RIELO*/
public class Usuario {
    private String nome; 
    private String email;
    private String tipo; // 'cliente' ou 'admin'

    // CONSTRUTOR
    public Usuario(String nome, String email, String tipo) { 
        this.nome = nome; 
        this.email = email;
        // Se o tipo vier nulo/vazio, seja padrão 'cliente'
        this.tipo = (tipo == null || tipo.isEmpty()) ? "cliente" : tipo; 
    }

    // GETTERS
    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getTipo() {
        return tipo;
    }
 
    // MÉTODO DE PERMISSÃO
    public boolean isAdmin() {
        return "admin".equalsIgnoreCase(tipo);
    }
}