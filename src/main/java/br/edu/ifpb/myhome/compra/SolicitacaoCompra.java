package br.edu.ifpb.myhome.compra;

import br.edu.ifpb.myhome.anuncio.Anuncio;
import br.edu.ifpb.myhome.pagamento.FormaPagamento;
import br.edu.ifpb.myhome.usuario.Usuario;

import java.time.LocalDateTime;

/**
 * Solicitação de compra/aluguel: comprador executou pagamento (fictício)
 * e aguarda o vendedor confirmar o recebimento para o anúncio ir para Arquivado.
 */
public class SolicitacaoCompra {

    private final Usuario comprador;
    private final Anuncio anuncio;
    private final String formaPagamentoNome;
    private final LocalDateTime data;

    public SolicitacaoCompra(Usuario comprador, Anuncio anuncio, FormaPagamento formaPagamento) {
        this.comprador = comprador;
        this.anuncio = anuncio;
        this.formaPagamentoNome = formaPagamento != null ? formaPagamento.getNome() : "";
        this.data = LocalDateTime.now();
    }

    public Usuario getComprador() { return comprador; }
    public Anuncio getAnuncio() { return anuncio; }
    public String getFormaPagamentoNome() { return formaPagamentoNome; }
    public LocalDateTime getData() { return data; }
}
