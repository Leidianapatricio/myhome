package br.edu.ifpb.myhome.validacao;

import br.edu.ifpb.myhome.anuncio.Anuncio;

public class ValidadorImovel extends ValidadorAnuncio {

    @Override
    public boolean validar(Anuncio a) {
        if (a.getImovel() == null) {
            return false;
        }
        return passarParaProximo(a);
    }
}
