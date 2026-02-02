package br.edu.ifpb.myhome.prototype;

import br.edu.ifpb.myhome.imovel.Apartamento;
import br.edu.ifpb.myhome.imovel.Imovel;

/**
 * RF02 - Protótipo de apartamento com configuração padrão: unidade habitacional
 * em condomínio, 2 quartos, 60 m², 1 suíte, andar 1, sem elevador.
 */
public class ApartamentoPadrao implements ImovelPrototype {

    private static final int QUARTOS_PADRAO = 2;
    private static final double AREA_PADRAO_M2 = 60;
    private static final int ANDAR_PADRAO = 1;
    private static final String DESCRICAO_PADRAO = "Unidade habitacional em condomínio";
    private static final int SUITES_PADRAO = 1;

    @Override
    public Imovel clone() {
        Apartamento a = new Apartamento("", 0.0, QUARTOS_PADRAO, AREA_PADRAO_M2, ANDAR_PADRAO, false);
        a.setDescricao(DESCRICAO_PADRAO);
        a.setQuantidadeSuites(SUITES_PADRAO);
        return a;
    }
}
