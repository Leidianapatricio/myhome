package br.edu.ifpb.myhome.notificacao;

import br.edu.ifpb.myhome.anuncio.Anuncio;

/**
 * Observer que envia notificação externa (e-mail/SMS). Trata falhas de envio
 * (ex.: e-mail não enviado) via NotificacaoFalhaHandler, sem propagar exceção.
 */
public class NotificacaoObserver implements Observer {

    private final ServicoNotificacaoExterno servico;
    private final NotificacaoFalhaHandler falhaHandler;

    public NotificacaoObserver(ServicoNotificacaoExterno servico) {
        this(servico, null);
    }

    public NotificacaoObserver(ServicoNotificacaoExterno servico, NotificacaoFalhaHandler falhaHandler) {
        this.servico = servico;
        this.falhaHandler = falhaHandler;
    }

    @Override
    public void atualizar(Anuncio a, TipoEvento evento) {
        if (a == null || evento == null) return;
        try {
            servico.enviarMensagem(a, evento);
        } catch (NotificacaoException e) {
            if (falhaHandler != null) {
                falhaHandler.onFalha(a, evento, e);
            }
        }
    }
}
