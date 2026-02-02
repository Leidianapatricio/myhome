package br.edu.ifpb.myhome.prototype;

import br.edu.ifpb.myhome.imovel.Casa;
import br.edu.ifpb.myhome.imovel.Imovel;

/**
 * RF02 - Protótipo de casa com configuração padrão: casa residencial,
 * 80 m², 1 suíte, sem quintal. Novas configurações poderão ser registradas no futuro.
 */
public class CasaPadrao implements ImovelPrototype {

    private static final double AREA_PADRAO_M2 = 80;
    private static final String DESCRICAO_PADRAO = "Casa residencial padrão";
    private static final int SUITES_PADRAO = 1;

    @Override
    public Imovel clone() {
        Casa c = new Casa("", 0.0, false);
        c.setAreaMetrosQuadrados(AREA_PADRAO_M2);
        c.setDescricao(DESCRICAO_PADRAO);
        c.setQuantidadeSuites(SUITES_PADRAO);
        return c;
    }
}
