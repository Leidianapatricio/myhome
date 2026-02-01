package br.edu.ifpb.myhome.busca;

import br.edu.ifpb.myhome.anuncio.Anuncio;

/**
 * RF06 - Filtro por bairro. Aceita anúncios cujo bairro corresponda ao informado
 * (ignora maiúsculas/minúsculas e espaços).
 */
public class FiltroBairro implements FiltroBusca {

    private final String bairro;

    public FiltroBairro(String bairro) {
        this.bairro = bairro == null ? "" : bairro.trim().toLowerCase();
    }

    @Override
    public boolean aceita(Anuncio anuncio) {
        if (anuncio == null || anuncio.getImovel() == null || bairro.isEmpty()) return true;
        String b = anuncio.getImovel().getBairro();
        return b != null && b.trim().toLowerCase().equals(bairro);
    }
}
