package br.edu.ifpb.myhome.factory;

import br.edu.ifpb.myhome.imovel.Apartamento;
import br.edu.ifpb.myhome.imovel.Imovel;

public class ApartamentoFactory extends ImovelFactory {

    @Override
    public Imovel criarImovel() {
        return new Apartamento("", 0.0, 0, 0.0, 0, false);
    }
}
