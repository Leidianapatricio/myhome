package src.br.edu.ifpb.myhome.visitor;

import src.br.edu.ifpb.myhome.anuncio.Anuncio;
import src.br.edu.ifpb.myhome.imovel.Imovel;

public interface Visitor {

    void visitar(Anuncio anuncio);
    void visitar(Imovel imovel);
}
