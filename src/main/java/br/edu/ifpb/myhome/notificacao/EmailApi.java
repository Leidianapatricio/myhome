package br.edu.ifpb.myhome.notificacao;

/**
 * API externa de e-mail (adaptee). Interface real que o adapter traduz para o domínio.
 * Em produção, faria o envio HTTP real ao provedor de e-mail.
 */
public class EmailApi {

    /**
     * Envia e-mail.
     * @param destinatario endereço de e-mail do destinatário
     * @param assunto assunto do e-mail
     * @param corpo corpo/mensagem do e-mail
     */
    public void sendEmail(String destinatario, String assunto, String corpo) {
        // Em produção: HttpClient.post(url.servico.email, { "to": destinatario, "subject": assunto, "body": corpo })
    }
}
