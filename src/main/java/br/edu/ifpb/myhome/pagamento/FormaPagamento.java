package br.edu.ifpb.myhome.pagamento;

/**
 * Strategy: interface para formas de pagamento.
 * Novas formas (PIX, boleto, cartão) podem ser adicionadas sem alterar o código que processa o pagamento.
 */
public interface FormaPagamento {

    String getNome();

    /**
     * Processa o pagamento do valor e retorna mensagem de confirmação ou erro.
     */
    String processar(double valor);
}
