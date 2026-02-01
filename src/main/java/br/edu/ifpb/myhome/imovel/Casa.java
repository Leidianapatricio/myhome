package br.edu.ifpb.myhome.imovel;

public class Casa extends Imovel {

    private boolean possuiQuintal;

    public Casa(String endereco, double preco, boolean possuiQuintal) {
        super(endereco, preco);
        this.possuiQuintal = possuiQuintal;
    }

    @Override
    public double calcularValor() {
        double valor = getPreco();
        if (possuiQuintal) {
            valor *= 1.2;
        }
        return valor;
    }

    public boolean isPossuiQuintal() {
        return possuiQuintal;
    }

    public void setPossuiQuintal(boolean possuiQuintal) {
        this.possuiQuintal = possuiQuintal;
    }
}
