package src.br.edu.ifpb.myhome.notificacao;

import src.br.edu.ifpb.myhome.anuncio.Anuncio;

public class NotificacaoObserver implements Observer {

    private ServicoNotificacaoExterno servico;

    public NotificacaoObserver(ServicoNotificacaoExterno servico) {
        this.servico = servico;
    }

    @Override
    public void atualizar(Anuncio a) {
        servico.enviarMensagem(a);
    }
}
