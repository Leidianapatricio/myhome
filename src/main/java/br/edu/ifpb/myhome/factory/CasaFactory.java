package br.edu.ifpb.myhome.factory;

import br.edu.ifpb.myhome.imovel.Casa;
import br.edu.ifpb.myhome.imovel.Imovel;

public class CasaFactory extends ImovelFactory {

    @Override
    public Imovel criarImovel() {
        return new Casa("", 0.0, false);
    }
}
