package br.edu.ifpb.myhome.busca;

import br.edu.ifpb.myhome.anuncio.Anuncio;
import br.edu.ifpb.myhome.imovel.Apartamento;
import br.edu.ifpb.myhome.imovel.Terreno;

public class FiltroArea implements FiltroBusca {

    private final double areaMin;
    private final double areaMax;

    public FiltroArea(double areaMin, double areaMax) {
        this.areaMin = areaMin;
        this.areaMax = areaMax;
    }

    @Override
    public boolean aceita(Anuncio anuncio) {
        if (anuncio == null || anuncio.getImovel() == null) return false;
        double area = 0;
        if (anuncio.getImovel() instanceof Terreno) {
            area = ((Terreno) anuncio.getImovel()).getArea();
        } else if (anuncio.getImovel() instanceof Apartamento) {
            area = ((Apartamento) anuncio.getImovel()).getArea();
        } else {
            return true;
        }
        return area >= areaMin && area <= areaMax;
    }
}
