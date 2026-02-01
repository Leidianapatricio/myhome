package br.edu.ifpb.myhome.notificacao;

import br.edu.ifpb.myhome.anuncio.Anuncio;

public class SmsAdapter implements ServicoNotificacaoExterno {

    @Override
    public void enviarMensagem(Anuncio a) {
        // envio por SMS
    }
}
