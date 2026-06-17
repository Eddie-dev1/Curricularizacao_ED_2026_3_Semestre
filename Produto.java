/*EDUARDO ALVES DOS SANTOS
FERNANDO FREIRE DE OLIVEIRA
NICOLAS YUJI HIRATANI
VICTOR SOARES RIELO*/
public class Produto {

    private int id_prod;
    private String nome_prod;
    private String tipo_prod;
    private String marca_prod;
    private double volume_prod;
    private double peso_prod;
    private double preco_prod;
    private int prateleira_prod;

    public int getId_prod() { return id_prod; }
    public void setId_prod(int id_prod) { this.id_prod = id_prod; }

    public String getNome_prod() { return nome_prod; }
    public void setNome_prod(String nome_prod) { this.nome_prod = nome_prod; }

    public String getTipo_prod() { return tipo_prod; }
    public void setTipo_prod(String tipo_prod) { this.tipo_prod = tipo_prod; }

    public String getMarca_prod() { return marca_prod; }
    public void setMarca_prod(String marca_prod) { this.marca_prod = marca_prod; }

    public double getVolume_prod() { return volume_prod; }
    public void setVolume_prod(double volume_prod) { this.volume_prod = volume_prod; }

    public double getPeso_prod() { return peso_prod; }
    public void setPeso_prod(double peso_prod) { this.peso_prod = peso_prod; }

    public double getPreco_prod() { return preco_prod; }
    public void setPreco_prod(double preco_prod) { this.preco_prod = preco_prod; }

    public int getPrateleira_prod() { return prateleira_prod; }
    public void setPrateleira_prod(int prateleira_prod) { this.prateleira_prod = prateleira_prod; }
}
