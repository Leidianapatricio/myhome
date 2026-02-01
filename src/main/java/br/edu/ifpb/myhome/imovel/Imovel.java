package br.edu.ifpb.myhome.imovel;

public abstract class Imovel {

    private String endereco;
    /** Bairro do imóvel. */
    private String bairro;
    private double preco;
    /** Área em metros quadrados. */
    private double areaMetrosQuadrados;
    /** Descrição/detalhes do imóvel (texto livre). */
    private String descricao;
    /** Quantidade de suítes. */
    private int quantidadeSuites;

    public Imovel(String endereco, double preco) {
        this.endereco = endereco;
        this.preco = preco;
    }

    public abstract double calcularValor();

    public String getEndereco() { return endereco; }
    public String getBairro() { return bairro; }
    public double getPreco() { return preco; }
    public double getAreaMetrosQuadrados() { return areaMetrosQuadrados; }
    public String getDescricao() { return descricao; }
    public int getQuantidadeSuites() { return quantidadeSuites; }

    public void setEndereco(String endereco) { this.endereco = endereco; }
    public void setBairro(String bairro) { this.bairro = bairro != null ? bairro.trim() : ""; }
    public void setPreco(double preco) { this.preco = preco; }
    public void setAreaMetrosQuadrados(double areaMetrosQuadrados) { this.areaMetrosQuadrados = areaMetrosQuadrados; }
    public void setDescricao(String descricao) { this.descricao = descricao != null ? descricao : ""; }
    public void setQuantidadeSuites(int quantidadeSuites) { this.quantidadeSuites = quantidadeSuites; }
}
