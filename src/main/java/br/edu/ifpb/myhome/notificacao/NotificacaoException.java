package br.edu.ifpb.myhome.notificacao;

/**
 * Exceção lançada quando o envio de notificação (e-mail, SMS, etc.) falha.
 */
public class NotificacaoException extends Exception {

    public NotificacaoException(String mensagem) {
        super(mensagem);
    }

    public NotificacaoException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
