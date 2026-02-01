package br.edu.ifpb.myhome.validacao;

import br.edu.ifpb.myhome.anuncio.Anuncio;

public abstract class ValidadorAnuncio {

    private ValidadorAnuncio proximo;

    public void setProximo(ValidadorAnuncio v) {
        this.proximo = v;
    }

    protected boolean passarParaProximo(Anuncio a) {
        return proximo == null || proximo.validar(a);
    }

    public abstract boolean validar(Anuncio a);
}
