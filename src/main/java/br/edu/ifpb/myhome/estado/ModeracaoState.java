package br.edu.ifpb.myhome.estado;

import br.edu.ifpb.myhome.anuncio.Anuncio;

/**
 * Aprovado: proximo() -> Ativo.
 * Reprovado: use anuncio.setEstado(new SuspensoState()) para enviar a Suspenso.
 */
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
