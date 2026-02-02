package br.edu.ifpb.myhome.prototype;

import br.edu.ifpb.myhome.imovel.Imovel;

import java.util.HashMap;
import java.util.Map;

/**
 * RF02 - Registro de protótipos de imóveis. Permite obter uma cópia (clone)
 * da configuração padrão para cada tipo (casa, apartamento, terreno).
 * Novos tipos e configurações podem ser registrados no futuro.
 */
public class ImovelPrototypeRegistry {

    private static final String CASA = "casa";
    private static final String APARTAMENTO = "apartamento";
    private static final String TERRENO = "terreno";

    private Map<String, ImovelPrototype> prototipos = new HashMap<>();

    public void registrar(String tipo, ImovelPrototype prototipo) {
        prototipos.put(tipo != null ? tipo.toLowerCase() : "", prototipo);
    }

    /** Retorna uma cópia do protótipo para o tipo (casa, apartamento, terreno), ou null. */
    public Imovel clonarPrototipo(String tipo) {
        ImovelPrototype p = prototipos.get(tipo != null ? tipo.toLowerCase() : "");
        return p != null ? p.clone() : null;
    }

    /** Registro com os protótipos padrão (RF02). Novos podem ser adicionados depois. */
    public static ImovelPrototypeRegistry criarRegistroPadrao() {
        ImovelPrototypeRegistry r = new ImovelPrototypeRegistry();
        r.registrar(CASA, new CasaPadrao());
        r.registrar(APARTAMENTO, new ApartamentoPadrao());
        r.registrar(TERRENO, new TerrenoPadrao());
        return r;
    }
}
