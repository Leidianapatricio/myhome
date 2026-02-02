package br.edu.ifpb.myhome.notificacao;

import br.edu.ifpb.myhome.anuncio.Anuncio;

/**
 * Observer é notificado com o anúncio e o tipo de evento ocorrido
 * (mudança de estado, novo interessado, preço alterado, etc.).
 */
public interface Observer {

    void atualizar(Anuncio a, TipoEvento evento);
}
