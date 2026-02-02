package br.edu.ifpb.myhome.prototype;

import br.edu.ifpb.myhome.imovel.Imovel;

/**
 * RF02 - Interface do protótipo de imóvel. Permite clonar uma configuração
 * padrão para cada tipo (casa, apartamento, terreno); novas configurações
 * poderão ser adicionadas no futuro.
 */
public interface ImovelPrototype {

    Imovel clone();
}
