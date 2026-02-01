package br.edu.ifpb.myhome.estado;

import br.edu.ifpb.myhome.anuncio.Anuncio;

public interface EstadoAnuncio {
    void proximo(Anuncio anuncio);
    String getNome();
}
