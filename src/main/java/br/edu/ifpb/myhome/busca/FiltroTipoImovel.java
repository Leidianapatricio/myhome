package br.edu.ifpb.myhome.busca;

import br.edu.ifpb.myhome.anuncio.Anuncio;
import br.edu.ifpb.myhome.imovel.Apartamento;
import br.edu.ifpb.myhome.imovel.Casa;
import br.edu.ifpb.myhome.imovel.Terreno;

public class FiltroTipoImovel implements FiltroBusca {

    private final String tipo;

    public FiltroTipoImovel(String tipo) {
        this.tipo = tipo == null ? "" : tipo.trim().toLowerCase();
    }

    @Override
    public boolean aceita(Anuncio anuncio) {
        if (anuncio == null || tipo.isEmpty()) return true;
        String nomeTipo = anuncio.getTipoImovel();
        if (nomeTipo == null) return false;
        if (tipo.equals("casa") && anuncio.getImovel() instanceof Casa) return true;
        if (tipo.equals("apartamento") && anuncio.getImovel() instanceof Apartamento) return true;
        if (tipo.equals("terreno") && anuncio.getImovel() instanceof Terreno) return true;
        return nomeTipo.equalsIgnoreCase(tipo);
    }
}
