package br.edu.ifpb.myhome.pagamento;

public class PagamentoCartao implements FormaPagamento {

    @Override
    public String getNome() {
        return "Cartão de crédito/débito";
    }

    @Override
    public String processar(double valor) {
        return "Pagamento de " + valor + " processado via cartão.";
    }
}
