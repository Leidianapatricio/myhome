package src.br.edu.ifpb.myhome.estado;

import src.br.edu.ifpb.myhome.anuncio.Anuncio;

public class ModeracaoState implements EstadoAnuncio {
    
    @Override
    public void proximo(Anuncio anuncio) {
        anuncio.setEstado(new AtivoState());
    }

    @Override
    public String getNome() {
        return "Moderação";
    }
}
