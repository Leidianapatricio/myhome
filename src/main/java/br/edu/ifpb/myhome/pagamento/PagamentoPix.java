package br.edu.ifpb.myhome.pagamento;

public class PagamentoPix implements FormaPagamento {

    @Override
    public String getNome() {
        return "PIX";
    }

    @Override
    public String processar(double valor) {
        return "Pagamento de " + valor + " processado via PIX.";
    }
}
