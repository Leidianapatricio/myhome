package br.edu.ifpb.myhome.estado;

import br.edu.ifpb.myhome.anuncio.Anuncio;

/**
 * Estado Moderação: a transição para Ativo ou Suspenso é decidida aqui,
 * via aplicarResultadoModeracao(anuncio, aprovado). O cliente não chama setEstado.
 */
public class ModeracaoState implements EstadoAnuncio {

    @Override
    public void proximo(Anuncio anuncio) {
        anuncio.setEstado(new AtivoState());
    }

    @Override
    public void aplicarResultadoModeracao(Anuncio anuncio, boolean aprovado) {
        if (aprovado) {
            anuncio.setEstado(new AtivoState());
        } else {
            anuncio.setEstado(new SuspensoState());
        }
    }

    @Override
    public String getNome() {
        return "Moderação";
    }
}
