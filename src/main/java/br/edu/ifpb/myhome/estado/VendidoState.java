package br.edu.ifpb.myhome.estado;

import br.edu.ifpb.myhome.anuncio.Anuncio;

public class VendidoState implements EstadoAnuncio {
    
    @Override
    public void proximo(Anuncio anuncio) {
        // Estado final — não muda mais
    }

    @Override
    public String getNome() {
        return "Vendido";
    }
}
