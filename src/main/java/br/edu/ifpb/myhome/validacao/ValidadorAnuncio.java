package br.edu.ifpb.myhome.validacao;

import br.edu.ifpb.myhome.anuncio.Anuncio;

/**
 * Chain of Responsibility: cada validador retorna ResultadoValidacao
 * com sucesso ou com a mensagem de erro específica (qual falhou e por quê).
 */
public abstract class ValidadorAnuncio {

    private ValidadorAnuncio proximo;

    public void setProximo(ValidadorAnuncio v) {
        this.proximo = v;
    }

    protected ResultadoValidacao passarParaProximo(Anuncio a) {
        return proximo == null ? ResultadoValidacao.ok() : proximo.validar(a);
    }

    public abstract ResultadoValidacao validar(Anuncio a);
}
