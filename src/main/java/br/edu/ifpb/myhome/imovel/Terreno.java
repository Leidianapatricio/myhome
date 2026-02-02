package br.edu.ifpb.myhome.imovel;

import br.edu.ifpb.myhome.prototype.ImovelPrototype;

/**
 * RF02 - Subclasse de Imovel que atua como protótipo: implementa ImovelPrototype
 * e clone() para produzir cópias com configuração padrão ou cópia do estado atual.
 */
public class Terreno extends Imovel implements ImovelPrototype {

    public Terreno(String endereco, double preco, double area) {
        super(endereco, preco);
        setAreaMetrosQuadrados(area);
    }

    @Override
    public Imovel clone() {
        Terreno t = new Terreno(getEndereco(), getPreco(), getAreaMetrosQuadrados());
        t.setBairro(getBairro());
        t.setDescricao(getDescricao());
        t.setQuantidadeSuites(getQuantidadeSuites());
        return t;
    }

    @Override
    public double calcularValor() {
        return getPreco() * getAreaMetrosQuadrados();
    }

    public double getArea() { return getAreaMetrosQuadrados(); }
    public void setArea(double area) { setAreaMetrosQuadrados(area); }
}
