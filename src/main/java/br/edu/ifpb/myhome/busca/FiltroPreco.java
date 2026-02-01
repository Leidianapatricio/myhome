package br.edu.ifpb.myhome.busca;

import br.edu.ifpb.myhome.anuncio.Anuncio;

public class FiltroPreco implements FiltroBusca {

    private final double precoMin;
    private final double precoMax;

    public FiltroPreco(double precoMin, double precoMax) {
        this.precoMin = precoMin;
        this.precoMax = precoMax;
    }

    @Override
    public boolean aceita(Anuncio anuncio) {
        if (anuncio == null) return false;
        double p = anuncio.getPreco();
        return p >= precoMin && p <= precoMax;
    }
}
