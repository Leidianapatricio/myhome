package src.br.edu.ifpb.myhome.factory;

import src.br.edu.ifpb.myhome.imovel.Casa;
import src.br.edu.ifpb.myhome.imovel.Imovel;

public class CasaFactory extends ImovelFactory {

    @Override
    public Imovel criarImovel() {
        return new Casa("", 0.0, false);
    }
}
