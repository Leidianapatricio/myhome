package src.br.edu.ifpb.myhome.busca;

import src.br.edu.ifpb.myhome.anuncio.Anuncio;

import java.util.ArrayList;
import java.util.List;

/**
 * RF06 - Busca avançada: aplica múltiplos filtros combinados (AND).
 * Novos filtros podem ser adicionados sem alterar este serviço.
 */
public class BuscaService {

    public static List<Anuncio> buscar(List<Anuncio> anuncios, List<FiltroBusca> filtros) {
        List<Anuncio> resultado = new ArrayList<>();
        for (Anuncio a : anuncios) {
            boolean aceito = true;
            for (FiltroBusca f : filtros) {
                if (f == null) continue;
                if (!f.aceita(a)) {
                    aceito = false;
                    break;
                }
            }
            if (aceito) resultado.add(a);
        }
        return resultado;
    }
}
