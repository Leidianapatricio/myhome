package br.edu.ifpb.myhome.notificacao;

import br.edu.ifpb.myhome.config.Configuracao;

/**
 * API externa de envio de SMS (adaptee).
 * Usa a URL configurada em config.properties (url.servico.sms).
 * Em produção, faria a chamada HTTP real ao provedor de SMS.
 */
public class SmsApi {

    private static final String CONFIG_URL = "url.servico.sms";

    /**
     * Envia SMS para o destinatário.
     * @param destinatario número de telefone ou identificador
     * @param mensagem texto da mensagem
     */
    public void sendSms(String destinatario, String mensagem) {
        String baseUrl = Configuracao.getInstancia().getParametro(CONFIG_URL);
        if (baseUrl == null) baseUrl = "https://api.sms.example.com";
        // Em produção: HttpClient.post(baseUrl, body com "to" e "body"); ex.: new Gson().toJson(Map.of("to", destinatario, "body", mensagem))
        // Stub para ambiente de desenvolvimento/teste (sem chamada HTTP real)
    }
}
