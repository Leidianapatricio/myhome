package br.edu.ifpb.myhome.estado;

import br.edu.ifpb.myhome.anuncio.Anuncio;

/**
 * Padrão State: as transições são decididas pelos estados, não pelo cliente.
 * O cliente (Main) só chama métodos do contexto (Anuncio), que delegam ao estado atual.
 */
public interface EstadoAnuncio {
    void proximo(Anuncio anuncio);
    String getNome();

    /** Apenas ModeracaoState: transiciona para Ativo (aprovado) ou Suspenso (reprovado). */
    default void aplicarResultadoModeracao(Anuncio anuncio, boolean aprovado) {}

    /** Apenas AtivoState: transiciona para Arquivado ao confirmar pagamento. */
    default void confirmarPagamento(Anuncio anuncio) {}

    /** Apenas RascunhoState: ativa diretamente (ex.: anúncios carregados do CSV). */
    default void ativar(Anuncio anuncio) {}
}
