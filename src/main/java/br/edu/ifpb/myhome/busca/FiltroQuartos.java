package br.edu.ifpb.myhome.busca;

import br.edu.ifpb.myhome.anuncio.Anuncio;
import br.edu.ifpb.myhome.imovel.Apartamento;

/**
 * RF06 - Filtro por número mínimo de quartos. Aplica-se a apartamentos;
 * casas e terrenos são aceitos se o mínimo for 0, caso contrário excluídos.
 */
public class FiltroQuartos implements FiltroBusca {

    private final int quartosMinimos;

    public FiltroQuartos(int quartosMinimos) {
        this.quartosMinimos = Math.max(0, quartosMinimos);
    }

    @Override
    public boolean aceita(Anuncio anuncio) {
        if (anuncio == null || anuncio.getImovel() == null) return false;
        if (anuncio.getImovel() instanceof Apartamento) {
            return ((Apartamento) anuncio.getImovel()).getQuartos() >= quartosMinimos;
        }
        return quartosMinimos <= 0;
    }
}
