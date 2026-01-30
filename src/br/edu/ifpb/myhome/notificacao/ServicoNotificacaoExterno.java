package src.br.edu.ifpb.myhome.notificacao;

import src.br.edu.ifpb.myhome.anuncio.Anuncio;

public interface ServicoNotificacaoExterno {

    void enviarMensagem(Anuncio a);
}
