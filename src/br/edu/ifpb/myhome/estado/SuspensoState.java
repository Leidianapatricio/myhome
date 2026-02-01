package src.br.edu.ifpb.myhome.estado;

import src.br.edu.ifpb.myhome.anuncio.Anuncio;

/**
 * Estado Suspenso (RF04): reprovado na moderação ou retirado pelo usuário.
 * Volta para Rascunho para nova edição/submissão.
 */
public class SuspensoState implements EstadoAnuncio {

    @Override
    public void proximo(Anuncio anuncio) {
        anuncio.setEstado(new RascunhoState());
    }

    @Override
    public String getNome() {
        return "Suspenso";
    }
}
