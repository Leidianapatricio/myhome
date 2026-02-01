package br.edu.ifpb.myhome.imovel;

public class Terreno extends Imovel {

    public Terreno(String endereco, double preco, double area) {
        super(endereco, preco);
        setAreaMetrosQuadrados(area);
    }

    @Override
    public double calcularValor() {
        return getPreco() * getAreaMetrosQuadrados();
    }

    public double getArea() { return getAreaMetrosQuadrados(); }
    public void setArea(double area) { setAreaMetrosQuadrados(area); }
}
