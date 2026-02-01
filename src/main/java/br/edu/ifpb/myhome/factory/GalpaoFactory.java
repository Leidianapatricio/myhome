package br.edu.ifpb.myhome.factory;

import br.edu.ifpb.myhome.imovel.Galpao;
import br.edu.ifpb.myhome.imovel.Imovel;

/**
 * PADRÃO: FACTORY METHOD
 * PAPEL: ConcreteCreator para Galpão
 * FUNÇÃO: Permite criar objetos Galpao sem que o cliente precise conhecer
 * a classe concreta diretamente
 */
public class GalpaoFactory extends ImovelFactory {

    @Override
    public Imovel criarImovel() {
        return new Galpao("", 0.0);
    }
}