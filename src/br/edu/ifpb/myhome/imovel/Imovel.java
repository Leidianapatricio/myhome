package src.br.edu.ifpb.myhome.imovel;

import src.br.edu.ifpb.myhome.visitor.Visitor;

public abstract class Imovel {

    private String endereco;
    private double preco;

    public Imovel(String endereco, double preco) {
        this.endereco = endereco;
        this.preco = preco;
    }

    public abstract double calcularValor();

    public void aceitar(Visitor v) {
        v.visitar(this);
    }

    public String getEndereco() {
        return endereco;
    }

    public double getPreco() {
        return preco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }
}
