package src.br.edu.ifpb.myhome.imovel;

public class Terreno extends Imovel {

    private double area;

    public Terreno(String endereco, double preco, double area) {
        super(endereco, preco);
        this.area = area;
    }

    @Override
    public double calcularValor() {
        return getPreco() * area;
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }
}
