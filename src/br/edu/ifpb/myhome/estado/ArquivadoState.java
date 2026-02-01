package src.br.edu.ifpb.myhome.estado;

import src.br.edu.ifpb.myhome.anuncio.Anuncio;

/**
 * Estado final: anúncio arquivado após compra do imóvel.
 */
public class ArquivadoState implements EstadoAnuncio {

    @Override
    public void proximo(Anuncio anuncio) {
        // Estado final — não muda mais
    }

    @Override
    public String getNome() {
        return "Arquivado";
    }
}
