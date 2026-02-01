package src.br.edu.ifpb.myhome.busca;

import src.br.edu.ifpb.myhome.anuncio.Anuncio;

/**
 * RF06 - Estratégia de filtro para busca avançada. Filtros podem ser combinados
 * e novos filtros adicionados sem alterar o código de busca principal.
 */
public interface FiltroBusca {

    boolean aceita(Anuncio anuncio);
}
