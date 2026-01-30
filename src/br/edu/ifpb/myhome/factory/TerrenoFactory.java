package src.br.edu.ifpb.myhome.factory;

import src.br.edu.ifpb.myhome.imovel.Imovel;
import src.br.edu.ifpb.myhome.imovel.Terreno;

public class TerrenoFactory extends ImovelFactory {

    @Override
    public Imovel criarImovel() {
        return new Terreno("", 0.0, 0.0);
    }
}
