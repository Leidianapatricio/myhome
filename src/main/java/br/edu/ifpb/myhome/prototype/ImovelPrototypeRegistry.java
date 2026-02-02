package br.edu.ifpb.myhome.prototype;

import br.edu.ifpb.myhome.imovel.Apartamento;
import br.edu.ifpb.myhome.imovel.Casa;
import br.edu.ifpb.myhome.imovel.Imovel;
import br.edu.ifpb.myhome.imovel.Terreno;

import java.util.HashMap;
import java.util.Map;

/**
 * RF02 - Registro de protótipos de imóveis. As subclasses de Imovel (Casa, Apartamento, Terreno)
 * são os protótipos: cada uma implementa ImovelPrototype e clone(). Este registro guarda
 * uma instância padrão de cada tipo e devolve cópias (clone) ao clonarPrototipo(tipo).
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

    /** Registro com os protótipos padrão (RF02): instâncias de Casa, Apartamento, Terreno com valores padrão. */
    public static ImovelPrototypeRegistry criarRegistroPadrao() {
        ImovelPrototypeRegistry r = new ImovelPrototypeRegistry();
        Casa protCasa = new Casa("", 0.0, false);
        protCasa.setAreaMetrosQuadrados(80);
        protCasa.setDescricao("Casa residencial padrão");
        protCasa.setQuantidadeSuites(1);
        r.registrar(CASA, protCasa);
        Apartamento protApto = new Apartamento("", 0.0, 2, 60, 1, false);
        protApto.setDescricao("Unidade habitacional em condomínio");
        protApto.setQuantidadeSuites(1);
        r.registrar(APARTAMENTO, protApto);
        Terreno protTerreno = new Terreno("", 0.0, 200);
        protTerreno.setDescricao("Terreno padrão");
        r.registrar(TERRENO, protTerreno);
        return r;
    }
}
