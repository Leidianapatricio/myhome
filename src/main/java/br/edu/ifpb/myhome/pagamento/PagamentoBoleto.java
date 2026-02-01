package br.edu.ifpb.myhome.pagamento;

public class PagamentoBoleto implements FormaPagamento {

    @Override
    public String getNome() {
        return "Boleto bancário";
    }

    @Override
    public String processar(double valor) {
        return "Boleto gerado no valor de " + valor + ". Vencimento em 3 dias úteis.";
    }
}
