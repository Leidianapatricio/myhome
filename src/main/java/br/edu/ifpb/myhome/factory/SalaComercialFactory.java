package br.edu.ifpb.myhome.factory;

import br.edu.ifpb.myhome.imovel.Imovel;
import br.edu.ifpb.myhome.imovel.SalaComercial;

/**
 * PADRÃO: Factory Method
 * PAPEL: ConcreteCreator para Sala Comercial
 * FUNÇÃO: Permite criar objetos SalaComercial sem que o cliente precise conhecer
 * a classe concreta diretamente
 */
public class SalaComercialFactory extends ImovelFactory {

    @Override
    public Imovel criarImovel() {
        return new SalaComercial("", 0.0);
    }
}