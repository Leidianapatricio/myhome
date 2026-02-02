package br.edu.ifpb.myhome.notificacao;

import br.edu.ifpb.myhome.anuncio.Anuncio;

/**
 * Adapter que adapta a SmsApi (API externa de SMS) à interface ServicoNotificacaoExterno.
 * Converte Anuncio + TipoEvento em chamada sendSms(destinatario, mensagem).
 */
public class SmsAdapter implements ServicoNotificacaoExterno {

    private final SmsApi api = new SmsApi();

    @Override
    public void enviarMensagem(Anuncio a, TipoEvento tipo) throws NotificacaoException {
        try {
            String titulo = a != null ? a.getTitulo() : "";
            String mensagem = "Anúncio: " + titulo;
            if (tipo == TipoEvento.MUDANCA_ESTADO) {
                mensagem = "MyHome: o anúncio \"" + titulo + "\" mudou de estado.";
            } else if (tipo == TipoEvento.NOVO_INTERESSADO) {
                mensagem = "MyHome: novo interessado no anúncio \"" + titulo + "\".";
            } else if (tipo == TipoEvento.PRECO_ALTERADO) {
                mensagem = "MyHome: preço alterado no anúncio \"" + titulo + "\".";
            }
            api.sendSms("", mensagem);
        } catch (Exception e) {
            throw new NotificacaoException("Falha ao enviar SMS: " + (e.getMessage() != null ? e.getMessage() : e.getClass().getSimpleName()), e);
        }
    }
}
