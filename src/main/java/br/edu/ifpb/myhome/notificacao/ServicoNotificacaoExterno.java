package br.edu.ifpb.myhome.notificacao;

import br.edu.ifpb.myhome.anuncio.Anuncio;

public interface ServicoNotificacaoExterno {

    /**
     * Envia mensagem externa (e-mail/SMS) conforme o anúncio e o tipo de evento.
     * @throws NotificacaoException quando o envio falhar (ex.: e-mail não enviado)
     */
    void enviarMensagem(Anuncio a, TipoEvento tipo) throws NotificacaoException;
}
