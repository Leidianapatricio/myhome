package src.br.edu.ifpb.myhome.prototype;

import src.br.edu.ifpb.myhome.imovel.Apartamento;
import src.br.edu.ifpb.myhome.imovel.Imovel;

public class ApartamentoPadrao implements ImovelPrototype {

    @Override
    public Imovel clone() {
        return new Apartamento("", 0.0, 0, 0.0, 0, false);
    }
}
