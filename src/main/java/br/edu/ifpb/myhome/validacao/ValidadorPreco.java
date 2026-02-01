package br.edu.ifpb.myhome.validacao;

import br.edu.ifpb.myhome.anuncio.Anuncio;

public class ValidadorPreco extends ValidadorAnuncio {

    @Override
    public boolean validar(Anuncio anuncio) {
        if (anuncio.getPreco() <= 0) {
            return false;
        }
        return passarParaProximo(anuncio);
    }
}


