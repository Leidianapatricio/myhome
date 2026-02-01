package br.edu.ifpb.myhome.validacao;

import br.edu.ifpb.myhome.anuncio.Anuncio;

public class ValidadorTitulo extends ValidadorAnuncio {

    @Override
    public boolean validar(Anuncio anuncio) {
        if (anuncio.getTitulo() == null || anuncio.getTitulo().isBlank()) {
            return false;
        }
        return passarParaProximo(anuncio);
    }
}
