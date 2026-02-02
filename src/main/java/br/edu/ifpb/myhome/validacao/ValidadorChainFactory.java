package br.edu.ifpb.myhome.validacao;

/**
 * Factory que monta a cadeia de validação (Chain of Responsibility).
 * O cliente não configura setProximo manualmente; usa criarCadeiaPadrao().
 */
public final class ValidadorChainFactory {

    private ValidadorChainFactory() {}

    /**
     * Retorna a cabeça da cadeia padrão: Preco → Titulo → Imovel.
     * O cliente usa apenas o retorno para validar; a configuração fica encapsulada.
     */
    public static ValidadorAnuncio criarCadeiaPadrao() {
        ValidadorAnuncio v1 = new ValidadorPreco();
        ValidadorAnuncio v2 = new ValidadorTitulo();
        ValidadorAnuncio v3 = new ValidadorImovel();
        v1.setProximo(v2);
        v2.setProximo(v3);
        return v1;
    }
}
