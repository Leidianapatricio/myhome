package br.edu.ifpb.myhome.notificacao;

import br.edu.ifpb.myhome.anuncio.Anuncio;

/**
 * Tratamento de falhas na notificação (ex.: e-mail não enviado).
 * Os observers chamam onFalha quando ocorre uma exceção ao notificar.
 */
@FunctionalInterface
public interface NotificacaoFalhaHandler {

    void onFalha(Anuncio a, TipoEvento evento, Exception e);
}
