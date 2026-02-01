package br.edu.ifpb.myhome.imovel;

public class SalaComercial extends Imovel {

    public SalaComercial(String endereco, double preco) {
        super(endereco, preco);
    }

    @Override
    public double calcularValor() {
        return getPreco();
    }
}
