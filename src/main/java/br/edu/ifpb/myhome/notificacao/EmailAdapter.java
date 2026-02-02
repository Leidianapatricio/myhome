package br.edu.ifpb.myhome.notificacao;

import br.edu.ifpb.myhome.anuncio.Anuncio;

/**
 * Adapter que traduz a interface do domínio (Anuncio + TipoEvento) para a API externa (destinatario, assunto, corpo).
 * Faz a tradução real: obtém destinatário do anunciante, monta assunto e corpo a partir do anúncio e do evento.
 */
public class EmailAdapter implements ServicoNotificacaoExterno {

    private final EmailApi api = new EmailApi();

    @Override
    public void enviarMensagem(Anuncio a, TipoEvento tipo) throws NotificacaoException {
        try {
            String destinatario = obterEmailAnunciante(a);
            String assunto = montarAssunto(a, tipo);
            String corpo = montarCorpoEmail(a, tipo);
            api.sendEmail(destinatario, assunto, corpo);
        } catch (Exception e) {
            throw new NotificacaoException("Falha ao enviar e-mail: " + (e.getMessage() != null ? e.getMessage() : e.getClass().getSimpleName()), e);
        }
    }

    /** Obtém o e-mail do anunciante (dono do anúncio) para envio da notificação. */
    private String obterEmailAnunciante(Anuncio a) {
        if (a == null || a.getDono() == null) return "";
        return a.getDono().getEmail() != null ? a.getDono().getEmail() : "";
    }

    /** Monta o assunto do e-mail conforme o tipo de evento. */
    private String montarAssunto(Anuncio a, TipoEvento tipo) {
        String titulo = a != null ? a.getTitulo() : "";
        if (tipo == TipoEvento.MUDANCA_ESTADO) {
            return "Mudança no anúncio: " + titulo;
        }
        if (tipo == TipoEvento.NOVO_INTERESSADO) {
            return "Novo interessado no anúncio: " + titulo;
        }
        if (tipo == TipoEvento.PRECO_ALTERADO) {
            return "Preço alterado no anúncio: " + titulo;
        }
        return "Anúncio: " + titulo;
    }

    /** Monta o corpo do e-mail com detalhes do anúncio e do evento. */
    private String montarCorpoEmail(Anuncio a, TipoEvento tipo) {
        if (a == null) return "";
        String titulo = a.getTitulo() != null ? a.getTitulo() : "(sem título)";
        String estado = a.getEstadoAtual();
        StringBuilder sb = new StringBuilder();
        sb.append("Anúncio: ").append(titulo).append("\n");
        sb.append("Estado atual: ").append(estado).append("\n");
        if (tipo == TipoEvento.MUDANCA_ESTADO) {
            sb.append("O estado do seu anúncio foi alterado.\n");
        } else if (tipo == TipoEvento.NOVO_INTERESSADO) {
            sb.append("Há um novo interessado no seu anúncio. Acesse o chat para responder.\n");
        } else if (tipo == TipoEvento.PRECO_ALTERADO) {
            sb.append("Os valores do anúncio foram atualizados.\n");
        }
        if (a.getImovel() != null && a.getImovel().getDescricao() != null && !a.getImovel().getDescricao().isBlank()) {
            sb.append("Descrição: ").append(a.getImovel().getDescricao()).append("\n");
        }
        return sb.toString();
    }
}
