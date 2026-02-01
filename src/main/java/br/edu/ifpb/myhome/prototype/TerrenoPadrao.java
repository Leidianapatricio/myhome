package br.edu.ifpb.myhome.prototype;

import br.edu.ifpb.myhome.imovel.Imovel;
import br.edu.ifpb.myhome.imovel.Terreno;

public class TerrenoPadrao implements ImovelPrototype {

    @Override
    public Imovel clone() {
        return new Terreno("", 0.0, 0.0);
    }
}
