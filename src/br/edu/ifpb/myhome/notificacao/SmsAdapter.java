package src.br.edu.ifpb.myhome.notificacao;

import src.br.edu.ifpb.myhome.anuncio.Anuncio;

public class SmsAdapter implements ServicoNotificacaoExterno {

    @Override
    public void enviarMensagem(Anuncio a) {
        // envio por SMS
    }
}
