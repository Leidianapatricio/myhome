package br.edu.ifpb.myhome.estado;

import br.edu.ifpb.myhome.anuncio.Anuncio;

/**
 * Estado Ativo: confirmarPagamento(anuncio) e proximo(anuncio) transicionam para Arquivado
 * (estado final único). A decisão da transição fica no estado, não no cliente.
 */
public class AtivoState implements EstadoAnuncio {

    @Override
    public void proximo(Anuncio anuncio) {
        anuncio.setEstado(new ArquivadoState());
    }

    @Override
    public void confirmarPagamento(Anuncio anuncio) {
        anuncio.setEstado(new ArquivadoState());
    }

    @Override
    public String getNome() {
        return "Ativo";
    }
}
