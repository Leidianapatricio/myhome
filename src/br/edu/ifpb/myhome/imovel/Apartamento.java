package src.br.edu.ifpb.myhome.imovel;

public class Apartamento extends Imovel {

    private int quartos;
    private double area;
    private int andar;
    private boolean possuiElevador;

    public Apartamento(String endereco, double preco, int quartos, double area, int andar, boolean possuiElevador) {
        super(endereco, preco);
        this.quartos = quartos;
        this.area = area;
        this.andar = andar;
        this.possuiElevador = possuiElevador;
    }

    @Override
    public double calcularValor() {
        double valor = getPreco() * area * (1 + quartos * 0.1);
        if (possuiElevador) {
            valor *= 1.05;
        }
        return valor;
    }

    public int getQuartos() {
        return quartos;
    }

    public double getArea() {
        return area;
    }

    public int getAndar() {
        return andar;
    }

    public boolean isPossuiElevador() {
        return possuiElevador;
    }

    public void setQuartos(int quartos) {
        this.quartos = quartos;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public void setAndar(int andar) {
        this.andar = andar;
    }

    public void setPossuiElevador(boolean possuiElevador) {
        this.possuiElevador = possuiElevador;
    }
}
