package br.edu.ifpb.myhome.notificacao;

import br.edu.ifpb.myhome.anuncio.Anuncio;

public interface ServicoNotificacaoExterno {

    void enviarMensagem(Anuncio a);
}
