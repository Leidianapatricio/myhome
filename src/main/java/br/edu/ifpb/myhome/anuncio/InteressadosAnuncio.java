package br.edu.ifpb.myhome.anuncio;

import br.edu.ifpb.myhome.usuario.Usuario;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Responsável por manter a lista de usuários interessados em um anúncio
 * (ex.: quem abriu conversa sobre o anúncio). Centraliza a lógica de
 * adicionar e consultar interessados, mantendo a classe Anuncio mais enxuta.
 */
public class InteressadosAnuncio {

    private final List<Usuario> interessados = new ArrayList<>();

    public void adicionar(Usuario u) {
        if (u != null && !interessados.contains(u)) {
            interessados.add(u);
        }
    }

    public List<Usuario> getInteressados() {
        return Collections.unmodifiableList(interessados);
    }
}
