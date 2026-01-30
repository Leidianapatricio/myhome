package src.br.edu.ifpb.myhome.imovel;

public class Galpao extends Imovel {

    public Galpao(String endereco, double preco) {
        super(endereco, preco);
    }

    @Override
    public double calcularValor() {
        return getPreco();
    }
}
