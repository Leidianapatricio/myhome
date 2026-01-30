package src.br.edu.ifpb.myhome.prototype;

import src.br.edu.ifpb.myhome.imovel.Casa;
import src.br.edu.ifpb.myhome.imovel.Imovel;

public class CasaPadrao implements ImovelPrototype {

    @Override
    public Imovel clone() {
        return new Casa("", 0.0, false);
    }
}
