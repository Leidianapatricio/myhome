package src.br.edu.ifpb.myhome.notificacao;

import src.br.edu.ifpb.myhome.anuncio.Anuncio;

public class EmailAdapter implements ServicoNotificacaoExterno {

    private final EmailApi api = new EmailApi();

    @Override
    public void enviarMensagem(Anuncio a) {
        api.sendEmail("", "Anúncio: " + (a != null ? a.getTitulo() : ""));
    }
}
