package br.edu.ifpb.myhome.busca;

import br.edu.ifpb.myhome.anuncio.Anuncio;

/**
 * RF06 - Filtro por faixa de área (m²). Usa área do imóvel para todos os tipos
 * (casa, apartamento, terreno).
 */
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
        double area = anuncio.getImovel().getAreaMetrosQuadrados();
        return area >= areaMin && area <= areaMax;
    }
}
