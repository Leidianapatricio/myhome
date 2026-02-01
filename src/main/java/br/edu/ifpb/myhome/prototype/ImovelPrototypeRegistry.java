package br.edu.ifpb.myhome.prototype;

import br.edu.ifpb.myhome.imovel.Imovel;

import java.util.HashMap;
import java.util.Map;

public class ImovelPrototypeRegistry {

    private Map<String, ImovelPrototype> prototipos = new HashMap<>();

    public void registrar(String tipo, ImovelPrototype prototipo) {
        prototipos.put(tipo, prototipo);
    }

    public Imovel getPrototype(String tipo) {
        ImovelPrototype p = prototipos.get(tipo);
        return p != null ? p.clone() : null;
    }
}
