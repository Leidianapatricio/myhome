package src.br.edu.ifpb.myhome.estado;

import src.br.edu.ifpb.myhome.anuncio.Anuncio;

/**
 * proximo() -> Vendido. Para suspender (retirado pelo usuário): anuncio.setEstado(new SuspensoState()).
 */
public class AtivoState implements EstadoAnuncio {

    @Override
    public void proximo(Anuncio anuncio) {
        anuncio.setEstado(new VendidoState());
    }

    @Override
    public String getNome() {
        return "Ativo";
    }
}
