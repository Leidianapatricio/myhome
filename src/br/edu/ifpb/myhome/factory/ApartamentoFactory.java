package src.br.edu.ifpb.myhome.factory;

import src.br.edu.ifpb.myhome.imovel.Apartamento;
import src.br.edu.ifpb.myhome.imovel.Imovel;

public class ApartamentoFactory extends ImovelFactory {

    @Override
    public Imovel criarImovel() {
        return new Apartamento("", 0.0, 0, 0.0, 0, false);
    }
}
