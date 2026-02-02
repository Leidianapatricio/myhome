package br.edu.ifpb.myhome.estado;

import br.edu.ifpb.myhome.anuncio.Anuncio;

/**
 * Estado Rascunho: proximo() -> Moderação; ativar() -> Ativo (ex.: anúncios carregados do CSV).
 */
public class RascunhoState implements EstadoAnuncio {

    @Override
    public void proximo(Anuncio anuncio) {
        anuncio.setEstado(new ModeracaoState());
    }

    @Override
    public void ativar(Anuncio anuncio) {
        anuncio.setEstado(new AtivoState());
    }

    @Override
    public String getNome() {
        return "Rascunho";
    }
}
