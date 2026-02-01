package src.br.edu.ifpb.myhome.busca;

import src.br.edu.ifpb.myhome.anuncio.Anuncio;

public class FiltroTitulo implements FiltroBusca {

    private final String trecho;

    public FiltroTitulo(String trecho) {
        this.trecho = trecho == null ? "" : trecho.trim().toLowerCase();
    }

    @Override
    public boolean aceita(Anuncio anuncio) {
        if (anuncio == null || trecho.isEmpty()) return true;
        String titulo = anuncio.getTitulo();
        return titulo != null && titulo.toLowerCase().contains(trecho);
    }
}
