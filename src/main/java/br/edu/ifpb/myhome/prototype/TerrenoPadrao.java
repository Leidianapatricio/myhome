package br.edu.ifpb.myhome.prototype;

import br.edu.ifpb.myhome.imovel.Imovel;
import br.edu.ifpb.myhome.imovel.Terreno;

/**
 * RF02 - Protótipo de terreno com configuração padrão: 200 m²,
 * descrição "Terreno padrão". Outras configurações poderão ser adicionadas no futuro.
 */
public class TerrenoPadrao implements ImovelPrototype {

    private static final double AREA_PADRAO_M2 = 200;
    private static final String DESCRICAO_PADRAO = "Terreno padrão";

    @Override
    public Imovel clone() {
        Terreno t = new Terreno("", 0.0, AREA_PADRAO_M2);
        t.setDescricao(DESCRICAO_PADRAO);
        return t;
    }
}
