package br.edu.ifpb.myhome.estado;

import br.edu.ifpb.myhome.anuncio.Anuncio;

public class RascunhoState implements EstadoAnuncio {
    
    @Override
    public void proximo(Anuncio anuncio) {
        anuncio.setEstado(new ModeracaoState());
    }

    @Override
    public String getNome() {
        return "Rascunho";
    }
}
