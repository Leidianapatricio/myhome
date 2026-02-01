package br.edu.ifpb.myhome.pagamento;

/**
 * Contexto que usa a Strategy FormaPagamento para processar pagamentos.
 */
public class ServicoPagamento {

    private FormaPagamento formaPagamento;

    public ServicoPagamento(FormaPagamento formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public void setFormaPagamento(FormaPagamento formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public String pagar(double valor) {
        if (formaPagamento == null) return "Forma de pagamento não selecionada.";
        return formaPagamento.processar(valor);
    }

    /** Processa assinatura de um plano. */
    public String assinarPlano(PlanoAssinatura plano) {
        if (plano == null) return "Plano inválido.";
        return formaPagamento != null
                ? formaPagamento.processar(plano.getValorMensal()) + " Plano: " + plano.getNome() + " - " + plano.getBeneficios()
                : "Selecione uma forma de pagamento para assinar.";
    }
}
