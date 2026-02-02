package br.edu.ifpb.myhome.notificacao;

/**
 * Tipo de evento que pode ser notificado aos observadores do anúncio.
 * Permite que o Observer saiba QUAL evento ocorreu.
 */
public enum TipoEvento {
    /** Estado do anúncio foi alterado (ex.: Rascunho → Ativo, Ativo → Arquivado). */
    MUDANCA_ESTADO,
    /** Novo usuário foi registrado como interessado no anúncio (ex.: abriu conversa). */
    NOVO_INTERESSADO,
    /** Preço do anúncio foi alterado (venda, aluguel ou temporada). */
    PRECO_ALTERADO
}
