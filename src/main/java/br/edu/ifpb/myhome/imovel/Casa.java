package br.edu.ifpb.myhome.imovel;

import br.edu.ifpb.myhome.prototype.ImovelPrototype;

/**
 * RF02 - Subclasse de Imovel que atua como protótipo: implementa ImovelPrototype
 * e clone() para produzir cópias com configuração padrão ou cópia do estado atual.
 */
public class Casa extends Imovel implements ImovelPrototype {

    private boolean possuiQuintal;

    public Casa(String endereco, double preco, boolean possuiQuintal) {
        super(endereco, preco);
        this.possuiQuintal = possuiQuintal;
    }

    @Override
    public Imovel clone() {
        Casa c = new Casa(getEndereco(), getPreco(), possuiQuintal);
        c.setBairro(getBairro());
        c.setAreaMetrosQuadrados(getAreaMetrosQuadrados());
        c.setDescricao(getDescricao());
        c.setQuantidadeSuites(getQuantidadeSuites());
        return c;
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
