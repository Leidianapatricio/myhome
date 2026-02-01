package br.edu.ifpb.myhome.factory;

import br.edu.ifpb.myhome.imovel.Imovel;
import br.edu.ifpb.myhome.imovel.Terreno;

public class TerrenoFactory extends ImovelFactory {

    @Override
    public Imovel criarImovel() {
        return new Terreno("", 0.0, 0.0);
    }
}
