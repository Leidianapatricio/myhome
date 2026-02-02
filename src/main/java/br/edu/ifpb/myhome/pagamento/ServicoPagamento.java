package br.edu.ifpb.myhome.pagamento;

import br.edu.ifpb.myhome.anuncio.Anuncio;

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

    /**
     * Confirma o pagamento do anúncio (vendedor confirma recebimento) e transiciona o anúncio para Arquivado.
     * O cliente chama este método em vez de anuncio.confirmarPagamento().
     */
    public void confirmar(Anuncio anuncio, FormaPagamento forma) {
        if (anuncio == null) return;
        double valor = anuncio.getPreco();
        if (forma != null) forma.processar(valor);
        anuncio.getEstado().confirmarPagamento(anuncio);
    }

    /** Processa assinatura de um plano. */
    public String assinarPlano(PlanoAssinatura plano) {
        if (plano == null) return "Plano inválido.";
        return formaPagamento != null
                ? formaPagamento.processar(plano.getValorMensal()) + " Plano: " + plano.getNome() + " - " + plano.getBeneficios()
                : "Selecione uma forma de pagamento para assinar.";
    }
}
