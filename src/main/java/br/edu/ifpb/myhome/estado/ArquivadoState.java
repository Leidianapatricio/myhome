package br.edu.ifpb.myhome.estado;

import br.edu.ifpb.myhome.anuncio.Anuncio;

/**
 * Estado final: anúncio arquivado após compra do imóvel.
 * Qualquer tentativa de mudança de estado lança IllegalStateException.
 */
public class ArquivadoState implements EstadoAnuncio {

    private static final String MSG_FINAL = "Anúncio em estado final (Arquivado): não é possível alterar o estado.";

    @Override
    public void proximo(Anuncio anuncio) {
        throw new IllegalStateException(MSG_FINAL);
    }

    @Override
    public void aplicarResultadoModeracao(Anuncio anuncio, boolean aprovado) {
        throw new IllegalStateException(MSG_FINAL);
    }

    @Override
    public void confirmarPagamento(Anuncio anuncio) {
        throw new IllegalStateException(MSG_FINAL);
    }

    @Override
    public void ativar(Anuncio anuncio) {
        throw new IllegalStateException(MSG_FINAL);
    }

    @Override
    public String getNome() {
        return "Arquivado";
    }
}
